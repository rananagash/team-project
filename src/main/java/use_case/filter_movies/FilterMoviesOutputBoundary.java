package use_case.filter_movies;

/**
 * Output boundary for the Filter Movies use case.
 * <p>
 * Defines the methods that the interactor uses to present the results
 * of the filter operation to the interface adapter layer (presenter/view model).
 */
public interface FilterMoviesOutputBoundary {

    /**
     * Prepares the view model for a successful filter operation.
     *
     * @param responseModel the response model containing filtered movies
     *                      and requested genre names
     */
    void prepareSuccessView(FilterMoviesResponseModel responseModel);

    /**
     * Prepares the view model for a failed filter operation.
     *
     * @param errorMessage the error message to display
     */
    void prepareFailView(String errorMessage);
}
