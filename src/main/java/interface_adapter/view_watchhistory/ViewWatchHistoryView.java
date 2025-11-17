package interface_adapter.view_watchhistory;

import entity.WatchedMovie;

import java.util.List;

/**
 * An interface representing a UI element that can display watch history.
 */
public interface ViewWatchHistoryView {

    /**
     * Displays the watch history for a user.
     * @param userName the username
     * @param watchedMovies the list of watched movies
     */
    void showWatchHistory(String userName, List<WatchedMovie> watchedMovies);

    /**
     * Displays an error message.
     * @param errorMessage the error message to display
     */
    void showError(String errorMessage);
}

