package use_case.compare_watchlists;

public interface CompareWatchListOutputBoundary {

    void prepareSuccessView(CompareWatchListResponseModel responseModel);

    void prepareFailView(String errorMessage);
}

