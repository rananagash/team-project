package use_case.filter_movies;

import entity.Movie;
import java.util.List;

/**
 * Response model for the Filter Movies use case. This class contains the
 * filtered list of movies along with the genre IDs and their corresponding
 * human-readable genre names that were requested in the filter operation.
 */
public class FilterMoviesResponseModel {

    private final List<Integer> requestedGenres;
    private final List<String> requestedGenreNames;
    private final List<Movie> movies;

    /**
     * Constructs a new {@code FilterMoviesResponseModel}.
     *
     * @param requestedGenres      the list of genre IDs that were used in the filter request
     * @param requestedGenreNames  the human-readable names corresponding to the requested genre IDs
     * @param movies               the list of movies that matched the filter criteria
     */
    public FilterMoviesResponseModel(List<Integer> requestedGenres,
                                     List<String> requestedGenreNames,
                                     List<Movie> movies) {
        this.requestedGenres = requestedGenres;
        this.requestedGenreNames = requestedGenreNames;
        this.movies = movies;
    }

    /**
     * Returns the list of requested genre IDs.
     *
     * @return a list of integer genre IDs
     */
    public List<Integer> getRequestedGenres() {
        return requestedGenres;
    }

    /**
     * Returns the human-readable genre names that correspond to the
     * requested genre IDs.
     *
     * @return a list of genre names
     */
    public List<String> getRequestedGenreNames() {
        return requestedGenreNames;
    }

    /**
     * Returns the list of movies that satisfied the filter criteria.
     *
     * @return a list of movies
     */
    public List<Movie> getMovies() {
        return movies;
    }
}
