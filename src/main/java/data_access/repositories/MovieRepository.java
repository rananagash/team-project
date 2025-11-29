package data_access.repositories;

import entity.Movie;
import java.util.List;

public interface MovieRepository {
    List<Movie> searchMovies(String query);
    List<Movie> getMoviesByGenre(List<Integer> genreIds);
}