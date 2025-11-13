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
}
