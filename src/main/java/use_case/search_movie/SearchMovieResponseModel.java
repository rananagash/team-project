package use_case.search_movie;

import entity.Movie;

import java.util.List;

public class SearchMovieResponseModel {

    private final String query;
    private final List<Movie> movies;

    public SearchMovieResponseModel(String query, List<Movie> movies) {
        this.query = query;
        this.movies = movies;
    }

    public String getQuery() {
        return query;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}

