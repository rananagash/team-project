package app;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

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
import interface_adapter.record_watchhistory.RecordWatchHistoryController;
import interface_adapter.record_watchhistory.RecordWatchHistoryPresenter;
import interface_adapter.review_movie.ReviewMovieController;
import interface_adapter.review_movie.ReviewMoviePresenter;
import interface_adapter.review_movie.ReviewMovieViewModel;
import interface_adapter.search_movie.SearchMovieController;
import interface_adapter.search_movie.SearchMoviePresenter;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.view_profile.ViewProfileController;
import interface_adapter.view_profile.ViewProfilePresenter;
import interface_adapter.view_profile.ViewProfileViewModel;
import interface_adapter.view_watchhistory.ViewWatchHistoryController;
import interface_adapter.view_watchhistory.ViewWatchHistoryPresenter;
import interface_adapter.view_watchlists.ViewWatchListsController;
import interface_adapter.view_watchlists.ViewWatchListsPresenter;
import interface_adapter.view_watchlists.ViewWatchListsViewModel;
import use_case.add_to_watchlist.AddWatchListInputBoundary;
import use_case.add_to_watchlist.AddWatchListInteractor;
import use_case.change_password.ChangePasswordInputBoundary;
import use_case.change_password.ChangePasswordInteractor;
import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.common.MovieGateway;
import use_case.common.UserDataAccessInterface;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.logout.LogoutInputBoundary;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutOutputBoundary;
import use_case.record_watchhistory.RecordWatchHistoryInputBoundary;
import use_case.record_watchhistory.RecordWatchHistoryInteractor;
import use_case.record_watchhistory.RecordWatchHistoryOutputBoundary;
import use_case.review_movie.ReviewMovieInteractor;
import use_case.search_movie.SearchMovieInputBoundary;
import use_case.search_movie.SearchMovieInteractor;
import use_case.search_movie.SearchMovieOutputBoundary;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.view_profile.ViewProfileInputBoundary;
import use_case.view_profile.ViewProfileInteractor;
import use_case.view_profile.ViewProfileOutputBoundary;
import use_case.view_watchhistory.ViewWatchHistoryInputBoundary;
import use_case.view_watchhistory.ViewWatchHistoryInteractor;
import use_case.view_watchhistory.ViewWatchHistoryOutputBoundary;
import use_case.view_watchlists.ViewWatchListsInputBoundary;
import use_case.view_watchlists.ViewWatchListsInteractor;
import use_case.view_watchlists.ViewWatchListsOutputBoundary;
import view.AddReviewPopup;
import view.LoggedInView;
import view.LoginView;
import view.ProfileView;
import view.RecordWatchHistoryPopup;
import view.SignupView;
import view.ViewManager;
import view.ViewWatchHistoryPopup;
import view.ViewWatchListsView;

/**
 * Application builder responsible for constructing.
 * <ul>
 *     <li>All views</li>
 *     <li>All view models</li>
 *     <li>All controllers</li>
 *     <li>All presenters</li>
 *     <li>All interactors</li>
 *     <li>The application-level data access shared across use cases</li>
 * </ul>
 */
public class AppBuilder {

    // ==============================================================
    // ================ Top-level UI & Frame Manager ================
    // ==============================================================

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // ==============================================================
    // ===================== Data Access Layer ======================
    // ==============================================================

    private final UserFactory userFactory = new UserFactory();
    private final UserDataAccessInterface fileUserDataAccessObject =
            new FileUserDataAccessObject("data/users.json", userFactory);
    private final CachedUserDataAccessObject userDataAccessObject =
            new CachedUserDataAccessObject(fileUserDataAccessObject);

    private final MovieGateway movieDataAccessObject = new TMDbMovieDataAccessObject();

    // ==============================================================
    // =================== View Models and Views ====================
    // ==============================================================

    private SignupView signupView;
    private SignupViewModel signupViewModel;

    private LoginView loginView;
    private LoginViewModel loginViewModel;

    private LoggedInView loggedInView;
    private LoggedInViewModel loggedInViewModel;

    private ProfileView profileView;
    private ViewProfileViewModel viewProfileViewModel;

    private ViewWatchListsView viewWatchListsView;
    private ViewWatchListsViewModel viewWatchListsViewModel;

    private AddReviewPopup addReviewPopup;
    private ReviewMovieViewModel reviewMovieViewModel;

    private RecordWatchHistoryPopup recordWatchHistoryPopup;
    private ViewWatchHistoryPopup viewWatchHistoryPopup;

    // ==============================================================
    // ======================== Constructor =========================
    // ==============================================================

    /**
     * Creates an AppBuilder with a prepared card layout.
     */
    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    /**
     * Creates and registers the Signup view and its view model.
     * @return this builder for chaining
     */
    public AppBuilder addSignupView() {
        signupViewModel = new SignupViewModel();
        signupView = new SignupView(signupViewModel);
        cardPanel.add(signupView, signupView.getViewName());
        return this;
    }

    /**
     * Creates and registers the Login view and its view model.
     * @return this builder for chaining
     */
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel, viewManagerModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

    /**
     * Creates and registers the Logged In view and its view model.
     * @return this builder for chaining
     */
    public AppBuilder addLoggedInView() {
        loggedInViewModel = new LoggedInViewModel();
        loggedInView = new LoggedInView(loggedInViewModel);
        cardPanel.add(loggedInView, loggedInView.getViewName());
        addAddToWatchListUseCase();
        return this;
    }

    /**
     * Creates and registers the User Profile view and its view model.
     * @return this builder for chaining
     */
    public AppBuilder addProfileView() {
        viewProfileViewModel = new ViewProfileViewModel();
        profileView = new ProfileView(viewProfileViewModel);
        cardPanel.add(profileView, profileView.getViewName());
        return this;
    }

    /**
     * Creates and registers the View Watchlists view and its view model.
     * @return this builder for chaining
     */
    public AppBuilder addViewWatchListsView() {
        viewWatchListsViewModel = new ViewWatchListsViewModel(viewManagerModel);
        viewWatchListsView = new ViewWatchListsView(viewWatchListsViewModel);
        cardPanel.add(viewWatchListsView, viewWatchListsView.getViewName());
        return this;
    }

    // ==============================================================
    // ======================= Use Case Wiring ======================
    // ==============================================================

    /**
     * Wires the Signup use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, loginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        final SignupController controller = new SignupController(userSignupInteractor);
        signupView.setSignupController(controller);
        return this;
    }

    /**
     * Wires the Login use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        final LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    /**
     * Wires the Change Password use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addChangePasswordUseCase() {
        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
                loggedInViewModel);

        final ChangePasswordInputBoundary changePasswordInteractor =
                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);

        final ChangePasswordController changePasswordController =
                new ChangePasswordController(changePasswordInteractor);
        loggedInView.setChangePasswordController(changePasswordController);
        return this;
    }

    /**
     * Wires the Logout use case.
     *
     * @return this builder for chaining
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

    /**
     * Wires the Search Movies use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addSearchMoviesUseCase() {
        final SearchMovieOutputBoundary searchOutputBoundary =
                new SearchMoviePresenter(loggedInViewModel);

        final TMDbMovieDataAccessObject movieGateway = new TMDbMovieDataAccessObject();

        final SearchMovieInputBoundary searchInteractor =
                new SearchMovieInteractor(movieGateway, searchOutputBoundary);

        final SearchMovieController searchController = new SearchMovieController(searchInteractor);

        loggedInView.setController(searchController);

        return this;
    }

    /**
     * Wires the View Profile use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addViewProfileUseCase() {
        // TODO: Rana double check this
        final ViewProfileOutputBoundary viewProfileOutputBoundary = new ViewProfilePresenter(viewProfileViewModel);

        final ViewProfileInputBoundary viewProfileInteractor =
                new ViewProfileInteractor(userDataAccessObject, viewProfileOutputBoundary);

        final ViewProfileController viewProfileController =
                new ViewProfileController(viewProfileInteractor, viewManagerModel);
        profileView.setViewProfileController(viewProfileController);
        loggedInView.setViewProfileController(viewProfileController);
        return this;
    }

    /**
     * Wires the View Watchlists use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addViewWatchListsUseCase() {
        final ViewWatchListsOutputBoundary viewWatchListsPresenter =
                new ViewWatchListsPresenter(viewWatchListsViewModel, viewManagerModel);
        final ViewWatchListsInputBoundary viewWatchListsInteractor =
                new ViewWatchListsInteractor(userDataAccessObject,
                viewWatchListsPresenter);
        final ViewWatchListsController viewWatchListsController =
                new ViewWatchListsController(viewWatchListsInteractor, viewManagerModel);

        viewWatchListsView.setController(viewWatchListsController);
        loggedInView.setViewWatchListsController(viewWatchListsController);
        profileView.setViewWatchListsController(viewWatchListsController);

        return this;
    }

    /**
     * Wires the View Watch History use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addViewWatchHistoryUseCase() {
        // Create ViewWatchHistoryPopup with a temporary parent frame
        // The popup will be reused and shown/hidden as needed
        // Note: The parent frame is set to a temporary frame here, but the popup
        // will be properly positioned when displayed
        final JFrame tempParent = new JFrame();
        tempParent.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        viewWatchHistoryPopup = new ViewWatchHistoryPopup(tempParent);

        // Create presenter with the view
        final ViewWatchHistoryOutputBoundary presenter = new ViewWatchHistoryPresenter(viewWatchHistoryPopup);

        // Create interactor with user data access and presenter
        final ViewWatchHistoryInputBoundary interactor =
                new ViewWatchHistoryInteractor(userDataAccessObject, presenter);

        // Create controller with the interactor
        final ViewWatchHistoryController controller = new ViewWatchHistoryController(interactor);

        // Connect controller to LoggedInView and ProfileView
        loggedInView.setViewWatchHistoryController(controller);
        profileView.setViewWatchHistoryController(controller);

        return this;
    }

    /**
     * Wires the Filter Movies use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addFilterMoviesUseCase() {
        // TODO: Inba
        return this;
    }

    /**
     * Wires the Add to Watchlist use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addAddToWatchListUseCase() {
        // Create presenter (no view yet)
        final AddWatchListPresenter addWatchListPresenter = new AddWatchListPresenter();

        // Create interactor
        final AddWatchListInputBoundary addWatchListInputBoundary = new AddWatchListInteractor(
                addWatchListPresenter,
                userDataAccessObject,
                movieDataAccessObject);

        // Create controller
        final AddWatchListController addWatchListController =
                new AddWatchListController(addWatchListInputBoundary, addWatchListPresenter);

        // Inject controller & presenter into LoggedInView
        loggedInView.setAddWatchListController(addWatchListController);
        loggedInView.setAddWatchListPresenter(addWatchListPresenter);
        return this;
    }

    /**
     * Wires the Record Watch History use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addRecordWatchHistoryPopup() {
        // Create RecordWatchHistoryPopup with a temporary parent frame
        // The popup will be reused and shown/hidden as needed
        // Note: The parent frame is set to a temporary frame here, but the popup
        // will be properly positioned when displayed
        final JFrame tempParent = new JFrame();
        tempParent.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        recordWatchHistoryPopup = new RecordWatchHistoryPopup(tempParent);

        // Create presenter with the view
        final RecordWatchHistoryOutputBoundary presenter = new RecordWatchHistoryPresenter(recordWatchHistoryPopup);

        // Create interactor with user data access, movie gateway, and presenter
        final RecordWatchHistoryInputBoundary interactor = new RecordWatchHistoryInteractor(
                userDataAccessObject, movieDataAccessObject, presenter);

        // Create controller with the interactor
        final RecordWatchHistoryController controller = new RecordWatchHistoryController(interactor);

        // Connect controller to LoggedInView
        loggedInView.setRecordWatchHistoryController(controller);

        return this;
    }

    /**
     * Wires the Add Review use case.
     *
     * @return this builder for chaining
     */
    public AppBuilder addAddReviewPopup() {
        // TODO: Oliver check this implementation
        reviewMovieViewModel = new ReviewMovieViewModel();
        final ReviewMoviePresenter presenter = new ReviewMoviePresenter(reviewMovieViewModel);
        final ReviewMovieInteractor interactor =
                new ReviewMovieInteractor(userDataAccessObject, movieDataAccessObject, presenter);
        final ReviewMovieController controller = new ReviewMovieController(interactor);

        loggedInView.setReviewMovieController(controller);
        loggedInView.setReviewMovieViewModel(reviewMovieViewModel);
        return this;
    }

    // ==============================================================
    // ======================= Final Assembly =======================
    // ==============================================================

    /**
     * Builds the complete Swing application window and returns it.
     *
     * @return a fully constructed {@link JFrame} for the MovieNight app
     */
    public JFrame build() {
        final JFrame application = new JFrame("MovieNight");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        // initial size of app
        application.setPreferredSize(new Dimension(900, 600));
        application.setMinimumSize(new Dimension(900, 600));

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
