package use_case.delete_watchedmovie;

/**
 * Input boundary for the Delete Watched Movie use case.
 */
public interface DeleteWatchedMovieInputBoundary {
    /**
     * Deletes a movie from the user's watch history.
     *
     * @param requestModel the request containing username and movie ID
     */
    void execute(DeleteWatchedMovieRequestModel requestModel);
}

