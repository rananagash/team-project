package use_case.common;

import entity.Movie;

import java.util.List;

/**
 * used to package info from TMDB
 */
public class PagedMovieResult {

    private final List<Movie> movies;
    private final int page;
    private final int totalPages;

    public PagedMovieResult(List<Movie> movies, int page, int totalPages) {
        this.movies = movies;
        this.page = page;
        this.totalPages = totalPages;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
