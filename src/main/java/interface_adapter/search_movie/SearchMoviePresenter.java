package interface_adapter.search_movie;

import use_case.search_movie.SearchMovieOutputBoundary;
import use_case.search_movie.SearchMovieResponseModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logged_in.LoggedInState;

import java.util.ArrayList;

public class SearchMoviePresenter implements SearchMovieOutputBoundary {

    private final LoggedInViewModel loggedInViewModel;

    public SearchMoviePresenter(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(SearchMovieResponseModel responseModel) {
        // check what has API responded
        LoggedInState currentState = loggedInViewModel.getState();
        if (currentState == null) {
            currentState = new LoggedInState();
        }

        currentState.setSearchResults(responseModel.getMovies());
        currentState.setCurrentPage(responseModel.getCurrentPage());
        currentState.setTotalPages(responseModel.getTotalPages());
        currentState.setLastQuery(responseModel.getQuery());
        currentState.setSearchError(null);
        // refresh view
        loggedInViewModel.setState(currentState);
        loggedInViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        LoggedInState currentState = loggedInViewModel.getState();
        if (currentState == null) {
            currentState = new LoggedInState();
        }

        currentState.setSearchError(errorMessage);
        currentState.setSearchResults(new ArrayList<>());

        loggedInViewModel.setState(currentState);
        loggedInViewModel.firePropertyChange();
    }
}
