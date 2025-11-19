package use_case.filter_movies;

import java.util.List;

/**
 * Request model for the Filter Movies use case.
 * <p>
 * Encapsulates the input data required by the interactor to filter movies,
 * specifically a list of genre IDs selected by the user.
 */
public class FilterMoviesRequestModel {

    private final List<Integer> genreIds;

    /**
     * Constructs a new {@code FilterMoviesRequestModel} with the specified genre IDs.
     *
     * @param genreIds the list of genre IDs to filter movies by
     */
    public FilterMoviesRequestModel(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    /**
     * Returns the list of genre IDs.
     *
     * @return a list of genre IDs
     */
    public List<Integer> getGenreIds() {
        return genreIds;
    }
}
