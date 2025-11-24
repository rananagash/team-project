package interface_adapter.view_profile;

import use_case.view_profile.ViewProfileInputBoundary;
import use_case.view_profile.ViewProfileInputData;

/**
 * Controller for the View Profile use case.
 */
public class ViewProfileController {
    private final ViewProfileInputBoundary viewProfileUseCaseInteractor;

    public ViewProfileController(ViewProfileInputBoundary viewProfileUseCaseInteractor) {
        this.viewProfileUseCaseInteractor = viewProfileUseCaseInteractor;
    }

    /**
     * Executes the view profile use case for the given username.
     * @param username the username to view profile for
     */
    public void execute(String username) {
        ViewProfileInputData viewProfileInputData = new ViewProfileInputData(username);
        viewProfileUseCaseInteractor.execute(viewProfileInputData);
    }

    public void switchToProfile(String username) {
        ViewProfileInputData viewProfileInputData = new ViewProfileInputData(username);
        viewProfileUseCaseInteractor.execute(viewProfileInputData);
    }
}