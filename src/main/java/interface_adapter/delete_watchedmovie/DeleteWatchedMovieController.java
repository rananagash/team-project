package interface_adapter.delete_watchedmovie;

import use_case.delete_watchedmovie.DeleteWatchedMovieInputBoundary;
import use_case.delete_watchedmovie.DeleteWatchedMovieRequestModel;

/**
 * Controller for deleting a watched movie from watch history.
 */
public class DeleteWatchedMovieController {
    private final DeleteWatchedMovieInputBoundary interactor;

    public DeleteWatchedMovieController(DeleteWatchedMovieInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void deleteMovie(String userName, String movieId) {
        DeleteWatchedMovieRequestModel requestModel = new DeleteWatchedMovieRequestModel(userName, movieId);
        interactor.execute(requestModel);
    }
}

