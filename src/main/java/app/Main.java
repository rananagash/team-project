package app;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    public static void main(String[] args) throws Exception {
        AppBuilder builder = new AppBuilder();

        /*
         * TODO(team): Build the main window here and inject controllers into the UI.
         */
        builder.buildSearchMovieController();
        builder.buildFilterMoviesController();
        builder.buildAddWatchListController();
        builder.buildCompareWatchListController();
        builder.buildViewWatchHistoryController();
        builder.buildReviewMovieController();

        // We are going to use the token directly (for now)
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1ODA5YTFjMmQzNDhjNjI3MDk1ZDllNzY3Y2Y0NWQ2MSIsIm5iZiI6MTc2MjgwODI3Mi4xNDQsInN1YiI6IjY5MTI1MWQwODA0Nzc2ZGJlMmQyNWU2MiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.qx1FvwXuldaEQZeXupCmT9FyBjoU3nrGcSSM_hVHFP8";

        // Whenever we end up using an environment variable later, we'll use this
        // String token = System.getenv("TMDB_API_READ_ACCESS");

        // Calling for the most popular movies
        String url = "https://api.themoviedb.org/3/movie/popular?language=en-US";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .header("Accept", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println(response.body());
    }
}
