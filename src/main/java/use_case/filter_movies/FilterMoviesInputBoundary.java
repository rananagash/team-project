package use_case.filter_movies;

/**
 * Input boundary for the Filter Movies use case.
 * <p>
 * Defines the entry point for the interactor. Implementations of this
 * interface handle requests to filter movies based on the provided
 * criteria in {@link FilterMoviesRequestModel}.
 */
public interface FilterMoviesInputBoundary {

    /**
     * Executes the filter movies use case with the given request model.
     *
     * @param requestModel the request model containing filter criteria
     */
    void execute(FilterMoviesRequestModel requestModel);
}
