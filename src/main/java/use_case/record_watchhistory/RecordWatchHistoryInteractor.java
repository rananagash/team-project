package use_case.record_watchhistory;

import entity.Movie;
import entity.User;
import entity.WatchHistory;
import use_case.common.MovieGateway;
import use_case.common.UserDataAccessInterface;

import java.time.LocalDateTime;
import java.util.UUID;

public class RecordWatchHistoryInteractor implements RecordWatchHistoryInputBoundary {

    private final UserDataAccessInterface userDataAccessInterface;
    private final MovieGateway movieGateway;
    private final RecordWatchHistoryOutputBoundary presenter;

    public RecordWatchHistoryInteractor(UserDataAccessInterface userDataAccessInterface,
                                        MovieGateway movieGateway,
                                        RecordWatchHistoryOutputBoundary presenter) {
        this.userDataAccessInterface = userDataAccessInterface;
        this.movieGateway = movieGateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(RecordWatchHistoryRequestModel requestModel) {
        // Validate input
        if (requestModel.getUserName() == null || requestModel.getUserName().isBlank()) {
            presenter.prepareFailView("User name cannot be empty.");
            return;
        }

        if (requestModel.getMovieId() == null || requestModel.getMovieId().isBlank()) {
            presenter.prepareFailView("Movie ID cannot be empty.");
            return;
        }

        User user = userDataAccessInterface.getUser(requestModel.getUserName());
        if (user == null) {
            presenter.prepareFailView("User not found: " + requestModel.getUserName());
            return;
        }

        handleUser(user, requestModel);
    }

    private void handleUser(User user, RecordWatchHistoryRequestModel requestModel) {
        movieGateway.findById(requestModel.getMovieId()).ifPresentOrElse(
                movie -> recordMovie(user, movie, requestModel),
                () -> presenter.prepareFailView("Movie not found: " + requestModel.getMovieId())
        );
    }

    private void recordMovie(User user, Movie movie, RecordWatchHistoryRequestModel requestModel) {
        // Ensure user has a watch history, create one if not
        WatchHistory watchHistory = user.getWatchHistory();
        if (watchHistory == null) {
            watchHistory = new WatchHistory(UUID.randomUUID().toString(), user);
            user.setWatchHistory(watchHistory);
        }

        // Determine watched time: use provided time or current time if null
        LocalDateTime watchedAt = requestModel.getWatchedAt();
        if (watchedAt == null) {
            watchedAt = LocalDateTime.now();
        }

        // Validate watched time is not in the future
        if (watchedAt.isAfter(LocalDateTime.now())) {
            presenter.prepareFailView("Watched time cannot be in the future.");
            return;
        }

        // Note: We allow duplicate entries (same movie watched multiple times) since users
        // may watch the same movie multiple times, and we want to track each viewing session.
        // Each entry has its own watchedAt timestamp, allowing users to see their viewing history
        // chronologically.

        // Record the movie in watch history
        watchHistory.recordMovie(movie, watchedAt);
        userDataAccessInterface.save(user);

        presenter.prepareSuccessView(new RecordWatchHistoryResponseModel(
                user.getUserName(),
                movie.getMovieId(),
                movie.getTitle(),
                watchedAt
        ));
    }
}


