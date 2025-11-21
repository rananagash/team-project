package interface_adapter.filter_movies;

import use_case.filter_movies.FilterMoviesOutputBoundary;
import use_case.filter_movies.FilterMoviesResponseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter for the Filter Movies use case. This class receives the output
 * from the use case layer and prepares the data for the view model.
 * It handles both success and failure scenarios and ensures the view model
 * state is consistent and user-friendly.
 */
public class FilterMoviesPresenter implements FilterMoviesOutputBoundary {

    private final FilterMoviesViewModel viewModel;

    /**
     * Constructs a {@code FilterMoviesPresenter} with the specified view model.
     *
     * @param viewModel the view model used to update the UI state
     */
    public FilterMoviesPresenter(FilterMoviesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Prepares the view model for a successful filter operation.
     * Clears previous errors, sets the filtered movie list, and sets the
     * selected genre names. If no movies were found, an informational
     * message is set on the view model.
     *
     * @param responseModel the response model containing filtered movies
     *                      and the list of requested genre names
     */
    @Override
    public void prepareSuccessView(FilterMoviesResponseModel responseModel) {
        viewModel.clearError();

        // Update movies and genre names
        viewModel.setFilteredMovies(responseModel.getMovies());
        viewModel.setSelectedGenreNames(responseModel.getRequestedGenreNames());

        // Show informational message when the list is empty
        if (responseModel.getMovies().isEmpty()) {
            String genreList = String.join(", ", responseModel.getRequestedGenreNames());
            viewModel.setErrorMessage("No movies found for the selected genres: " + genreList);
        }
    }

    /**
     * Prepares the view model for a failed filter operation.
     * Clears movies and genre selections and displays the provided error message.
     *
     * @param errorMessage the error message to display
     */
    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setFilteredMovies(new ArrayList<>());
        viewModel.setSelectedGenreNames(new ArrayList<>());
        viewModel.setErrorMessage(errorMessage);
    }
}
