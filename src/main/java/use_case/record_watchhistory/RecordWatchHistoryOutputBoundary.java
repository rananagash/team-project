package use_case.record_watchhistory;

public interface RecordWatchHistoryOutputBoundary {

    void prepareSuccessView(RecordWatchHistoryResponseModel responseModel);

    void prepareFailView(String errorMessage);
}


