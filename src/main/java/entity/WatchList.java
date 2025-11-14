package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WatchList {

    private final String watchListId;
    private final User user;
    private final String name;
    private final LocalDateTime dateCreated;
    private final List<Movie> movies = new ArrayList<>();

    public WatchList(String watchListId,
                     User user,
                     String name,
                     LocalDateTime dateCreated) {
        this.watchListId = Objects.requireNonNull(watchListId, "watchListId");
        this.user = Objects.requireNonNull(user, "user");
        this.name = Objects.requireNonNull(name, "name");
        this.dateCreated = Objects.requireNonNull(dateCreated, "dateCreated");
    }

    public String getWatchListId() {
        return watchListId;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public List<Movie> getMovies() {
        return List.copyOf(movies);
    }

    public void addMovie(Movie movie) {
        if (!movies.contains(movie)) {
            movies.add(Objects.requireNonNull(movie));
        }
    }

    public void removeMovie(Movie movie) {
        movies.remove(movie);
    }
}
