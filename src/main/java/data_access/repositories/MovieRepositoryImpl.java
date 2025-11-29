// data/repositories/MovieRepositoryImpl.java
package data_access.repositories;

import entity.Movie;
import data_access.repositories.MovieRepository;
import data_access.datasources.TmdbMovieDataSource;
import java.util.List;

public class MovieRepositoryImpl implements MovieRepository {
    private final TmdbMovieDataSource dataSource;

    public MovieRepositoryImpl() {
        this.dataSource = new TmdbMovieDataSource();
    }

    @Override
    public List<Movie> searchMovies(String query) {
        return dataSource.searchMovies(query);
    }

    @Override
    public List<Movie> getMoviesByGenre(List<Integer> genreIds) {
        // filter by genre
        return List.of();
    }
}