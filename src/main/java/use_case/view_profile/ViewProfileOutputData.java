package use_case.view_profile;

import entity.User;

/**
 * Output data for the View Profile use case.
 */
public class ViewProfileOutputData {
    private final User user;
    private final ProfileStats profileStats;

    public ViewProfileOutputData(User user, ProfileStats profileStats) {
        this.user = user;
        this.profileStats = profileStats;
    }

    public User getUser() { return user; }
    public ProfileStats getProfileStats() { return profileStats; }
}