package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WatchHistory {

    private final String watchHistoryId;
    private final User user;
    private final List<WatchedMovie> movies = new ArrayList<>();

    public WatchHistory(String watchHistoryId, User user) {
        this.watchHistoryId = Objects.requireNonNull(watchHistoryId, "watchHistoryId");
        this.user = Objects.requireNonNull(user, "user");
    }

    public String getWatchHistoryId() {
        return watchHistoryId;
    }

    public User getUser() {
        return user;
    }

    public List<WatchedMovie> getMovies() {
        return List.copyOf(movies);
    }

    public void addWatchedMovie(WatchedMovie watchedMovie) {
        movies.add(Objects.requireNonNull(watchedMovie));
    }

    public WatchedMovie recordMovie(Movie movie, LocalDateTime watchedAt) {
        WatchedMovie watchedMovie = new WatchedMovie(movie, watchedAt);
        addWatchedMovie(watchedMovie);
        return watchedMovie;
    }

    /**
     * Removes a watched movie from the history by movie ID.
     * If multiple entries exist for the same movie, removes the first one found.
     *
     * @param movieId the ID of the movie to remove
     * @return true if a movie was removed, false if not found
     */
    public boolean removeMovieByMovieId(String movieId) {
        if (movieId == null) {
            return false;
        }
        return movies.removeIf(movie -> movieId.equals(movie.getMovieId()));
    }

    /**
     * Removes a specific watched movie from the history.
     *
     * @param watchedMovie the movie to remove
     * @return true if the movie was removed, false if not found
     */
    public boolean removeMovie(WatchedMovie watchedMovie) {
        if (watchedMovie == null) {
            return false;
        }
        return movies.remove(watchedMovie);
    }
}
