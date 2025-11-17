package interface_adapter.filter_movies;

import use_case.filter_movies.FilterMoviesOutputBoundary;
import use_case.filter_movies.FilterMoviesResponseModel;

import java.util.ArrayList;

public class FilterMoviesPresenter implements FilterMoviesOutputBoundary {

    private final FilterMoviesViewModel viewModel;

    public FilterMoviesPresenter(FilterMoviesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(FilterMoviesResponseModel responseModel) {
        // Clear any previous errors
        viewModel.clearError();

        // Set the filtered movies
        viewModel.setFilteredMovies(responseModel.getMovies());

        // Set the genre names (already converted by the use case layer)
        viewModel.setSelectedGenreNames(responseModel.getRequestedGenreNames());

        // If no movies found, set an informational message
        if (responseModel.getMovies().isEmpty()) {
            String genreList = String.join(", ", responseModel.getRequestedGenreNames());
            viewModel.setErrorMessage("No movies found for the selected genres: " + genreList);
        }
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Clear movies and genre names on failure
        viewModel.setFilteredMovies(new ArrayList<>());
        viewModel.setSelectedGenreNames(new ArrayList<>());

        // Set the error message
        viewModel.setErrorMessage(errorMessage);
    }
}

