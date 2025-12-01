package interface_adapter.delete_watchedmovie;

import use_case.delete_watchedmovie.DeleteWatchedMovieOutputBoundary;
import use_case.delete_watchedmovie.DeleteWatchedMovieResponseModel;

/**
 * Presenter for deleting a watched movie.
 */
public class DeleteWatchedMoviePresenter implements DeleteWatchedMovieOutputBoundary {

    private DeleteWatchedMovieView view;

    public DeleteWatchedMoviePresenter(DeleteWatchedMovieView view) {
        this.view = view;
    }

    @Override
    public void prepareSuccessView(DeleteWatchedMovieResponseModel responseModel) {
        if (view != null) {
            view.onDeleteSuccess(responseModel);
        } else {
            System.out.println("Movie deleted successfully: " + responseModel.getMovieTitle());
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        if (view != null) {
            view.onDeleteError(errorMessage);
        } else {
            System.err.println("Error deleting movie: " + errorMessage);
        }
    }
}

