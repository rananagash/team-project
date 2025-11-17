package interface_adapter.record_watchhistory;

import use_case.record_watchhistory.RecordWatchHistoryOutputBoundary;
import use_case.record_watchhistory.RecordWatchHistoryResponseModel;

import java.time.format.DateTimeFormatter;

public class RecordWatchHistoryPresenter implements RecordWatchHistoryOutputBoundary {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
        String userName = responseModel.getUserName();
        String movieId = responseModel.getMovieId();
        String movieTitle = responseModel.getMovieTitle();
        String watchedAt = responseModel.getWatchedAt().format(DATE_FORMATTER);

        System.out.println("Successfully recorded watch history:");
        System.out.println("  User: " + userName);
        System.out.println("  Movie: " + movieTitle + " (ID: " + movieId + ")");
        System.out.println("  Watched at: " + watchedAt);
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.err.println("Error recording watch history: " + errorMessage);
    }
}

