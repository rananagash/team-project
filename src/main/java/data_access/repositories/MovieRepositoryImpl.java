// data/repositories/MovieRepositoryImpl.java
package data_access.repositories;

import com.moviesearch.domain.entities.Movie;
import com.moviesearch.data.repositories.MovieRepository;
import com.moviesearch.data.datasources.TmdbMovieDataSource;
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