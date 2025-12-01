package use_case.delete_watchedmovie;

import entity.User;
import use_case.common.UserDataAccessInterface;

/**
 * Interactor for deleting a movie from watch history.
 */
public class DeleteWatchedMovieInteractor implements DeleteWatchedMovieInputBoundary {

    private final UserDataAccessInterface userDataAccessInterface;
    private final DeleteWatchedMovieOutputBoundary presenter;

    public DeleteWatchedMovieInteractor(UserDataAccessInterface userDataAccessInterface,
                                        DeleteWatchedMovieOutputBoundary presenter) {
        this.userDataAccessInterface = userDataAccessInterface;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteWatchedMovieRequestModel requestModel) {
        User user = userDataAccessInterface.getUser(requestModel.getUserName());

        if (user == null) {
            presenter.prepareFailView("User not found: " + requestModel.getUserName());
            return;
        }

        if (user.getWatchHistory() == null) {
            presenter.prepareFailView("User has no watch history.");
            return;
        }

        // Find the movie to get its title before deletion
        String movieTitle = "Unknown Movie";
        var watchedMovies = user.getWatchHistory().getMovies();
        for (var watchedMovie : watchedMovies) {
            if (requestModel.getMovieId().equals(watchedMovie.getMovieId())) {
                movieTitle = watchedMovie.getTitle();
                break;
            }
        }

        // Remove the movie from watch history
        boolean removed = user.getWatchHistory().removeMovieByMovieId(requestModel.getMovieId());

        if (!removed) {
            presenter.prepareFailView("Movie not found in watch history: " + requestModel.getMovieId());
            return;
        }

        // Save the updated user
        userDataAccessInterface.save(user);

        presenter.prepareSuccessView(
                new DeleteWatchedMovieResponseModel(
                        requestModel.getUserName(),
                        requestModel.getMovieId(),
                        movieTitle
                )
        );
    }
}

