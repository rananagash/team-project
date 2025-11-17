package interface_adapter.record_watchhistory;

import use_case.record_watchhistory.RecordWatchHistoryInputBoundary;
import use_case.record_watchhistory.RecordWatchHistoryRequestModel;

import java.time.LocalDateTime;

public class RecordWatchHistoryController {

    private final RecordWatchHistoryInputBoundary interactor;

    public RecordWatchHistoryController(RecordWatchHistoryInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void recordMovie(String userName, String movieId, LocalDateTime watchedAt) {
        RecordWatchHistoryRequestModel requestModel = new RecordWatchHistoryRequestModel(
                userName, movieId, watchedAt);
        interactor.execute(requestModel);
    }

    public void recordMovie(String userName, String movieId) {
        // If watchedAt is not provided, use current time
        recordMovie(userName, movieId, null);
    }
}

