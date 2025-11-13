package use_case.view_watchhistory;

public interface ViewWatchHistoryOutputBoundary {

    void prepareSuccessView(ViewWatchHistoryResponseModel responseModel);

    void prepareFailView(String errorMessage);
}

