package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a user's watch history, tracking movies they have watched.
 *
 * <p>A WatchHistory contains:
 * <ul>
 *     <li>A unique history identifier</li>
 *     <li>The user who owns this history</li>
 *     <li>A chronological list of watched movies with timestamps</li>
 * </ul>
 *
 * <p>Movies can be added to the history either by creating a {@link WatchedMovie}
 * and adding it directly, or by using the convenience method {@link #recordMovie(Movie, LocalDateTime)}
 * which creates and adds a WatchedMovie in one step.
 *
 * <p>The list of movies is returned as an immutable copy to prevent external modification.
 *
 * @author Team Project 9
 */
public class WatchHistory {

    private final String watchHistoryId;
    private final User user;
    private final List<WatchedMovie> movies = new ArrayList<>();

    /**
     * Creates a new watch history for the specified user.
     *
     * @param watchHistoryId the unique history identifier (must not be null)
     * @param user the user who owns this history (must not be null)
     * @throws NullPointerException if {@code watchHistoryId} or {@code user} is null
     */
    public WatchHistory(String watchHistoryId, User user) {
        this.watchHistoryId = Objects.requireNonNull(watchHistoryId, "watchHistoryId");
        this.user = Objects.requireNonNull(user, "user");
    }

    /**
     * Returns the unique watch history identifier.
     *
     * @return the history ID
     */
    public String getWatchHistoryId() {
        return watchHistoryId;
    }

    /**
     * Returns the user who owns this watch history.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns an immutable copy of the watched movies list.
     *
     * @return an unmodifiable list of watched movies
     */
    public List<WatchedMovie> getMovies() {
        return List.copyOf(movies);
    }

    /**
     * Adds a watched movie to this history.
     *
     * @param watchedMovie the watched movie to add (must not be null)
     * @throws NullPointerException if {@code watchedMovie} is null
     */
    public void addWatchedMovie(WatchedMovie watchedMovie) {
        movies.add(Objects.requireNonNull(watchedMovie, "WatchedMovie cannot be null"));
    }

    /**
     * Records a movie as watched at the specified time.
     *
     * <p>This is a convenience method that creates a {@link WatchedMovie} from the
     * given movie and watched timestamp, adds it to the history, and returns it.
     *
     * @param movie the movie that was watched (must not be null)
     * @param watchedAt the date and time when the movie was watched (null defaults to current time)
     * @return the created WatchedMovie instance
     * @throws NullPointerException if {@code movie} is null
     */
    public WatchedMovie recordMovie(Movie movie, LocalDateTime watchedAt) {
        Objects.requireNonNull(movie, "Movie cannot be null");
        WatchedMovie watchedMovie = new WatchedMovie(movie, watchedAt);
        addWatchedMovie(watchedMovie);
        return watchedMovie;
    }
}
