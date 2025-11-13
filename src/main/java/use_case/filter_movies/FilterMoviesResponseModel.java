package use_case.filter_movies;

import entity.Movie;

import java.util.List;

public class FilterMoviesResponseModel {

    private final List<Integer> requestedGenres;
    private final List<Movie> movies;

    public FilterMoviesResponseModel(List<Integer> requestedGenres, List<Movie> movies) {
        this.requestedGenres = requestedGenres;
        this.movies = movies;
    }

    public List<Integer> getRequestedGenres() {
        return requestedGenres;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}

