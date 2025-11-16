package interface_adapter.filter_movies;

import use_case.filter_movies.FilterMoviesInteractor;
import use_case.filter_movies.FilterMoviesOutputBoundary;
import use_case.filter_movies.FilterMoviesResponseModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        // Convert genre IDs to genre names for display
        List<String> genreNames = responseModel.getRequestedGenres().stream()
                .map(genreId -> {
                    String genreName = FilterMoviesInteractor.getGenreName(genreId);
                    return genreName != null ? genreName : "Unknown Genre (" + genreId + ")";
                })
                .collect(Collectors.toList());
        viewModel.setSelectedGenreNames(genreNames);

        // If no movies found, set an informational message
        if (responseModel.getMovies().isEmpty()) {
            String genreList = String.join(", ", genreNames);
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

