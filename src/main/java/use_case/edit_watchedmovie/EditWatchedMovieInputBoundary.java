package use_case.edit_watchedmovie;

/**
 * Input boundary for the Edit Watched Movie use case.
 */
public interface EditWatchedMovieInputBoundary {
    /**
     * Updates a movie in the user's watch history.
     *
     * @param requestModel the request containing username, movie ID, and updated information
     */
    void execute(EditWatchedMovieRequestModel requestModel);
}

