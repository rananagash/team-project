package interface_adapter.view_profile;

import interface_adapter.view_login_signup.ViewModel;

/**
 * View Model for the View Profile View.
 */
public class ViewProfileViewModel extends ViewModel<ViewProfileState> {
    public static final String TITLE_LABEL = "User Profile";

    public ViewProfileViewModel() {
        super("view profile");
        setState(new ViewProfileState());
    }
}