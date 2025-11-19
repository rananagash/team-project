package interface_adapter.filter_movies;

import use_case.filter_movies.FilterMoviesInputBoundary;
import use_case.filter_movies.FilterMoviesRequestModel;

import java.util.List;

/**
 * Controller for the Filter Movies use case.
 * <p>
 * Accepts input from the view (e.g., selected genre IDs) and passes it
 * to the interactor. Acts as the entry point for the use case in the
 * interface adapter layer.
 */
public class FilterMoviesController {

    private final FilterMoviesInputBoundary interactor;

    /**
     * Constructs a new {@code FilterMoviesController} with the specified interactor.
     *
     * @param interactor the input boundary to execute the use case
     */
    public FilterMoviesController(FilterMoviesInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Filters movies by the provided genre IDs.
     * <p>
     * Wraps the genre IDs in a request model and passes it to the interactor.
     *
     * @param genreIds the list of genre IDs to filter movies by
     */
    public void filterByGenres(List<Integer> genreIds) {
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);
        interactor.execute(requestModel);
    }
}
