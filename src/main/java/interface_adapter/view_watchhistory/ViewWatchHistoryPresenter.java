package interface_adapter.view_watchhistory;

import entity.WatchedMovie;
import use_case.view_watchhistory.ViewWatchHistoryOutputBoundary;
import use_case.view_watchhistory.ViewWatchHistoryResponseModel;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ViewWatchHistoryPresenter implements ViewWatchHistoryOutputBoundary {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void prepareSuccessView(ViewWatchHistoryResponseModel responseModel) {
        String userName = responseModel.getUserName();
        List<WatchedMovie> watchedMovies = responseModel.getWatchedMovies();

        if (watchedMovies.isEmpty()) {
            System.out.println("User " + userName + " has no watch history yet.");
            return;
        }

        System.out.println("=== Watch History for " + userName + " ===");
        System.out.println("Total movies watched: " + watchedMovies.size());
        System.out.println();

        for (int i = 0; i < watchedMovies.size(); i++) {
            WatchedMovie movie = watchedMovies.get(i);
            System.out.println((i + 1) + ". " + movie.getTitle());
            System.out.println("   Watched at: " + movie.getWatchedDate().format(DATE_FORMATTER));
            System.out.println("   Movie ID: " + movie.getMovieId());
            if (movie.getRating() > 0) {
                System.out.println("   Rating: " + movie.getRating());
            }
            System.out.println();
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        System.err.println("Error viewing watch history: " + errorMessage);
    }
}

