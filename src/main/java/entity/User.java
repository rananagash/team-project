package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class User {

    private final String userName;
    private final String password;
    private final List<WatchList> watchLists = new ArrayList<>();
    private WatchHistory watchHistory;
    private final Map<String, Review> reviewsByMovieId = new HashMap<>();

    /**
     * Creates a new user with the given non-empty username and non-empty password.
     */
    public User(String userName, String password) {
        if ("".equals(userName)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public List<WatchList> getWatchLists() {
        return List.copyOf(watchLists);
    }

    public void addWatchList(WatchList watchList) {
        watchLists.add(Objects.requireNonNull(watchList));
    }

    public Optional<WatchList> getWatchListByName(String name) {
        return watchLists.stream()
                .filter(list -> list.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public WatchHistory getWatchHistory() {
        return watchHistory;
    }

    public void setWatchHistory(WatchHistory watchHistory) {
        this.watchHistory = watchHistory;
    }

    public void addReview(Review review) {
        reviewsByMovieId.put(review.getMovie().getMovieId(), review);
    }

    public Map<String, Review> getReviewsByMovieId() {
        return Map.copyOf(reviewsByMovieId);
    }
}
