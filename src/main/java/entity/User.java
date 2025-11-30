package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents a user in the MovieNight application.
 *
 * <p>A User has:
 * <ul>
 *     <li>A unique username and password for authentication</li>
 *     <li>One or more watch lists for organizing movies to watch</li>
 *     <li>An optional watch history tracking viewed movies</li>
 *     <li>A collection of movie reviews indexed by movie ID</li>
 * </ul>
 *
 * <p>Upon creation, a default watch list is automatically created with the name
 * "[username]'s Watch List". Additional watch lists can be added via {@link #addWatchList(WatchList)}.
 *
 * <p>All collections returned by getters are either immutable copies or unmodifiable views
 * to prevent external modification.
 *
 * @author Team Project 9
 */
public class User {

    private final String userName;
    private final String password;
    private final List<WatchList> watchLists = new ArrayList<>();
    private WatchHistory watchHistory;
    private final Map<String, Review> reviewsByMovieId = new HashMap<>();

    /**
     * Creates a new user with the given non-empty username and non-empty password.
     *
     * <p>A default watch list is automatically created with the name "[username]'s Watch List".
     *
     * @param userName the username (must not be null or empty)
     * @param password the password (must not be null or empty)
     * @throws NullPointerException if username or password is null
     * @throws IllegalArgumentException if username or password is empty
     */
    public User(String userName, String password) {
        if (userName == null) {
            throw new NullPointerException("Username cannot be null");
        }
        if (password == null) {
            throw new NullPointerException("Password cannot be null");
        }
        if (userName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.userName = userName;
        this.password = password;

        // initialize default watchlist
        WatchList defaultWL = new WatchList(this);
        this.watchLists.add(defaultWL);
    }

    /**
     * Returns the username.
     *
     * @return the username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the list of watch lists for this user.
     *
     * <p>Note: This returns a direct reference to the internal list. Modifications
     * to the returned list will affect the user's watch lists. For safety, consider
     * using {@link #addWatchList(WatchList)} instead of modifying the list directly.
     *
     * @return the list of watch lists (mutable)
     */
    public List<WatchList> getWatchLists() {
        return watchLists;
    }

    public void clearWatchLists() {
        this.watchLists.clear();
    }

    public void addWatchList(WatchList watchList) {
        Objects.requireNonNull(watchList, "WatchList cannot be null");

        // check it is correct user
        if (!watchList.getUser().equals(this)) {
            throw new IllegalArgumentException("WatchList must be the same user");
        }

        this.watchLists.add(watchList);
    }

    /**
     * Finds a watch list by name (case-insensitive).
     *
     * @param name the name to search for
     * @return an Optional containing the watch list if found, empty otherwise
     */
    public Optional<WatchList> getWatchListByName(String name) {
        return watchLists.stream()
                .filter(list -> list.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Returns the watch history for this user.
     *
     * @return the watch history, or null if not set
     */
    public WatchHistory getWatchHistory() {
        return watchHistory;
    }

    /**
     * Sets the watch history for this user.
     *
     * @param watchHistory the watch history to set (may be null)
     */
    public void setWatchHistory(WatchHistory watchHistory) {
        this.watchHistory = watchHistory;
    }

    /**
     * Adds a review to this user's collection.
     *
     * <p>If a review for the same movie already exists, it will be replaced.
     *
     * @param review the review to add (must not be null)
     * @throws NullPointerException if {@code review} is null
     */
    public void addReview(Review review) {
        Objects.requireNonNull(review, "Review cannot be null");
        reviewsByMovieId.put(review.getMovie().getMovieId(), review);
    }

    /**
     * Returns an immutable copy of the reviews indexed by movie ID.
     *
     * @return an unmodifiable map of movie ID to review
     */
    public Map<String, Review> getReviewsByMovieId() {
        return Map.copyOf(reviewsByMovieId);
    }
}
