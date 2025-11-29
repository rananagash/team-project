package use_case.view_profile;

import entity.User;

/**
 * Interactor for the View Profile use case.
 */
public class ViewProfileInteractor implements ViewProfileInputBoundary {
    private final ViewProfileUserDataAccessInterface userDataAccessObject;
    private final ViewProfileOutputBoundary userPresenter;

    public ViewProfileInteractor(ViewProfileUserDataAccessInterface userDataAccessInterface,
                                 ViewProfileOutputBoundary viewProfileOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.userPresenter = viewProfileOutputBoundary;
    }

    @Override
    public void execute(ViewProfileInputData viewProfileInputData) {
        String username = viewProfileInputData.getUsername();

        User user = userDataAccessObject.getUser(username);
        if (user == null) {
            userPresenter.prepareFailView("User not found: " + username);
            return;
        }

        ProfileStats stats = userDataAccessObject.getUserStats(username);
        ViewProfileOutputData outputData = new ViewProfileOutputData(user, stats);
        userPresenter.prepareSuccessView(outputData);
    }
}