package app;

import data_access.FileUserDataAccessObject;
import entity.factories.UserFactory;
import data_access.TMDbMovieDataAccessObject;
import interface_adapter.add_to_watchlist.AddWatchListController;
import interface_adapter.add_to_watchlist.AddWatchListPresenter;
import interface_adapter.compare_watchlists.CompareWatchListController;
import interface_adapter.compare_watchlists.CompareWatchListPresenter;
import interface_adapter.filter_movies.FilterMoviesController;
import interface_adapter.filter_movies.FilterMoviesViewModel;
import interface_adapter.filter_movies.FilterMoviesPresenter;
import interface_adapter.review_movie.ReviewMovieController;
import interface_adapter.review_movie.ReviewMoviePresenter;
import interface_adapter.review_movie.ReviewMovieViewModel;
import interface_adapter.search_movie.SearchMovieController;
import interface_adapter.search_movie.SearchMoviePresenter;
import interface_adapter.record_watchhistory.RecordWatchHistoryController;
import interface_adapter.record_watchhistory.RecordWatchHistoryPresenter;
import interface_adapter.compare_watchlists.CompareWatchListViewModel;
import view.RecordWatchHistoryPopup;
import interface_adapter.view_watchhistory.ViewWatchHistoryController;
import interface_adapter.view_watchhistory.ViewWatchHistoryPresenter;
import view.ViewWatchHistoryPopup;
import use_case.add_to_watchlist.AddWatchListInteractor;
import use_case.compare_watchlists.CompareWatchListInteractor;
import use_case.filter_movies.FilterMoviesInteractor;
import use_case.record_watchhistory.RecordWatchHistoryInteractor;
import use_case.review_movie.ReviewMovieInteractor;
import use_case.search_movie.SearchMovieInteractor;
import use_case.view_watchhistory.ViewWatchHistoryInteractor;

public class AppBuilder {

    private final UserFactory userFactory = new UserFactory();
    private final FileUserDataAccessObject userGateway = new FileUserDataAccessObject("users.csv", userFactory);
    private final TMDbMovieDataAccessObject movieGateway = new TMDbMovieDataAccessObject();
    private ReviewMovieViewModel reviewMovieViewModel;

    public ReviewMovieViewModel getReviewMovieViewModel() {
        return reviewMovieViewModel;
    }

    public SearchMovieController buildSearchMovieController(
            interface_adapter.search_movie.SearchMovieViewModel searchMovieViewModel) {
        SearchMoviePresenter presenter = new SearchMoviePresenter(searchMovieViewModel);
        SearchMovieInteractor interactor = new SearchMovieInteractor(movieGateway, presenter);
        return new SearchMovieController(interactor);
    }

    public FilterMoviesController buildFilterMoviesController() {
        FilterMoviesViewModel viewModel = new FilterMoviesViewModel();
        FilterMoviesPresenter presenter = new FilterMoviesPresenter(viewModel);
        FilterMoviesInteractor interactor = new FilterMoviesInteractor(movieGateway, presenter);
        return new FilterMoviesController(interactor);
    }

    public AddWatchListController buildAddWatchListController() {
        final AddWatchListPresenter presenter = new AddWatchListPresenter(null);
        final AddWatchListInteractor interactor = new AddWatchListInteractor(presenter, userGateway);
        return new AddWatchListController(interactor, presenter);
    }

    public CompareWatchListController buildCompareWatchListController(
            CompareWatchListViewModel compareWatchListViewModel) {
        CompareWatchListPresenter presenter = new CompareWatchListPresenter(compareWatchListViewModel);
        CompareWatchListInteractor interactor = new CompareWatchListInteractor(userGateway, presenter);
        return new CompareWatchListController(interactor);
    }

    public ViewWatchHistoryController buildViewWatchHistoryController(javax.swing.JFrame parentFrame) {
        ViewWatchHistoryPopup view = new ViewWatchHistoryPopup(parentFrame);
        ViewWatchHistoryPresenter presenter = new ViewWatchHistoryPresenter(view);
        ViewWatchHistoryInteractor interactor = new ViewWatchHistoryInteractor(userGateway, presenter);
        return new ViewWatchHistoryController(interactor);
    }

    /**
     * Builds ViewWatchHistoryController without a parent frame (for testing or
     * console mode).
     * The presenter will fall back to console output if view is not set.
     */
    public ViewWatchHistoryController buildViewWatchHistoryController() {
        ViewWatchHistoryPresenter presenter = new ViewWatchHistoryPresenter(null);
        ViewWatchHistoryInteractor interactor = new ViewWatchHistoryInteractor(userGateway, presenter);
        return new ViewWatchHistoryController(interactor);
    }

    public RecordWatchHistoryController buildRecordWatchHistoryController(javax.swing.JFrame parentFrame) {
        RecordWatchHistoryPopup view = new RecordWatchHistoryPopup(parentFrame);
        RecordWatchHistoryPresenter presenter = new RecordWatchHistoryPresenter(view);
        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(userGateway, movieGateway,
                presenter);
        return new RecordWatchHistoryController(interactor);
    }

    /**
     * Builds RecordWatchHistoryController without a parent frame (for testing or
     * console mode).
     * The presenter will fall back to console output if view is not set.
     */
    public RecordWatchHistoryController buildRecordWatchHistoryController() {
        RecordWatchHistoryPresenter presenter = new RecordWatchHistoryPresenter(null);
        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(userGateway, movieGateway,
                presenter);
        return new RecordWatchHistoryController(interactor);
    }

    public ReviewMovieController buildReviewMovieController() {
        reviewMovieViewModel = new ReviewMovieViewModel();
        ReviewMoviePresenter presenter = new ReviewMoviePresenter(reviewMovieViewModel);
        ReviewMovieInteractor interactor = new ReviewMovieInteractor(userGateway, movieGateway, presenter);
        return new ReviewMovieController(interactor);
    }

    public interface_adapter.login.LoginController buildLoginController(
            interface_adapter.ViewManagerModel viewManagerModel,
            interface_adapter.login.LoginViewModel loginViewModel,
            interface_adapter.logged_in.LoggedInViewModel loggedInViewModel) {
        interface_adapter.login.LoginPresenter loginPresenter = new interface_adapter.login.LoginPresenter(
                viewManagerModel, loggedInViewModel, loginViewModel);
        use_case.login.LoginInteractor loginInteractor = new use_case.login.LoginInteractor(userGateway,
                loginPresenter);
        return new interface_adapter.login.LoginController(loginInteractor);
    }

    public interface_adapter.signup.SignupController buildSignupController(
            interface_adapter.ViewManagerModel viewManagerModel,
            interface_adapter.signup.SignupViewModel signupViewModel,
            interface_adapter.login.LoginViewModel loginViewModel) {
        interface_adapter.signup.SignupPresenter signupPresenter = new interface_adapter.signup.SignupPresenter(
                viewManagerModel, signupViewModel, loginViewModel);
        use_case.signup.SignupInteractor signupInteractor = new use_case.signup.SignupInteractor(userGateway,
                signupPresenter, userFactory);
        return new interface_adapter.signup.SignupController(signupInteractor);
    }

    public interface_adapter.logout.LogoutController buildLogoutController(
            interface_adapter.ViewManagerModel viewManagerModel,
            interface_adapter.logged_in.LoggedInViewModel loggedInViewModel,
            interface_adapter.login.LoginViewModel loginViewModel) {
        interface_adapter.logout.LogoutPresenter logoutPresenter = new interface_adapter.logout.LogoutPresenter(
                viewManagerModel, loggedInViewModel, loginViewModel);
        use_case.logout.LogoutInteractor logoutInteractor = new use_case.logout.LogoutInteractor(userGateway,
                logoutPresenter);
        return new interface_adapter.logout.LogoutController(logoutInteractor);
    }

    public interface_adapter.logged_in.ChangePasswordController buildChangePasswordController(
            interface_adapter.logged_in.LoggedInViewModel loggedInViewModel) {
        interface_adapter.logged_in.ChangePasswordPresenter changePasswordPresenter = new interface_adapter.logged_in.ChangePasswordPresenter(
                null, loggedInViewModel);
        use_case.change_password.ChangePasswordInteractor changePasswordInteractor = new use_case.change_password.ChangePasswordInteractor(
                userGateway, changePasswordPresenter, userFactory);
        return new interface_adapter.logged_in.ChangePasswordController(changePasswordInteractor);
    }
}
