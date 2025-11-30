package data_access;

import entity.Movie;
import use_case.common.MovieGateway;
import use_case.common.PagedMovieResult;
import use_case.common.MovieDataAccessException;

import java.util.*;
//Imports for the API call
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
// for the hiding of api
import io.github.cdimascio.dotenv.Dotenv;


public class TMDbMovieDataAccessObject implements MovieGateway {
    private final HttpClient client = HttpClient.newHttpClient();
    private String cachedApiToken = null; // Cache the API token to avoid reloading .env file

    // Helper methods for findById

    // Get API token, caching it after first load
    private String getApiToken() throws Exception {
        if (cachedApiToken != null) {
            return cachedApiToken;
        }

        // Try to load from .env file, fallback to environment variable
        try {
            Dotenv dotenv = Dotenv.load();
            cachedApiToken = dotenv.get("APITOKENKEY");
        } catch (Exception e) {
            // If .env file doesn't exist, try environment variable
            cachedApiToken = System.getenv("APITOKENKEY");
        }

        if (cachedApiToken == null || cachedApiToken.isEmpty()) {
            throw new Exception("API token not found. Please create a .env file in the project root with APITOKENKEY=your_token_here, or set the APITOKENKEY environment variable.");
        }

        return cachedApiToken;
    }

    // makeRequest actually makes the api call
    private String makeRequest(String url) throws Exception {
        String apiToken = getApiToken();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + apiToken)
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // extractString extracts a field for string values (i.e "title": "Avengers Endgame"
    private String extractString(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1) return null;

        start += key.length();
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    // extractNumber extracts numerica fields like "id":299534
    private String extractNumber(String json, String key) {
        int start = json.indexOf(key);
        if (start == -1) return null;
        start += key.length();
        int end = json.indexOf(",", start);
        return json.substring(start, end).trim();
    }

    // the genre lists are weird in tmdb, so we need a loop
    // This method handles the "genres":[{...}] format (single movie endpoint)
    private List<Integer> extractGenreIds(String json) {
        List<Integer> ids = new ArrayList<>();

        int start = json.indexOf("\"genres\":[");
        if (start == -1) return ids;

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

    // Extract genre_ids from search/discover results (format: "genre_ids":[28,12,878])
    private List<Integer> extractGenreIdsFromResult(String json) {
        List<Integer> ids = new ArrayList<>();

        int start = json.indexOf("\"genre_ids\":[");
        if (start == -1) {
            // Fallback to the genres format if genre_ids is not found
            return extractGenreIds(json);
        }

        start += "\"genre_ids\":[".length();
        int end = json.indexOf("]", start);
        if (end == -1) return ids;

        String genreIdsStr = json.substring(start, end).trim();
        if (genreIdsStr.isEmpty()) return ids;

        // Split by comma and parse each ID
        String[] parts = genreIdsStr.split(",");
        for (String part : parts) {
            try {
                ids.add(Integer.parseInt(part.trim()));
            } catch (Exception ignored) {
            }
        }

        return ids;
    }

    // without this helper whenver I used extractNumber to get the movie ID it would return the wrong one
    // we cant just use extractNumber because it would return the id of the collection, not the movie because
    // the id of the collection appears first (imagine avengers collection is a collection of all avengers movies
    // whose id of avengers movies != id avengers endgame
    private String extractMovieId(String json) {

        // The correct movie ID appears after the "homepage" field.
        int homepageIndex = json.indexOf("\"homepage\":");
        if (homepageIndex == -1) return null;

        // Now find the next "id" after homepage
        int idIndex = json.indexOf("\"id\":", homepageIndex);
        if (idIndex == -1) return null;

        idIndex += "\"id\":".length();
        int end = json.indexOf(",", idIndex);

        return json.substring(idIndex, end).trim();
    }


    // parseMovie takes the raw JSON text you got from the TMDb API and turns it into a Movie object
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

        // Extract genres (list of ints) - use genre_ids format for search results
        List<Integer> genreIds = extractGenreIdsFromResult(json);

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
        if (query == null || query.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }

    @Override
    public PagedMovieResult searchByTitle(String query, int page) throws MovieDataAccessException {
        try {
            // TMDb search endpoint: https://api.themoviedb.org/3/search/movie?query=avengers&page=1
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
                    e
            );
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
        if (idIndex == -1) return null;

        idIndex += "\"id\":".length();
        int end = json.indexOf(",", idIndex);
        if (end == -1) {
            end = json.indexOf("}", idIndex);
        }
        if (end == -1) return null;

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
            } catch (Exception ignored) {}
        }

        // Extract genres (list of ints) - use genre_ids format for search results
        List<Integer> genreIds = extractGenreIdsFromResult(json);

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

    // Maximum number of movies to parse from API response to prevent lag
    private static final int MAX_MOVIES_TO_PARSE = 10;

    // Extract all movie objects from the results array in discover/search response
    private List<Movie> parseMoviesFromResults(String json) {
        return parseMoviesFromResults(json, MAX_MOVIES_TO_PARSE);
    }

    // Extract movie objects from the results array, limiting to maxMovies
    // Optimized to stop parsing as soon as we have enough movies
    // This avoids processing the full JSON response when we only need a few movies
    private List<Movie> parseMoviesFromResults(String json, int maxMovies) {
        List<Movie> movies = new ArrayList<>();
        if (maxMovies <= 0) return movies;

        // Find the results array
        int resultsStart = json.indexOf("\"results\":[");
        if (resultsStart == -1) return movies;

        resultsStart += "\"results\":[".length();

        // Parse directly from JSON without extracting the entire results array
        // This is more memory-efficient when we only need a few movies
        int currentPos = resultsStart;
        int moviesParsed = 0;

        while (currentPos < json.length() && moviesParsed < maxMovies) {
            // Find the start of the next movie object
            int movieStart = json.indexOf("{", currentPos);
            if (movieStart == -1 || movieStart >= json.length()) break;

            // Check if we've hit the end of the results array
            int nextBracket = json.indexOf("]", movieStart);
            if (nextBracket != -1 && nextBracket < movieStart) break;

            // Find the matching closing brace for this movie object
            int braceCount = 0;
            int movieEnd = movieStart;
            boolean foundEnd = false;

            for (int i = movieStart; i < json.length(); i++) {
                char c = json.charAt(i);
                if (c == '{') braceCount++;
                if (c == '}') {
                    braceCount--;
                    if (braceCount == 0) {
                        movieEnd = i + 1;
                        foundEnd = true;
                        break;
                    }
                }
            }

            if (foundEnd && movieEnd > movieStart) {
                String movieJson = json.substring(movieStart, movieEnd);
                try {
                    Movie movie = parseMovieFromResult(movieJson);
                    if (movie.getMovieId() != null && movie.getTitle() != null) {
                        movies.add(movie);
                        moviesParsed++;
                        // Stop immediately once we have enough movies
                        if (moviesParsed >= maxMovies) {
                            break;
                        }
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
            // TMDb discover endpoint: https://api.themoviedb.org/3/discover/movie?with_genres=28,12
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

    @Override
    public PagedMovieResult getPopularMovies(int page) throws MovieDataAccessException {
        try {
            // TMDb discover endpoint for popular movies: https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=1
            String url = "https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=" + page;
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
                    "Failed to get popular movies: " + e.getMessage(),
                    e
            );
        }
    }
}
