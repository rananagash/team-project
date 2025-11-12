package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Review {

    private final String reviewId;
    private final User user;
    private final Movie movie;
    private final int rating; // 1-5 stars?
    private final String comment;
    private final LocalDateTime createdAt;

    public Review(String reviewId, User user, Movie movie, int rating, String comment, LocalDateTime createdAt) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating should be between 1 and 5.");
        }
        this.reviewId = Objects.requireNonNull(reviewId, "reviewId");
        this.user = Objects.requireNonNull(user, "user");
        this.movie = Objects.requireNonNull(movie, "movie");
        this.rating = rating;
        this.comment = comment == null ? "" : comment;
        this.createdAt = createdAt == null ? LocalDateTime.now() : createdAt;
    }

    public String getReviewId() {
        return reviewId;
    }

    public User getUser() {
        return user;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
