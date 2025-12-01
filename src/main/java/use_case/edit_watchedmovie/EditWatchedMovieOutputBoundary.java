package use_case.edit_watchedmovie;

/**
 * Output boundary for the Edit Watched Movie use case.
 */
public interface EditWatchedMovieOutputBoundary {
    /**
     * Prepares the success view after a movie is updated.
     *
     * @param responseModel the response model containing update result
     */
    void prepareSuccessView(EditWatchedMovieResponseModel responseModel);

    /**
     * Prepares the fail view when update fails.
     *
     * @param errorMessage the error message
     */
    void prepareFailView(String errorMessage);
}

