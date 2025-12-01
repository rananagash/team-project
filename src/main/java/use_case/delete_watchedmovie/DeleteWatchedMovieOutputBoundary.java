package use_case.delete_watchedmovie;

/**
 * Output boundary for the Delete Watched Movie use case.
 */
public interface DeleteWatchedMovieOutputBoundary {
    /**
     * Prepares the success view after a movie is deleted.
     *
     * @param responseModel the response model containing deletion result
     */
    void prepareSuccessView(DeleteWatchedMovieResponseModel responseModel);

    /**
     * Prepares the fail view when deletion fails.
     *
     * @param errorMessage the error message
     */
    void prepareFailView(String errorMessage);
}

