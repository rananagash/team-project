package interface_adapter.signup;

import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

public class SignupPresenter implements SignupOutputBoundary {

    private final SignupViewModel signupViewModel;

    public SignupPresenter(SignupViewModel signupViewModel) {
        this.signupViewModel = signupViewModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData response) {
        // On success, just show success message in signup view
        final SignupState signupState = signupViewModel.getState();
        signupState.setUsername(""); // Clear form
        signupState.setPassword("");
        signupState.setRepeatPassword("");
        signupState.setSuccessMessage("Signup successful! You can now login.");
        signupViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        final SignupState signupState = signupViewModel.getState();
        signupState.setUsernameError(error);
        signupViewModel.firePropertyChange();
    }

    @Override
    public void switchToLoginView() {
        // Temporarily do nothing - will implement when login is added
        System.out.println("Login view switch would happen here");
    }
}