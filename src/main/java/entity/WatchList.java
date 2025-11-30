package entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a user's Watch List containing a collection of movies.
 *
 * <p>A WatchList has:
 * <ul>
 *     <li>A unique identifier</li>
 *     <li>An owning {@link User}</li>
 *     <li>A human-readable name</li>
 *     <li>A creation timestamp</li>
 *     <li>A modifiable list of {@link Movie} objects</li>
 * </ul>
 *
 * <p>The {@code movies} list prevents duplicates. Callers should rely on
 * {@link #addMovie(Movie)} to enforce this rule.
 */
public class WatchList {

    private final String watchListId;
    private final User user;
    private final String name;
    private final LocalDateTime dateCreated;
    private final List<Movie> movies = new ArrayList<>();

    /**
     * Creates a new Watch List with a custom name for the given user.
     *
     * @param user the owner of the watch list
     * @param name the name of the watch list
     * @throws NullPointerException if {@code user} or {@code name} is null
     */
    public WatchList(User user,
                     String name) {
        this.watchListId = UUID.randomUUID().toString();
        this.user = Objects.requireNonNull(user, "user");
        this.name = Objects.requireNonNull(name, "name");
        this.dateCreated = LocalDateTime.now();
    }

    /**
     * Creates a new Watch List with a default name for the given user.
     *
     * <p>The default name is "[username]'s Watch List".
     *
     * @param user the owner of the watch list
     * @throws NullPointerException if {@code user} is null
     */
    public WatchList(User user) {
        this.watchListId = UUID.randomUUID().toString();
        this.user = Objects.requireNonNull(user, "user");
        this.name = user.getUserName() + "'s Watch List";
        this.dateCreated = LocalDateTime.now();
    }

    /**
     * Returns the unique identifier of this Watch List.
     * @return the watch list ID
     */
    public String getWatchListId() {
        return watchListId;
    }

    /**
     * Returns the owner of this Watch List.
     * @return the {@link User} who owns this list
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the name of this Watch List.
     * @return the watch list name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the creation timestamp of this Watch List.
     * @return the date and time this Watch List was created
     */
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    /**
     * Returns an immutable view of the movies in this Watch List.
     *
     * @return an unmodifiable list of movies
     */
    public List<Movie> getMovies() {
        return List.copyOf(movies);
    }

    /**
     * Returns a movie in the watchlist by the given movieId, or {@code null}
     * if the movie is not in the watchlist.
     *
     * @param movieId the movieId to search
     * @return the movie or {@code null} if not found
     */
    public Movie getMovieById(String movieId) {
        if (movieId == null) {
            return null;
        }

        for (Movie movie : movies) {
            if (movieId.equals(movie.getMovieId())) {
                return movie;
            }
        }
        return null;
    }

    /**
     * Attempts to add a movie to the Watch List.
     *
     * <p>This method prevents duplicates: if the movie is already in the list, no change occurs.</p>
     *
     * @param movie the movie to add (must not be null)
     * @return {@code true} if the movie was added; {@code false} if it was already present
     * @throws NullPointerException if {@code movie} is null
     */
    public boolean addMovie(Movie movie) {
        if (movies.contains(movie)) {
            return false;
        }
        movies.add(Objects.requireNonNull(movie));
        return true;
    }

    /**
     * Removes a movie from the Watch List.
     *
     * <p>If the movie is not present, no action is taken.</p>
     *
     * @param movie the movie to remove
     */
    public void removeMovie(Movie movie) {
        movies.remove(movie);
    }

    /**
     * Return the name of this Watch List.
     *
     * @return the name of this Watch List
     */
    @Override
    public String toString() {
        return this.name;
    }
}
