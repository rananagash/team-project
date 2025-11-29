package entity;

import java.util.List;

public class SearchResult {
    private final List<Movie> movies;
    private final String errorMessage;
    private final boolean success;

    public SearchResult(List<Movie> movies, String errorMessage, boolean success) {
        this.movies = movies;
        this.errorMessage = errorMessage;
        this.success = success;
    }

    // Getters
    public List<Movie> getMovies() { return movies; }
    public String getErrorMessage() { return errorMessage; }
    public boolean isSuccess() { return success; }

    // Static factory methods
    public static SearchResult success(List<Movie> movies) {
        return new SearchResult(movies, null, true);
    }

    public static SearchResult error(String errorMessage) {
        return new SearchResult(null, errorMessage, false);
    }
}