package use_case.edit_watchedmovie;

import java.time.LocalDateTime;

/**
 * Response model for editing a watched movie.
 */
public class EditWatchedMovieResponseModel {
    private final String userName;
    private final String movieId;
    private final String movieTitle;
    private final LocalDateTime watchedDate;
    private final Integer rating;
    private final String review;

    public EditWatchedMovieResponseModel(String userName, String movieId, String movieTitle,
                                         LocalDateTime watchedDate, Integer rating, String review) {
        this.userName = userName;
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.watchedDate = watchedDate;
        this.rating = rating;
        this.review = review;
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

    public LocalDateTime getWatchedDate() {
        return watchedDate;
    }

    public Integer getRating() {
        return rating;
    }

    public String getReview() {
        return review;
    }
}

