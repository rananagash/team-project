package use_case.add_to_watchlist;

public interface AddWatchListOutputBoundary {

    void prepareSuccessView(AddWatchListResponseModel responseModel);

    void prepareFailView(String errorMessage);
}

