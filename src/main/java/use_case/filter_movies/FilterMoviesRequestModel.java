package use_case.filter_movies;

import entity.Movie;

import java.util.List;

/**
 * Request model for the Filter Movies use case.
 * <p>
 * Encapsulates the input data required by the interactor to filter movies,
 * specifically a list of genre IDs selected by the user and the movies to filter.
 */
public class FilterMoviesRequestModel {

    private final List<Integer> genreIds;
    private final List<Movie> moviesToFilter;

    /**
     * Constructs a new {@code FilterMoviesRequestModel} with the specified genre IDs
     * and movies to filter.
     *
     * @param genreIds the list of genre IDs to filter movies by
     * @param moviesToFilter the list of movies to filter
     */
    public FilterMoviesRequestModel(List<Integer> genreIds, List<Movie> moviesToFilter) {
        this.genreIds = genreIds;
        this.moviesToFilter = moviesToFilter;
    }

    /**
     * Returns the list of genre IDs.
     *
     * @return a list of genre IDs
     */
    public List<Integer> getGenreIds() {
        return genreIds;
    }

    /**
     * Returns the list of movies to filter.
     *
     * @return a list of movies to filter
     */
    public List<Movie> getMoviesToFilter() {
        return moviesToFilter;
    }
}
