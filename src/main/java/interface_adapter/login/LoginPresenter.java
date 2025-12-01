package interface_adapter.login;

import java.util.ArrayList;
import java.util.List;

import interface_adapter.ViewManagerModel;
import interface_adapter.add_to_watchlist.AddWatchListViewModel;
import interface_adapter.add_to_watchlist.WatchListOption;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * The Presenter for the Login Use Case.
 */
public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;
    private final AddWatchListViewModel addWatchListViewModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInViewModel loggedInViewModel,
                          LoginViewModel loginViewModel,
                          AddWatchListViewModel addWatchListViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
        this.addWatchListViewModel = addWatchListViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, update the loggedInViewModel's state
        final LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        this.loggedInViewModel.firePropertyChange();

        // and clear everything from the LoginViewModel's state
        loginViewModel.setState(new LoginState());

        // switch to the logged in view
        this.viewManagerModel.setState(loggedInViewModel.getViewName());
        this.viewManagerModel.firePropertyChange();

        // Convert watchlist data into DTO
        final List<WatchListOption> watchlistOptions = new ArrayList<>();
        for (int i = 0; i < response.getWatchListIds().size(); i++) {
            watchlistOptions.add(new WatchListOption(
                    response.getWatchListIds().get(i),
                    response.getWatchListNames().get(i)
            ));

            addWatchListViewModel.setWatchListOptions(watchlistOptions);
        }
    }

    @Override
    public void prepareFailView(String error) {
        final LoginState loginState = loginViewModel.getState();
        loginState.setLoginError(error);
        loginViewModel.firePropertyChange();
    }
}
