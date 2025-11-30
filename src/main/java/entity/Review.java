package entity;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a user's review of a movie.
 *
 * <p>A Review contains:
 * <ul>
 *     <li>A unique review identifier</li>
 *     <li>The user who wrote the review</li>
 *     <li>The movie being reviewed</li>
 *     <li>A numeric rating (1-5 stars)</li>
 *     <li>An optional text comment</li>
 *     <li>A timestamp indicating when the review was created</li>
 * </ul>
 *
 * <p>All fields are immutable after construction. If a null comment is provided,
 * it is stored as an empty string. If a null creation timestamp is provided,
 * it defaults to the current time.
 *
 * @author Team Project 9
 */
public class Review {

    private final String reviewId;
    private final User user;
    private final Movie movie;
    private final int rating; // 1-5 stars
    private final String comment;
    private final LocalDateTime createdAt;

    /**
     * Creates a new review with the specified properties.
     *
     * @param reviewId the unique review identifier (must not be null)
     * @param user the user who wrote the review (must not be null)
     * @param movie the movie being reviewed (must not be null)
     * @param rating the rating, must be between 1 and 5 inclusive
     * @param comment the review comment (null is treated as empty string)
     * @param createdAt the creation timestamp (null defaults to current time)
     * @throws NullPointerException if {@code reviewId}, {@code user}, or {@code movie} is null
     * @throws IllegalArgumentException if {@code rating} is not between 1 and 5
     */
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

    /**
     * Returns the unique review identifier.
     *
     * @return the review ID
     */
    public String getReviewId() {
        return reviewId;
    }

    /**
     * Returns the user who wrote this review.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the movie being reviewed.
     *
     * @return the movie
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Returns the rating (1-5 stars).
     *
     * @return the rating
     */
    public int getRating() {
        return rating;
    }

    /**
     * Returns the review comment.
     *
     * @return the comment (never null, may be empty string)
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns the creation timestamp.
     *
     * @return the creation date and time
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
