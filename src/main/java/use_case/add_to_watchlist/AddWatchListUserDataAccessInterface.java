package use_case.add_to_watchlist;

import entity.User;

/**
 * Data access interface for the Add to Watchlist use case.
 */
public interface AddWatchListUserDataAccessInterface {

    /**
     * Retrieves a user by the username for adding to the watch list.
     * @param username the username to lookup
     * @return the User object, or null if not found
     */
    User getUser(String username);

    /**
     * Save user and associated data.
     * @param user the user to save.
     */
    void save(User user);
}
