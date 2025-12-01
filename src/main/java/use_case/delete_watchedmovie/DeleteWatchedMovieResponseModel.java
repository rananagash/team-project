package use_case.delete_watchedmovie;

/**
 * Response model for deleting a watched movie.
 */
public class DeleteWatchedMovieResponseModel {
    private final String userName;
    private final String movieId;
    private final String movieTitle;

    public DeleteWatchedMovieResponseModel(String userName, String movieId, String movieTitle) {
        this.userName = userName;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
    }

    public String getUserName() {
        return userName;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }
}

