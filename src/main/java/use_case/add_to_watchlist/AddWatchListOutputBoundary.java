package use_case.add_to_watchlist;

/**
 * Output boundary for the Add to Watch List use case.
 */
public interface AddWatchListOutputBoundary {

    /**
     * Prepares the view for a successful add to watch list operation.
     * @param responseModel the response model with the result message.
     */
    void prepareSuccessView(AddWatchListResponseModel responseModel);

    /**
     * Prepares the failure view for the add to watch list operation.
     * @param errorMessage human-readable error message to be displayed.
     */
    void prepareFailView(String errorMessage);
}

