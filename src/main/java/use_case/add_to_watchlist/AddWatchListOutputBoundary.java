package use_case.add_to_watchlist;

/**
 * Output boundary for the Add to Watch List use case.
 */
public interface AddWatchListOutputBoundary {

    void prepareSuccessView(AddWatchListResponseModel responseModel);

    void prepareFailView(String errorMessage);
}

