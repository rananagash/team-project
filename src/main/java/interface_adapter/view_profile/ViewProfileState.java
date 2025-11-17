package interface_adapter.view_profile;

import entity.User;
import use_case.view_profile.ProfileStats;

/**
 * State for the View Profile View Model.
 */
public class ViewProfileState {
    private User user;
    private ProfileStats profileStats;
    private String error;

    public User getUser() { return user; }
    public ProfileStats getProfileStats() { return profileStats; }
    public String getError() { return error; }

    public void setUser(User user) { this.user = user; }
    public void setProfileStats(ProfileStats profileStats) { this.profileStats = profileStats; }
    public void setError(String error) { this.error = error; }
}