package interface_adapter.filter_movies;

import entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.filter_movies.FilterMoviesResponseModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterMoviesPresenterTest {

    private FilterMoviesPresenter presenter;
    private FilterMoviesViewModel viewModel;

    @BeforeEach
    void setUp() {
        viewModel = new FilterMoviesViewModel();
        presenter = new FilterMoviesPresenter(viewModel);
    }

    @Test
    void testPrepareSuccessViewWithNonEmptyMovies() {
        // Arrange
        List<Integer> requestedGenres = Arrays.asList(28, 12);
        List<String> requestedGenreNames = Arrays.asList("Action", "Adventure");
        Movie movie1 = new Movie("1", "Movie 1", "Plot 1",
                Arrays.asList(28), "2023-01-01", 8.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Movie 2", "Plot 2",
                Arrays.asList(12), "2023-02-01", 7.5, 0.0, "poster2.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2);
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, movies);

        // Act
        presenter.prepareSuccessView(responseModel);

        // Assert
        assertFalse(viewModel.hasError(), "Should clear error");
        assertEquals(movies, viewModel.getFilteredMovies());
        assertEquals(requestedGenreNames, viewModel.getSelectedGenreNames());
        assertNull(viewModel.getErrorMessage(), "Should not have error message when movies found");
    }

    @Test
    void testPrepareSuccessViewWithEmptyMovies() {
        // Arrange
        List<Integer> requestedGenres = Arrays.asList(27); // Horror
        List<String> requestedGenreNames = Arrays.asList("Horror");
        List<Movie> movies = Collections.emptyList();
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, movies);

        // Act
        presenter.prepareSuccessView(responseModel);

        // Assert
        // When an informational error message is set (even for empty results), hasError is true
        assertTrue(viewModel.hasError(), "Should have error flag set when informational message is displayed");
        assertTrue(viewModel.getFilteredMovies().isEmpty(), "Filtered movies should be empty");
        assertEquals(requestedGenreNames, viewModel.getSelectedGenreNames());
        assertNotNull(viewModel.getErrorMessage(), "Should have error message when no movies found");
        assertTrue(viewModel.getErrorMessage().contains("No movies found"), "Error message should indicate no movies found");
        assertTrue(viewModel.getErrorMessage().contains("Horror"), "Error message should contain genre name");
    }

    @Test
    void testPrepareSuccessViewWithMultipleGenresAndEmptyMovies() {
        // Arrange
        List<Integer> requestedGenres = Arrays.asList(28, 12, 35);
        List<String> requestedGenreNames = Arrays.asList("Action", "Adventure", "Comedy");
        List<Movie> movies = Collections.emptyList();
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, movies);

        // Act
        presenter.prepareSuccessView(responseModel);

        // Assert
        assertTrue(viewModel.getFilteredMovies().isEmpty());
        assertEquals(requestedGenreNames, viewModel.getSelectedGenreNames());
        assertNotNull(viewModel.getErrorMessage());
        assertTrue(viewModel.getErrorMessage().contains("Action, Adventure, Comedy"),
                "Error message should contain all genre names");
    }

    @Test
    void testPrepareSuccessViewClearsPreviousError() {
        // Arrange - Set an initial error
        viewModel.setErrorMessage("Previous error");
        assertTrue(viewModel.hasError());

        List<Integer> requestedGenres = Arrays.asList(28);
        List<String> requestedGenreNames = Arrays.asList("Action");
        Movie movie = new Movie("1", "Movie 1", "Plot",
                Arrays.asList(28), "2023-01-01", 8.0, 0.0, "poster.jpg");
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, Collections.singletonList(movie));

        // Act
        presenter.prepareSuccessView(responseModel);

        // Assert
        assertFalse(viewModel.hasError(), "Previous error should be cleared");
        assertNull(viewModel.getErrorMessage(), "Error message should be cleared");
    }

    @Test
    void testPrepareFailViewWithErrorMessage() {
        // Arrange
        String errorMessage = "Select at least one genre.";

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        assertTrue(viewModel.hasError(), "Should have error flag set");
        assertEquals(errorMessage, viewModel.getErrorMessage());
        assertTrue(viewModel.getFilteredMovies().isEmpty(), "Filtered movies should be cleared");
        assertTrue(viewModel.getSelectedGenreNames().isEmpty(), "Selected genre names should be cleared");
    }

    @Test
    void testPrepareFailViewClearsPreviousData() {
        // Arrange - Set some previous data
        Movie movie = new Movie("1", "Movie 1", "Plot",
                Arrays.asList(28), "2023-01-01", 8.0, 0.0, "poster.jpg");
        viewModel.setFilteredMovies(Collections.singletonList(movie));
        viewModel.setSelectedGenreNames(Arrays.asList("Action"));
        assertFalse(viewModel.getFilteredMovies().isEmpty());
        assertFalse(viewModel.getSelectedGenreNames().isEmpty());

        String errorMessage = "No movies to filter.";

        // Act
        presenter.prepareFailView(errorMessage);

        // Assert
        assertTrue(viewModel.hasError());
        assertEquals(errorMessage, viewModel.getErrorMessage());
        assertTrue(viewModel.getFilteredMovies().isEmpty(), "Previous movies should be cleared");
        assertTrue(viewModel.getSelectedGenreNames().isEmpty(), "Previous genre names should be cleared");
    }

    @Test
    void testPrepareFailViewWithNullErrorMessage() {
        // Act
        presenter.prepareFailView(null);

        // Assert
        assertNull(viewModel.getErrorMessage());
        assertTrue(viewModel.getFilteredMovies().isEmpty());
        assertTrue(viewModel.getSelectedGenreNames().isEmpty());
    }

    @Test
    void testPrepareSuccessViewWithSingleMovie() {
        // Arrange
        List<Integer> requestedGenres = Collections.singletonList(35);
        List<String> requestedGenreNames = Collections.singletonList("Comedy");
        Movie movie = new Movie("1", "Comedy Movie", "Funny plot",
                Collections.singletonList(35), "2023-01-01", 8.5, 0.0, "poster.jpg");
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, Collections.singletonList(movie));

        // Act
        presenter.prepareSuccessView(responseModel);

        // Assert
        assertEquals(1, viewModel.getFilteredMovies().size());
        assertEquals(movie, viewModel.getFilteredMovies().get(0));
        assertEquals(requestedGenreNames, viewModel.getSelectedGenreNames());
    }
}

