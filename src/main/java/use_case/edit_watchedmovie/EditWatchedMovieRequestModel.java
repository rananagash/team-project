package use_case.edit_watchedmovie;

import java.time.LocalDateTime;

/**
 * Request model for editing a watched movie.
 */
public class EditWatchedMovieRequestModel {
    private final String userName;
    private final String movieId;
    private final LocalDateTime watchedDate;
    private final Integer rating; // 1-5, null if not set
    private final String review; // comment/review text, null if not set

    public EditWatchedMovieRequestModel(String userName, String movieId,
                                        LocalDateTime watchedDate,
                                        Integer rating, String review) {
        this.userName = userName;
        this.movieId = movieId;
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

