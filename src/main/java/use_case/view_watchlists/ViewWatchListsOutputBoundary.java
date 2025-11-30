package use_case.view_watchlists;

/**
 * Output boundary for the View Watch Lists use case.
 */
public interface ViewWatchListsOutputBoundary {
    /**
     * Prepares the view for a successful view watch lists operation.
     *
     * @param responseModel the response model
     */
    void prepareSuccessView(ViewWatchListsResponseModel responseModel);

    /**
     * Prepares the failure view for the view watch lists operation.
     *
     * @param errorMessage human-readable error message
     */
    void prepareFailureView(String errorMessage);
}
