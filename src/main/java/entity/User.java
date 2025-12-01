package entity;

import java.util.*;

/**
 * Represents an application user.
 * A user has:
 * <ul>
 *     <li>A unique username</li>
 *     <li>A non-empty password</li>
 *     <li>One or more watchlists (a default watchlist is created automatically)</li>
 *     <li>A watch history</li>
 *     <li>A set of reviews</li>
 * </ul>
 */
public class User {

    private final String userName;
    private final String password;
    private final List<WatchList> watchLists = new ArrayList<>();
    private WatchHistory watchHistory;
    private final Map<String, Review> reviewsByMovieId = new HashMap<>();

    /**
     * Constructs a new {@code User} with a non-empty username and password.
     *
     * <p>A default watchlist belonging to this user is created automatically.
     *
     * @param userName the username of the user (must not be empty)
     * @param password the password of the user (must not be empty)
     * @throws IllegalArgumentException if either argument is empty
     */
    public User(String userName, String password) {
        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.userName = userName;
        this.password = password;

        // Create default watchlist
        this.watchLists.add(new WatchList(this));
    }

    /**
     * Returns the username of this user.
     *
     * @return the non-null username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Returns the password of this user.
     *
     * @return the non-null password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns all watchlists belonging to the user.
     * @return a list of watchlists
     */
    public List<WatchList> getWatchLists() {
        return watchLists;
    }

    /**
     * Removes all watchlists belonging to this user.
     */
    public void clearWatchLists() {
        this.watchLists.clear();
    }

    /**
     * Adds a watchlist to the user.
     *
     * @param watchList the watchlist to add (must belong to this user)
     * @throws NullPointerException if {@code watchList} is null
     * @throws IllegalArgumentException if the watchlist is associated with another user
     */
    public void addWatchList(WatchList watchList) {
        Objects.requireNonNull(watchList, "WatchList cannot be null");

        // check it is correct user
        if (!watchList.getUser().equals(this)) {
            throw new IllegalArgumentException("WatchList must be the same user");
        }

        this.watchLists.add(watchList);
    }

    /**
     * Returns the first watchlist whose name matches (case-insensitive).
     *
     * @param name the watchlist name to search for
     * @return an Optional containing the matching watchlist or empty if none found
     */
    public Optional<WatchList> getWatchListByName(String name) {
        return watchLists.stream()
                .filter(list -> list.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Finds a watchlist by its unique ID.
     *
     * @param watchListId the ID to search for
     * @return the matching watchlist, or {@code null} if none exists or ID is null
     */
    public WatchList getWatchListById(String watchListId) {
        if (watchListId == null) {
            return null;
        }

        for (WatchList watchList : watchLists) {
            if (watchListId.equals(watchList.getWatchListId())) {
                return watchList;
            }
        }
        return null;
    }

    /**
     * Returns the user's watch history.
     *
     * @return the watch history, or null if none has been set
     */
    public WatchHistory getWatchHistory() {
        return watchHistory;
    }

    /**
     * Sets the user's watch history.
     *
     * @param watchHistory the new watch history
     */
    public void setWatchHistory(WatchHistory watchHistory) {
        this.watchHistory = watchHistory;
    }

    /**
     * Adds or replaces the review for a given movie.
     *
     * @param review the review to add (must not be null)
     * @throws NullPointerException if {@code review} is null
     */
    public void addReview(Review review) {
        reviewsByMovieId.put(review.getMovie().getMovieId(), review);
    }

    /**
     * Returns an immutable copy of all reviews keyed by movie ID.
     *
     * @return an unmodifiable map of movie ID to review
     */
    public Map<String, Review> getReviewsByMovieId() {
        return Map.copyOf(reviewsByMovieId);
    }
    /**
     * Returns all watchlists belonging to this user.
     *
     * @return a collection of watchlists
     */
    public Collection<Object> getWatchlists() {
        return new ArrayList<>(watchLists);
    }

    /**
     * Returns all reviews written by this user.
     *
     * @return a collection of reviews
     */
    public Collection<Object> getReviews() {
        return new ArrayList<>(reviewsByMovieId.values());
    }

    /**
     * Returns all watched movies from the user's watch history.
     *
     * @return a collection of watched movies, or empty collection if no watch history
     */
    public Collection<Object> getWatchedMovies() {
        if (watchHistory == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(watchHistory.getMovies());
    }
}
