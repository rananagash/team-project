package interface_adapter.record_watchhistory;

import use_case.record_watchhistory.RecordWatchHistoryOutputBoundary;
import use_case.record_watchhistory.RecordWatchHistoryResponseModel;

import java.time.format.DateTimeFormatter;

/**
 * Presenter for the Record Watch History use case.
 *
 * <p>This class receives data from the interactor and delegates UI updates to a {@link RecordWatchHistoryView}.
 * If no view is set, it falls back to console output for testing purposes.
 */
public class RecordWatchHistoryPresenter implements RecordWatchHistoryOutputBoundary {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RecordWatchHistoryView view;

    /**
     * Constructs a presenter with a view interface that can display the results.
     * @param view the UI interface used to display the results (can be null for console mode)
     */
    public RecordWatchHistoryPresenter(RecordWatchHistoryView view) {
        this.view = view;
    }

    @Override
    public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
        String userName = responseModel.getUserName();
        String movieId = responseModel.getMovieId();
        String movieTitle = responseModel.getMovieTitle();
        String watchedAt = responseModel.getWatchedAt().format(DATE_FORMATTER);

        if (view != null) {
            view.showSuccess(userName, movieTitle, movieId, watchedAt);
        } else {
            // Fallback to console output if view is not set
            System.out.println("Successfully recorded watch history:");
            System.out.println("  User: " + userName);
            System.out.println("  Movie: " + movieTitle + " (ID: " + movieId + ")");
            System.out.println("  Watched at: " + watchedAt);
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        if (view != null) {
            view.showError(errorMessage);
        } else {
            // Fallback to console output if view is not set
            System.err.println("Error recording watch history: " + errorMessage);
        }
    }
}

