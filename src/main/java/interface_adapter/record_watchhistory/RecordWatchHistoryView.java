package interface_adapter.record_watchhistory;

/**
 * An interface representing a UI element that can display the result of recording watch history.
 */
public interface RecordWatchHistoryView {

    /**
     * Displays a success message when watch history is successfully recorded.
     * @param userName the username
     * @param movieTitle the title of the movie
     * @param movieId the ID of the movie
     * @param watchedAt the date and time when the movie was watched
     */
    void showSuccess(String userName, String movieTitle, String movieId, String watchedAt);

    /**
     * Displays an error message when recording watch history fails.
     * @param errorMessage the error message to display
     */
    void showError(String errorMessage);
}

