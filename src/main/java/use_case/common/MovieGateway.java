package use_case.common;

import entity.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieGateway {

    Optional<Movie> findById(String movieId);

    List<Movie> searchByTitle(String query);

    List<Movie> filterByGenres(List<Integer> genreIds);
}

