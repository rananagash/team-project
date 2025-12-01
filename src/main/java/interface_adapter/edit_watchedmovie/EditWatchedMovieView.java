package interface_adapter.edit_watchedmovie;

import use_case.edit_watchedmovie.EditWatchedMovieResponseModel;

/**
 * View interface for edit watched movie operations.
 */
public interface EditWatchedMovieView {
    /**
     * Called when a movie is successfully updated.
     *
     * @param responseModel the response model containing update details
     */
    void onEditSuccess(EditWatchedMovieResponseModel responseModel);

    /**
     * Called when update fails.
     *
     * @param errorMessage the error message
     */
    void onEditError(String errorMessage);
}

