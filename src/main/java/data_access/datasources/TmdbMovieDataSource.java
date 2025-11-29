package data_access.datasources;

import com.moviesearch.domain.entities.Movie;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TmdbMovieDataSource {
    private static final String API_KEY = "5809a1c2d348c627095d9e767cf45d61";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ODA5YTFjMmQzNDhjNjI3MDk1ZDllNzY3Y2Y0NWQ2MSIsIm5iZiI6MTc2MjgwODI3Mi4xNDQsInN1YiI6IjY5MTI1MWQwODA0Nzc2ZGJlMmQyNWU2MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.qx1FvwXuldaEQZeXupCmT9FyBjoU3nrGcSSM_hVHFP8";

    public List<Movie> searchMovies(String query) {
        List<Movie> movies = new ArrayList<>();

        try {
            // API code decipher
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
            String urlString = BASE_URL + "/search/movie?query=" + encodedQuery + "&include_adult=false&language=en-US&page=1";

            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + BEARER_TOKEN);
            connection.setRequestProperty("accept", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // JSON respond receiver
                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray results = jsonResponse.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieJson = results.getJSONObject(i);
                    Movie movie = parseMovieFromJson(movieJson);
                    if (movie != null) {
                        movies.add(movie);
                    }
                }
            } else {
                System.err.println("TMDb API request failed with response code: " + responseCode);
            }

            connection.disconnect();

        } catch (Exception e) {
            System.err.println("Error searching movies from TMDb API: " + e.getMessage());
            e.printStackTrace();
        }

        return movies;
    }

    private Movie parseMovieFromJson(JSONObject movieJson) {
        try {
            int id = movieJson.getInt("id");
            String title = movieJson.getString("title");
            String releaseDate = movieJson.optString("release_date", "");

            // processing genre IDs
            List<Integer> genreIds = new ArrayList<>();
            if (movieJson.has("genre_ids")) {
                JSONArray genreArray = movieJson.getJSONArray("genre_ids");
                for (int i = 0; i < genreArray.length(); i++) {
                    genreIds.add(genreArray.getInt(i));
                }
            }

            String posterPath = movieJson.optString("poster_path", null);
            if (posterPath == null || posterPath.equals("null") || posterPath.isEmpty())  {
                posterPath = "";
            }

            return new Movie(id, title, releaseDate, genreIds, posterPath);

        } catch (Exception e) {
            System.err.println("Error parsing movie JSON: " + e.getMessage());
            return null;
        }
    }
}