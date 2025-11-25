package use_case.logout;

/**
 * The Logout Interactor.
 */
public class LogoutInteractor implements LogoutInputBoundary {
    private final LogoutUserDataAccessInterface userDataAccessObject;
    private final LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutUserDataAccessInterface userDataAccessInterface,
                            LogoutOutputBoundary logoutOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.logoutPresenter = logoutOutputBoundary;
    }

    @Override
    public void execute() {
        String currentUsername = userDataAccessObject.getCurrentUsername();

//        if (currentUsername == null) {
//            // If no user is logged in, we can still consider this a "success"
//            // because the desired end state (no one logged in) is already achieved
//            currentUsername = "";
//        }

        userDataAccessObject.setCurrentUsername(null);
        LogoutOutputData logoutOutputData = new LogoutOutputData(currentUsername);
        logoutPresenter.prepareSuccessView(logoutOutputData);
    }
}