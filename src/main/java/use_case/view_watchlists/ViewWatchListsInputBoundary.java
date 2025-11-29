package use_case.view_watchlists;

/**
 * The input boundary for the View Watch Lists use case.
 */
public interface ViewWatchListsInputBoundary {
    /**
     * Executes the View Watch Lists use case with the given request model.
     *
     * @param requestModel the request model with the data to be added.
     */
    void execute(ViewWatchListsRequestModel requestModel);
}
