package app;

import data_access.InMemoryUserDataAccessObject;
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
import interface_adapter.search_movie.SearchMovieController;
import interface_adapter.search_movie.SearchMoviePresenter;
import interface_adapter.record_watchhistory.RecordWatchHistoryController;
import interface_adapter.record_watchhistory.RecordWatchHistoryPresenter;
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

    private final InMemoryUserDataAccessObject userGateway = new InMemoryUserDataAccessObject();
    private final TMDbMovieDataAccessObject movieGateway = new TMDbMovieDataAccessObject();

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

    /**
     * Builds ViewWatchHistoryController without a parent frame (for testing or console mode).
     * The presenter will fall back to console output if view is not set.
     */
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
        ReviewMoviePresenter presenter = new ReviewMoviePresenter();
        ReviewMovieInteractor interactor = new ReviewMovieInteractor(userGateway, movieGateway, presenter);
        return new ReviewMovieController(interactor);
    }

    /*
     * TODO(team): Wire controllers into Swing views here.
     */
}
