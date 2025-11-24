package app;

import data_access.InMemoryUserDataAccessObject;
import data_access.TMDbMovieDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.add_to_watchlist.AddWatchListController;
import interface_adapter.add_to_watchlist.AddWatchListPresenter;
import interface_adapter.compare_watchlists.CompareWatchListController;
import interface_adapter.compare_watchlists.CompareWatchListPresenter;
import interface_adapter.filter_movies.FilterMoviesController;
import interface_adapter.filter_movies.FilterMoviesViewModel;
import interface_adapter.filter_movies.FilterMoviesPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.logout.LogoutPresenter;
import interface_adapter.review_movie.ReviewMovieController;
import interface_adapter.review_movie.ReviewMoviePresenter;
import interface_adapter.review_movie.ReviewMovieViewModel;
import interface_adapter.search_movie.SearchMovieController;
import interface_adapter.search_movie.SearchMoviePresenter;
import interface_adapter.record_watchhistory.RecordWatchHistoryController;
import interface_adapter.record_watchhistory.RecordWatchHistoryPresenter;
import interface_adapter.view_watchhistory.ViewWatchHistoryController;
import interface_adapter.view_watchhistory.ViewWatchHistoryPresenter;
import interface_adapter.view_profile.ViewProfileController;
import interface_adapter.view_profile.ViewProfilePresenter;
import interface_adapter.view_profile.ViewProfileViewModel;
import use_case.add_to_watchlist.AddWatchListInteractor;
import use_case.change_password.ChangePasswordInteractor;
import use_case.compare_watchlists.CompareWatchListInteractor;
import use_case.filter_movies.FilterMoviesInteractor;
import use_case.login.LoginInteractor;
import use_case.login.LoginUserDataAccessInterface;
import use_case.logout.LogoutInteractor;
import use_case.logout.LogoutUserDataAccessInterface;
import use_case.record_watchhistory.RecordWatchHistoryInteractor;
import use_case.review_movie.ReviewMovieInteractor;
import use_case.search_movie.SearchMovieInteractor;
import use_case.view_profile.ViewProfileUserDataAccessInterface;
import use_case.view_watchhistory.ViewWatchHistoryInteractor;
import use_case.view_profile.ViewProfileInteractor;
import view.LoggedInView;
import view.ViewWatchHistoryPopup;

import javax.swing.*;

public class AppBuilder {

    private final InMemoryUserDataAccessObject userGateway = new InMemoryUserDataAccessObject();
    private final TMDbMovieDataAccessObject movieGateway = new TMDbMovieDataAccessObject();

    private LoginViewModel loginViewModel;
    private LoggedInViewModel loggedInViewModel;
    private ReviewMovieViewModel reviewMovieViewModel;
    private ViewManagerModel viewManagerModel;

    public AppBuilder() {
        this.loginViewModel = new LoginViewModel();
        this.loggedInViewModel = new LoggedInViewModel();
        this.reviewMovieViewModel = new ReviewMovieViewModel();
        this.viewManagerModel = new ViewManagerModel();
    }

    public ReviewMovieViewModel getReviewMovieViewModel() {
        return reviewMovieViewModel;
    }

    public ViewManagerModel getViewManagerModel() {
        return viewManagerModel;
    }

    public LoggedInViewModel getLoggedInViewModel() {
        return loggedInViewModel;
    }

    public LoginViewModel getLoginViewModel() {
        return loginViewModel;
    }

    // ===== Login =====
    public LoginController buildLoginController() {
        LoginPresenter presenter = new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
        LoginInteractor interactor = new LoginInteractor((LoginUserDataAccessInterface) userGateway, presenter);
        return new LoginController(interactor);
    }

    // ===== Logout =====
    public LogoutController buildLogoutController() {
        LogoutPresenter presenter = new LogoutPresenter(viewManagerModel, loggedInViewModel, loginViewModel);
        LogoutInteractor interactor = new LogoutInteractor((LogoutUserDataAccessInterface) userGateway, presenter);
        return new LogoutController(interactor);
    }

    public SearchMovieController buildSearchMovieController() {
        SearchMoviePresenter presenter = new SearchMoviePresenter();
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
        AddWatchListPresenter presenter = new AddWatchListPresenter(null);
        AddWatchListInteractor interactor = new AddWatchListInteractor(presenter, userGateway);
        return new AddWatchListController(interactor);
    }

    public CompareWatchListController buildCompareWatchListController() {
        CompareWatchListPresenter presenter = new CompareWatchListPresenter();
        CompareWatchListInteractor interactor = new CompareWatchListInteractor(userGateway, presenter);
        return new CompareWatchListController(interactor);
    }

    public ViewWatchHistoryController buildViewWatchHistoryController(javax.swing.JFrame parentFrame) {
        ViewWatchHistoryPopup view = new ViewWatchHistoryPopup(parentFrame);
        ViewWatchHistoryPresenter presenter = new ViewWatchHistoryPresenter(view);
        ViewWatchHistoryInteractor interactor = new ViewWatchHistoryInteractor(userGateway, presenter);
        return new ViewWatchHistoryController(interactor);
    }

    public ViewWatchHistoryController buildViewWatchHistoryController() {
        ViewWatchHistoryPresenter presenter = new ViewWatchHistoryPresenter(null);
        ViewWatchHistoryInteractor interactor = new ViewWatchHistoryInteractor(userGateway, presenter);
        return new ViewWatchHistoryController(interactor);
    }

    public RecordWatchHistoryController buildRecordWatchHistoryController() {
        RecordWatchHistoryPresenter presenter = new RecordWatchHistoryPresenter();
        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(userGateway, movieGateway, presenter);
        return new RecordWatchHistoryController(interactor);
    }

    public ReviewMovieController buildReviewMovieController() {
        ReviewMoviePresenter presenter = new ReviewMoviePresenter(reviewMovieViewModel);
        ReviewMovieInteractor interactor = new ReviewMovieInteractor(userGateway, movieGateway, presenter);
        return new ReviewMovieController(interactor);
    }

    // ===== View Profile =====
    public ViewProfileController buildViewProfileController() {
        ViewProfileViewModel viewProfileViewModel = new ViewProfileViewModel();
        ViewProfilePresenter presenter = new ViewProfilePresenter(viewProfileViewModel);
        ViewProfileInteractor interactor = new ViewProfileInteractor((ViewProfileUserDataAccessInterface) userGateway, presenter);
        return new ViewProfileController(interactor);
    }

    // ===== Logged In View =====
    public LoggedInView buildLoggedInView(JFrame parentFrame) {
        LoggedInView loggedInView = new LoggedInView(loggedInViewModel);
        loggedInView.setLogoutController(buildLogoutController());
        loggedInView.setViewWatchHistoryController(buildViewWatchHistoryController(parentFrame));
        loggedInView.setReviewMovieController(buildReviewMovieController());
        loggedInView.setReviewMovieViewModel(reviewMovieViewModel);
        loggedInView.setController(buildSearchMovieController());
        loggedInView.setViewProfileController(buildViewProfileController());
        return loggedInView;
    }
}