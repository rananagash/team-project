package interface_adapter.search_movie;

import use_case.search_movie.SearchMovieOutputBoundary;
import use_case.search_movie.SearchMovieResponseModel;

public class SearchMoviePresenter implements SearchMovieOutputBoundary {

    private final SearchMovieViewModel searchMovieViewModel;

    public SearchMoviePresenter(SearchMovieViewModel searchMovieViewModel) {
        this.searchMovieViewModel = searchMovieViewModel;
    }

    @Override
    public void prepareSuccessView(SearchMovieResponseModel responseModel) {
        SearchMovieState state = searchMovieViewModel.getState();
        state.setMovies(responseModel.getMovies());
        state.setCurrentPage(responseModel.getCurrentPage());
        state.setTotalPages(responseModel.getTotalPages());
        state.setQuery(responseModel.getQuery());
        state.setError(null);
        searchMovieViewModel.setState(state);
        searchMovieViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        SearchMovieState state = searchMovieViewModel.getState();
        state.setError(errorMessage);
        searchMovieViewModel.setState(state);
        searchMovieViewModel.firePropertyChange();
    }
}
