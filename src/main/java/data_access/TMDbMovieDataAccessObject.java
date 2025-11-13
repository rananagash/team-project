package data_access;

import entity.Movie;
import use_case.common.MovieGateway;
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
            } catch (Exception ignored) {}
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
            } catch (Exception ignored) {}
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

        // Build and return the Movie object
        return new Movie(id, title, plot, genreIds, releaseDate, rating, posterUrl);
    }






    @Override
    public Optional<Movie> findById(String movieId) {
        try {
            String url = "https://api.themoviedb.org/3/movie/" + movieId;
            String json = makeRequest(url);
            Movie movie = parseMovie(json);
            return Optional.of(movie);
        } catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<Movie> searchByTitle(String query) {
        /*
         * TODO(Chester Zhao): Hit TMDb search endpoint and return a list of matching movies.
         */
        return Collections.emptyList();
    }

    @Override
    public List<Movie> filterByGenres(List<Integer> genreIds) {
        /*
         * TODO(Inba Thiyagarajan): Combine popular/discover endpoints to implement genre filtering.
         */
        return Collections.emptyList();
    }
}
