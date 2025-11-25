package interface_adapter.search_movie;

import entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class SearchMovieState {
    private String query = "";
    private List<Movie> movies = new ArrayList<>();
    private int currentPage = 1;
    private int totalPages = 1;
    private String error;

    public SearchMovieState(SearchMovieState copy) {
        query = copy.query;
        movies = new ArrayList<>(copy.movies);
        currentPage = copy.currentPage;
        totalPages = copy.totalPages;
        error = copy.error;
    }

    public SearchMovieState() {
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
