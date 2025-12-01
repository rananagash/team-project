package interface_adapter.edit_watchedmovie;

import use_case.edit_watchedmovie.EditWatchedMovieInputBoundary;
import use_case.edit_watchedmovie.EditWatchedMovieRequestModel;

import java.time.LocalDateTime;

/**
 * Controller for editing a watched movie in watch history.
 */
public class EditWatchedMovieController {
    private final EditWatchedMovieInputBoundary interactor;

    public EditWatchedMovieController(EditWatchedMovieInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void editMovie(String userName, String movieId,
                          LocalDateTime watchedDate,
                          Integer rating, String review) {
        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                userName, movieId, watchedDate, rating, review
        );
        interactor.execute(requestModel);
    }
}

