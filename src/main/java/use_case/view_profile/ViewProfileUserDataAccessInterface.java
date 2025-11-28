package use_case.view_profile;

import use_case.common.UserDataAccessInterface;

/**
 * Data access interface for the View Profile use case.
 */
public interface ViewProfileUserDataAccessInterface extends UserDataAccessInterface {

    /**
     * Gets profile statistics for a user.
     * @param username the username to get stats for
     * @return ProfileStats object containing user statistics
     */
    ProfileStats getUserStats(String username);
}