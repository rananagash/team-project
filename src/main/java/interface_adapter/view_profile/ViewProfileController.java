package interface_adapter.view_profile;

import interface_adapter.ViewManagerModel;
import use_case.view_profile.ViewProfileInputBoundary;
import use_case.view_profile.ViewProfileInputData;

/**
 * Controller for the View Profile use case.
 */
public class ViewProfileController {
    private final ViewProfileInputBoundary viewProfileUseCaseInteractor;
    private final ViewManagerModel viewManagerModel;

    public ViewProfileController(ViewProfileInputBoundary viewProfileUseCaseInteractor,
                                 ViewManagerModel viewManagerModel) {
        this.viewProfileUseCaseInteractor = viewProfileUseCaseInteractor;
        this.viewManagerModel = viewManagerModel;
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

        viewManagerModel.setState("view profile");
        viewManagerModel.firePropertyChange();
    }
}