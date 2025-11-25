package app;

import interface_adapter.compare_watchlists.CompareWatchListViewModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.view_watchhistory.ViewWatchHistoryController;
import interface_adapter.review_movie.ReviewMovieController;
import interface_adapter.review_movie.ReviewMovieViewModel;
import view.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.signup.SignupController;

import java.awt.CardLayout;
import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        AppBuilder builder = new AppBuilder();

        // Initialize Models first
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
        LoginViewModel loginViewModel = new LoginViewModel();
        SignupViewModel signupViewModel = new SignupViewModel();

        // Build Main Window
        JFrame application = new JFrame("Movie App");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.setSize(800, 600);

        CardLayout cardLayout = new CardLayout();
        JPanel views = new JPanel(cardLayout);
        application.add(views);

        new ViewManager(views, cardLayout, viewManagerModel);

        // Build Controllers and Views
        interface_adapter.search_movie.SearchMovieViewModel searchMovieViewModel = new interface_adapter.search_movie.SearchMovieViewModel();
        interface_adapter.search_movie.SearchMovieController searchMovieController = builder
                .buildSearchMovieController(searchMovieViewModel);
        view.SearchResultsView searchResultsView = new view.SearchResultsView(searchMovieViewModel);
        searchResultsView.setController(searchMovieController);

        builder.buildFilterMoviesController();
        builder.buildAddWatchListController();

        CompareWatchListViewModel compareWatchListViewModel = new CompareWatchListViewModel();
        interface_adapter.compare_watchlists.CompareWatchListController compareWatchListController = builder
                .buildCompareWatchListController(compareWatchListViewModel);
        view.CompareWatchListView compareWatchListView = new view.CompareWatchListView(
                compareWatchListViewModel, loggedInViewModel, viewManagerModel);
        compareWatchListView.setController(compareWatchListController);

        ReviewMovieController reviewController = builder.buildReviewMovieController();
        ReviewMovieViewModel reviewViewModel = builder.getReviewMovieViewModel();

        // ✅ FIXED: correct constructor usage
        LoggedInView loggedInView = new LoggedInView(loggedInViewModel, viewManagerModel);

        loggedInView.setReviewMovieController(reviewController);
        loggedInView.setReviewMovieViewModel(reviewViewModel);

        ViewWatchHistoryController viewHistoryController = builder.buildViewWatchHistoryController(application);
        loggedInView.setViewWatchHistoryController(viewHistoryController);

        LoginController loginController = builder.buildLoginController(viewManagerModel, loginViewModel,
                loggedInViewModel);
        LoginView loginView = new LoginView(loginViewModel);
        loginView.setLoginController(loginController);

        SignupController signupController = builder.buildSignupController(viewManagerModel, signupViewModel,
                loginViewModel);
        SignupView signupView = new SignupView(signupViewModel);
        signupView.setSignupController(signupController);

        interface_adapter.logout.LogoutController logoutController = builder.buildLogoutController(
                viewManagerModel,
                loggedInViewModel, loginViewModel);
        loggedInView.setLogoutController(logoutController);

        interface_adapter.logged_in.ChangePasswordController changePasswordController = builder
                .buildChangePasswordController(loggedInViewModel);
        loggedInView.setChangePasswordController(changePasswordController);

        views.add(compareWatchListView, "compare watchlists");
        views.add(searchResultsView, "search movies");
        views.add(loginView, loginView.getViewName());
        views.add(signupView, signupView.getViewName());
        views.add(loggedInView, loggedInView.getViewName());

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
