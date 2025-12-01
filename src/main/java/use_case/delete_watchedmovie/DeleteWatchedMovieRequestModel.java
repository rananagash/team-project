package use_case.delete_watchedmovie;

/**
 * Request model for deleting a watched movie.
 */
public class DeleteWatchedMovieRequestModel {
    private final String userName;
    private final String movieId;

    public DeleteWatchedMovieRequestModel(String userName, String movieId) {
        this.userName = userName;
        this.movieId = movieId;
    }

    public String getUserName() {
        return userName;
    }

    public String getMovieId() {
        return movieId;
    }
}

