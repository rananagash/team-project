package data_access;

import entity.Movie;
import use_case.common.MovieGateway;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TMDbMovieDataAccessObject implements MovieGateway {

    @Override
    public Optional<Movie> findById(String movieId) {
        /*
         * TODO(team): Call the TMDb detail endpoint and populate a Movie from the response.
         */
        return Optional.empty();
    }

    @Override
    public List<Movie> searchByTitle(String query) {
        /*
         * TODO(Chester Zhao): Hit TMDb search endpoint and return a list of matching movies.
         */
        return Collections.emptyList();
    }

    @Override
    public List<Movie> filterByGenres(List<Integer> genreIds) {
        /*
         * TODO(Inba Thiyagarajan): Combine popular/discover endpoints to implement genre filtering.
         */
        return Collections.emptyList();
    }
}
