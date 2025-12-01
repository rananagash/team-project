package interface_adapter.delete_watchedmovie;

import use_case.delete_watchedmovie.DeleteWatchedMovieResponseModel;

/**
 * View interface for delete watched movie operations.
 */
public interface DeleteWatchedMovieView {
    /**
     * Called when a movie is successfully deleted.
     *
     * @param responseModel the response model containing deletion details
     */
    void onDeleteSuccess(DeleteWatchedMovieResponseModel responseModel);

    /**
     * Called when deletion fails.
     *
     * @param errorMessage the error message
     */
    void onDeleteError(String errorMessage);
}

