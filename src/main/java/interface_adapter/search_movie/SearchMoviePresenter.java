package interface_adapter.search_movie;

import entity.Movie;
import use_case.search_movie.SearchMovieOutputBoundary;
import use_case.search_movie.SearchMovieResponseModel;
import view.LoggedInView;

import java.util.List;

public class SearchMoviePresenter implements SearchMovieOutputBoundary {

    private final LoggedInView loggedInView;

    public SearchMoviePresenter(LoggedInView loggedInView) {
        this.loggedInView = loggedInView;
    }

    @Override
    public void prepareSuccessView(SearchMovieResponseModel responseModel) {
        List<Movie> movies = responseModel.getMovies();
        loggedInView.showResults(movies, responseModel.getCurrentPage(), responseModel.getTotalPages());
    }

    @Override
    public void prepareFailView(String errorMessage) {
        loggedInView.showError(errorMessage);
    }
}
