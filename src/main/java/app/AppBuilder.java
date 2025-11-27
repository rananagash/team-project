package app;

import data_access.CachedUserDataAccessObject;
import data_access.FileUserDataAccessObject;
import data_access.TMDbMovieDataAccessObject;
import entity.factories.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_to_watchlist.AddWatchListController;
import interface_adapter.add_to_watchlist.AddWatchListPresenter;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.ChangePasswordPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.review_movie.ReviewMovieController;
import interface_adapter.review_movie.ReviewMoviePresenter;
import interface_adapter.review_movie.ReviewMovieViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import use_case.add_to_watchlist.AddWatchListInputBoundary;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.common.UserDataAccessInterface;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.view_profile.ViewProfileInputBoundary;
import use_case.view_profile.ViewProfileOutputBoundary;
import view.*;
import interface_adapter.view_profile.ViewProfileController;
import interface_adapter.view_profile.ViewProfilePresenter;
import interface_adapter.view_profile.ViewProfileViewModel;
import use_case.add_to_watchlist.AddWatchListInteractor;
import use_case.change_password.ChangePasswordInteractor;
import use_case.login.LoginInteractor;
import use_case.logout.LogoutInteractor;
import use_case.review_movie.ReviewMovieInteractor;
import use_case.view_profile.ViewProfileInteractor;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    //TODO: Alana - replace with wrapper DAO class once implemented
    private final UserDataAccessInterface persistent = new FileUserDataAccessObject("data/users.json", userFactory);
    private final CachedUserDataAccessObject userDataAccessObject = new CachedUserDataAccessObject(persistent);

    private SignupView signupView;
    private SignupViewModel signupViewModel;
    private LoginView loginView;
    private LoginViewModel loginViewModel;
    private LoggedInView loggedInView;
    private LoggedInViewModel loggedInViewModel;
    private AddReviewPopup addReviewPopup;
    private ReviewMovieViewModel reviewMovieViewModel;
    private AddToWatchListPopup addToWatchListPopup;
    private RecordWatchHistoryPopup recordWatchHistoryPopup;
    private ViewWatchHistoryPopup viewWatchHistoryPopup;
    private ProfileView profileView;
    private ViewProfileViewModel viewProfileViewModel;


    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        addAddToWatchListUseCase();
        return this;
    }

    public AppBuilder addProfileView() {
        viewProfileViewModel = new ViewProfileViewModel();
        profileView = new ProfileView(viewProfileViewModel);
        cardPanel.add(profileView, profileView.getViewName());
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Adds the Logout Use Case to the application.
     * @return this builder
     */
    public AppBuilder addLogoutUseCase() {
        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);

        final LogoutInputBoundary logoutInteractor =
                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);

        final LogoutController logoutController = new LogoutController(logoutInteractor);
        loggedInView.setLogoutController(logoutController);
        return this;
    }

    public AppBuilder addSearchMoviesUseCase() {
        //TODO:Chester
        return this;
    }

    public AppBuilder addViewProfileUseCase() {
        //TODO: Rana double check this
        final ViewProfileOutputBoundary viewProfileOutputBoundary = new ViewProfilePresenter(viewProfileViewModel);

        final ViewProfileInputBoundary viewProfileInteractor = new ViewProfileInteractor(userDataAccessObject, viewProfileOutputBoundary);

        final ViewProfileController viewProfileController = new ViewProfileController(viewProfileInteractor, viewManagerModel);
        profileView.setViewProfileController(viewProfileController);
        loggedInView.setViewProfileController(viewProfileController);
        return this;
    }

    public AppBuilder addViewWatchHistoryUseCase() {
        //TODO: Jiaqi
        return this;
    }

    public AppBuilder addFilterMoviesUseCase() {
        //TODO: Inba
        return this;
    }

    public AppBuilder addAddToWatchListUseCase() {
        AddWatchListPresenter addWatchListOutputBoundary = new AddWatchListPresenter(null);
        AddWatchListInputBoundary addWatchListInputBoundary = new AddWatchListInteractor(addWatchListOutputBoundary, userDataAccessObject);
        AddWatchListController addWatchListController = new AddWatchListController(addWatchListInputBoundary, addWatchListOutputBoundary);
        loggedInView.setAddWatchListController(addWatchListController);
        return this;
    }

    public AppBuilder addRecordWatchHistoryPopup() {
        //TODO: Jiaqi
        return this;
    }

    public AppBuilder addAddReviewPopup() {
        //TODO: Oliver check this implementation
        reviewMovieViewModel = new ReviewMovieViewModel();
        ReviewMoviePresenter presenter = new ReviewMoviePresenter(reviewMovieViewModel);
        TMDbMovieDataAccessObject movieGateway = new TMDbMovieDataAccessObject();
        ReviewMovieInteractor interactor = new ReviewMovieInteractor(userDataAccessObject, movieGateway, presenter);
        ReviewMovieController controller = new ReviewMovieController(interactor);

        loggedInView.setReviewMovieController(controller);
        loggedInView.setReviewMovieViewModel(reviewMovieViewModel);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(signupView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}