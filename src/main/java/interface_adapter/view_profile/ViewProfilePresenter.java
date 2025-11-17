package interface_adapter.view_profile;

import use_case.view_profile.ViewProfileOutputBoundary;
import use_case.view_profile.ViewProfileOutputData;

/**
 * Presenter for the View Profile use case.
 */
public class ViewProfilePresenter implements ViewProfileOutputBoundary {
    private final ViewProfileViewModel viewProfileViewModel;

    public ViewProfilePresenter(ViewProfileViewModel viewProfileViewModel) {
        this.viewProfileViewModel = viewProfileViewModel;
    }

    @Override
    public void prepareSuccessView(ViewProfileOutputData response) {
        ViewProfileState state = viewProfileViewModel.getState();
        state.setUser(response.getUser());
        state.setProfileStats(response.getProfileStats());
        state.setError(null);
        viewProfileViewModel.setState(state);
        viewProfileViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        ViewProfileState state = viewProfileViewModel.getState();
        state.setError(error);
        viewProfileViewModel.setState(state);
        viewProfileViewModel.firePropertyChange();
    }
}