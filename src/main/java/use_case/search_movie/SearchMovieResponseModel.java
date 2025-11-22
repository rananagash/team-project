package use_case.search_movie;

import entity.Movie;

import java.util.List;

public class SearchMovieResponseModel {

    private final String query;
    private final List<Movie> movies;

    private final int currentPage;
    private final int totalPages;

    public SearchMovieResponseModel(String query, List<Movie> movies, int currentPage, int totalPages) {
        this.query = query;
        this.movies = movies;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
    }

    public SearchMovieResponseModel(String query, List<Movie> movies) {
        this(query, movies, 1, 1);
    }
    public String getQuery() {
        return query;
    }

    public List<Movie> getMovies() {
        return movies;
    }
    public int getCurrentPage() {
        return currentPage;
    }
    public int getTotalPages() {
        return totalPages;
    }
}

