package use_case.login;

import java.util.List;

import entity.User;
import entity.WatchList;
import use_case.common.UserDataAccessInterface;

/**
 * The Login Interactor.
 */
public class LoginInteractor implements LoginInputBoundary {
    private final UserDataAccessInterface userDataAccessObject;
    private final LoginOutputBoundary loginPresenter;

    public LoginInteractor(UserDataAccessInterface userDataAccessInterface,
                           LoginOutputBoundary loginOutputBoundary) {
        this.userDataAccessObject = userDataAccessInterface;
        this.loginPresenter = loginOutputBoundary;
    }

    @Override
    public void execute(LoginInputData loginInputData) {
        final String username = loginInputData.getUsername();
        final String password = loginInputData.getPassword();

        if (!userDataAccessObject.existsByName(username)) {
            loginPresenter.prepareFailView(username + ": Account does not exist.");
        }
        else {
            final String pwd = userDataAccessObject.getUser(username).getPassword();
            if (!password.equals(pwd)) {
                loginPresenter.prepareFailView("Incorrect password for \"" + username + "\".");
            }
            else {

                final User user = userDataAccessObject.getUser(loginInputData.getUsername());

                userDataAccessObject.setCurrentUsername(username);

                final List<String> watchlistIds = user.getWatchLists().stream()
                        .map(WatchList::getWatchListId)
                        .toList();

                final List<String> watchlistNames = user.getWatchLists().stream()
                        .map(WatchList::getName)
                        .toList();

                final LoginOutputData loginOutputData = new LoginOutputData(
                        user.getUserName(),
                        watchlistIds,
                        watchlistNames);
                loginPresenter.prepareSuccessView(loginOutputData);
            }
        }
    }
}
