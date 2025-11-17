package use_case.view_profile;

import entity.User;

/**
 * Data access interface for the View Profile use case.
 */
public interface ViewProfileUserDataAccessInterface {

    /**
     * Retrieves a user by username for profile viewing.
     * @param username the username to look up
     * @return the User object, or null if not found
     */
    User getUser(String username);

    /**
     * Gets profile statistics for a user.
     * @param username the username to get stats for
     * @return ProfileStats object containing user statistics
     */
    ProfileStats getUserStats(String username);
}