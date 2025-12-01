package interface_adapter.edit_watchedmovie;

import use_case.edit_watchedmovie.EditWatchedMovieOutputBoundary;
import use_case.edit_watchedmovie.EditWatchedMovieResponseModel;

/**
 * Presenter for editing a watched movie.
 */
public class EditWatchedMoviePresenter implements EditWatchedMovieOutputBoundary {

    private EditWatchedMovieView view;

    public EditWatchedMoviePresenter(EditWatchedMovieView view) {
        this.view = view;
    }

    @Override
    public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
        if (view != null) {
            view.onEditSuccess(responseModel);
        } else {
            System.out.println("Movie updated successfully: " + responseModel.getMovieTitle());
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        if (view != null) {
            view.onEditError(errorMessage);
        } else {
            System.err.println("Error updating movie: " + errorMessage);
        }
    }
}

