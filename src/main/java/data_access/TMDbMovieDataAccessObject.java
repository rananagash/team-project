package data_access;

import entity.Movie;
import use_case.common.MovieGateway;
import use_case.common.PagedMovieResult;
import use_case.common.MovieDataAccessException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
//Imports for the API call
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TMDbMovieDataAccessObject implements MovieGateway {
    private final HttpClient client = HttpClient.newHttpClient();

    // Helper methods for findById

    // makeRequest actually makes the api call
    private String makeRequest(String url) throws Exception {
        String apiToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ODA5YTFjMmQzNDhjNjI3MDk1ZDllNzY3Y2Y0NWQ2MSIsIm5iZiI6MTc2MjgwODI3Mi4xNDQsInN1YiI6IjY5MTI1MWQwODA0Nzc2ZGJlMmQyNWU2MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.qx1FvwXuldaEQZeXupCmT9FyBjoU3nrGcSSM_hVHFP8";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + apiToken)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // extractString extracts a field for string values (i.e "title": "Avengers
    // Endgame"
    private String extractString(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1)
            return null;

        start += key.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    // extractNumber extracts numerica fields like "id":299534
    private String extractNumber(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1)
            return null;
        start += key.length();
        int end = json.indexOf(",", start);
        return json.substring(start, end).trim();
    }

    // the genre lists are weird in tmdb, so we need a loop
    private List<Integer> extractGenreIds(String json) {
        List<Integer> ids = new ArrayList<>();

        int start = json.indexOf("\"genres\":[");
        if (start == -1)
            return ids;

        int end = json.indexOf("]", start);
        String section = json.substring(start, end);

        String[] parts = section.split("\"id\":");
        for (int i = 1; i < parts.length; i++) {
            String num = parts[i].split(",")[0].trim();
            try {
                ids.add(Integer.parseInt(num));
            } catch (Exception ignored) {
            }
        }

        return ids;
    }

    // without this helper whenver I used extractNumber to get the movie ID it would
    // return the wrong one
    // we cant just use extractNumber because it would return the id of the
    // collection, not the movie because
    // the id of the collection appears first (imagine avengers collection is a
    // collection of all avengers movies
    // whose id of avengers movies != id avengers endgame
    private String extractMovieId(String json) {

        // The correct movie ID appears after the "homepage" field.
        int homepageIndex = json.indexOf("\"homepage\":");
        if (homepageIndex == -1)
            return null;

        // Now find the next "id" after homepage
        int idIndex = json.indexOf("\"id\":", homepageIndex);
        if (idIndex == -1)
            return null;

        idIndex += "\"id\":".length();
        int end = json.indexOf(",", idIndex);

        return json.substring(idIndex, end).trim();
    }

    // parseMovie takes the raw JSON text you got from the TMDb API and turns it
    // into a Movie object
    private Movie parseMovie(String json) {
        // Extract simple fields
        String id = extractMovieId(json);
        String title = extractString(json, "\"title\":\"");
        String plot = extractString(json, "\"overview\":\"");
        String releaseDate = extractString(json, "\"release_date\":\"");
        String ratingStr = extractNumber(json, "\"vote_average\":");
        String posterPath = extractString(json, "\"poster_path\":\"");

        // Convert rating to double
        double rating = 0.0;
        if (ratingStr != null) {
            try {
                rating = Double.parseDouble(ratingStr);
            } catch (Exception ignored) {
            }
        }

        // Extract genres (list of ints)
        List<Integer> genreIds = extractGenreIds(json);

        String posterUrl;
        if (posterPath == null) {
            // TMDb did not give a poster_path, so we return an empty string
            posterUrl = "";
        } else {
            // TMDb gives poster_path like "/abc123.jpg"
            // We must prepend the real base URL to make it usable
            posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
        }

        // Extract popularity if available, otherwise use 0.0
        String popularityStr = extractNumber(json, "\"popularity\":");
        double popularity = 0.0;
        if (popularityStr != null) {
            try {
                popularity = Double.parseDouble(popularityStr);
            } catch (Exception ignored) {
            }
        }

        // Build and return the Movie object
        return new Movie(id, title, plot, genreIds, releaseDate, rating, popularity, posterUrl);
    }

    @Override
    public Optional<Movie> findById(String movieId) {
        try {
            String url = "https://api.themoviedb.org/3/movie/" + movieId;
            String json = makeRequest(url);
            Movie movie = parseMovie(json);
            return Optional.of(movie);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Movie> searchByTitle(String query) {
        try {
            PagedMovieResult result = searchByTitle(query, 1);
            return result.getMovies();
        } catch (MovieDataAccessException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public PagedMovieResult searchByTitle(String query, int page) throws MovieDataAccessException {
        try {
            // TMDb search endpoint:
            // https://api.themoviedb.org/3/search/movie?query=avengers&page=1
            String encodedQuery = query.replace(" ", "%20");
            String url = "https://api.themoviedb.org/3/search/movie?query=" + encodedQuery + "&page=" + page;
            String json = makeRequest(url);

            // Parse movies from results
            List<Movie> movies = parseMoviesFromResults(json);

            // Extract pagination info
            int currentPage = extractPageNumber(json, "\"page\":");
            int totalPages = extractPageNumber(json, "\"total_pages\":");

            return new PagedMovieResult(movies, currentPage, totalPages);
        } catch (Exception e) {
            throw new MovieDataAccessException(
                    MovieDataAccessException.Type.NETWORK,
                    "Failed to search movies: " + e.getMessage(),
                    e);
        }
    }

    // Helper method to extract page number from JSON
    private int extractPageNumber(String json, String key) {
        try {
            String numberStr = extractNumber(json, key);
            if (numberStr != null) {
                return Integer.parseInt(numberStr);
            }
        } catch (Exception ignored) {
        }
        return 1; // Default to page 1 if extraction fails
    }

    private String extractMovieIdFromResult(String json) {
        int idIndex = json.indexOf("\"id\":");
        if (idIndex == -1)
            return null;

        idIndex += "\"id\":".length();
        int end = json.indexOf(",", idIndex);
        if (end == -1) {
            end = json.indexOf("}", idIndex);
        }
        if (end == -1)
            return null;

        return json.substring(idIndex, end).trim();
    }

    // Parse a movie from the discover/search results array
    // The structure is slightly different from the single movie endpoint
    private Movie parseMovieFromResult(String json) {
        // Extract simple fields
        String id = extractMovieIdFromResult(json);
        String title = extractString(json, "\"title\":\"");
        String plot = extractString(json, "\"overview\":\"");
        String releaseDate = extractString(json, "\"release_date\":\"");
        String ratingStr = extractNumber(json, "\"vote_average\":");
        String posterPath = extractString(json, "\"poster_path\":\"");

        // Convert rating to double
        double rating = 0.0;
        if (ratingStr != null) {
            try {
                rating = Double.parseDouble(ratingStr);
            } catch (Exception ignored) {
            }
        }

        // Extract genres (list of ints)
        List<Integer> genreIds = extractGenreIds(json);

        String posterUrl;
        if (posterPath == null || posterPath.equals("null")) {
            posterUrl = "";
        } else {
            posterUrl = "https://image.tmdb.org/t/p/w500" + posterPath;
        }

        // Extract popularity if available, otherwise use 0.0
        String popularityStr = extractNumber(json, "\"popularity\":");
        double popularity = 0.0;
        if (popularityStr != null) {
            try {
                popularity = Double.parseDouble(popularityStr);
            } catch (Exception ignored) {
            }
        }

        // Build and return the Movie object
        return new Movie(id, title, plot, genreIds, releaseDate, rating, popularity, posterUrl);
    }

    // Extract all movie objects from the results array in discover/search response
    private List<Movie> parseMoviesFromResults(String json) {
        List<Movie> movies = new ArrayList<>();

        // Find the results array
        int resultsStart = json.indexOf("\"results\":[");
        if (resultsStart == -1)
            return movies;

        resultsStart += "\"results\":[".length();
        int resultsEnd = json.lastIndexOf("]");
        if (resultsEnd == -1 || resultsEnd <= resultsStart)
            return movies;

        String resultsArray = json.substring(resultsStart, resultsEnd);

        // Split by movie objects (each starts with {)
        // We'll find each complete movie object by matching braces
        int currentPos = 0;
        while (currentPos < resultsArray.length()) {
            int movieStart = resultsArray.indexOf("{", currentPos);
            if (movieStart == -1)
                break;

            // Find the matching closing brace
            int braceCount = 0;
            int movieEnd = movieStart;
            for (int i = movieStart; i < resultsArray.length(); i++) {
                char c = resultsArray.charAt(i);
                if (c == '{')
                    braceCount++;
                if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        movieEnd = i + 1;
                        break;
                    }
                }
            }

            if (movieEnd > movieStart) {
                String movieJson = resultsArray.substring(movieStart, movieEnd);
                try {
                    Movie movie = parseMovieFromResult(movieJson);
                    if (movie.getMovieId() != null && movie.getTitle() != null) {
                        movies.add(movie);
                    }
                } catch (Exception e) {
                    // Skip invalid movie entries
                }
            }

            currentPos = movieEnd;
        }

        return movies;
    }

    @Override
    public List<Movie> filterByGenres(List<Integer> genreIds) {
        try {
            // Build URL with comma-separated genre IDs
            // TMDb discover endpoint:
            // https://api.themoviedb.org/3/discover/movie?with_genres=28,12
            StringBuilder urlBuilder = new StringBuilder("https://api.themoviedb.org/3/discover/movie?with_genres=");

            for (int i = 0; i < genreIds.size(); i++) {
                if (i > 0) {
                    urlBuilder.append(",");
                }
                urlBuilder.append(genreIds.get(i));
            }

            // Add sort_by parameter to get popular movies first
            urlBuilder.append("&sort_by=popularity.desc");

            String url = urlBuilder.toString();
            String json = makeRequest(url);

            // Parse the results array and convert to Movie objects

            return parseMoviesFromResults(json);
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
