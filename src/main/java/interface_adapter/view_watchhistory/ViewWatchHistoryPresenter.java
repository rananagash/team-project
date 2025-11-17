package interface_adapter.view_watchhistory;

import entity.WatchedMovie;
import use_case.view_watchhistory.ViewWatchHistoryOutputBoundary;
import use_case.view_watchhistory.ViewWatchHistoryResponseModel;

import java.util.List;

public class ViewWatchHistoryPresenter implements ViewWatchHistoryOutputBoundary {

    private ViewWatchHistoryView view;

    public ViewWatchHistoryPresenter(ViewWatchHistoryView view) {
        this.view = view;
    }

    @Override
    public void prepareSuccessView(ViewWatchHistoryResponseModel responseModel) {
        String userName = responseModel.getUserName();
        List<WatchedMovie> watchedMovies = responseModel.getWatchedMovies();

        if (view != null) {
            view.showWatchHistory(userName, watchedMovies);
        } else {
            // Fallback to console output if view is not set
            System.out.println("=== Watch History for " + userName + " ===");
            System.out.println("Total movies watched: " + watchedMovies.size());
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        if (view != null) {
            view.showError(errorMessage);
        } else {
            // Fallback to console output if view is not set
            System.err.println("Error viewing watch history: " + errorMessage);
        }
    }
}

