package use_case.filter_movies;
import interface_adapter.filter_movies.FilterMoviesViewModel;

import entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterMoviesViewModelTest {

    private FilterMoviesViewModel viewModel;
    private boolean propertyChangeFired;
    private PropertyChangeEvent capturedEvent;

    @BeforeEach
    void setUp() {
        viewModel = new FilterMoviesViewModel();
        propertyChangeFired = false;
        capturedEvent = null;
    }

    @Test
    void testInitialState() {
        // Assert
        assertTrue(viewModel.getFilteredMovies().isEmpty());
        assertTrue(viewModel.getSelectedGenreNames().isEmpty());
        assertNull(viewModel.getErrorMessage());
        assertFalse(viewModel.hasError());
        assertEquals(0, viewModel.getMovieCount());
    }

    @Test
    void testSetFilteredMovies() {
        // Arrange
        Movie movie1 = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1");
        Movie movie2 = new Movie("2", "Movie 2", "Plot", Arrays.asList(12), "2023-02-01", 8.0, 0.0, "poster2");
        List<Movie> movies = Arrays.asList(movie1, movie2);

        // Act
        viewModel.setFilteredMovies(movies);

        // Assert
        List<Movie> result = viewModel.getFilteredMovies();
        assertEquals(2, result.size());
        assertEquals(movie1, result.get(0));
        assertEquals(movie2, result.get(1));
    }

    @Test
    void testSetFilteredMoviesWithNull() {
        // Act
        viewModel.setFilteredMovies(null);

        // Assert
        assertTrue(viewModel.getFilteredMovies().isEmpty(), "Should default to empty list when null");
    }

    @Test
    void testSetFilteredMoviesPerformsDefensiveCopy() {
        // Arrange
        Movie movie1 = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1");
        List<Movie> movies = new java.util.ArrayList<>(Collections.singletonList(movie1));

        // Act
        viewModel.setFilteredMovies(movies);
        movies.add(new Movie("2", "Movie 2", "Plot", Arrays.asList(12), "2023-02-01", 8.0, 0.0, "poster2"));

        // Assert
        assertEquals(1, viewModel.getFilteredMovies().size(), "External modification should not affect view model");
    }

    @Test
    void testGetFilteredMoviesReturnsSameReference() {
        // Arrange
        Movie movie = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1");
        viewModel.setFilteredMovies(Collections.singletonList(movie));

        // Act
        List<Movie> result1 = viewModel.getFilteredMovies();
        List<Movie> result2 = viewModel.getFilteredMovies();

        // Assert - The getter returns the same reference (defensive copying happens in setter)
        assertSame(result1, result2, "Should return the same reference");
        assertEquals(movie, result1.get(0));
        assertEquals(movie, result2.get(0));
    }

    @Test
    void testSetSelectedGenreNames() {
        // Arrange
        List<String> genreNames = Arrays.asList("Action", "Adventure", "Comedy");

        // Act
        viewModel.setSelectedGenreNames(genreNames);

        // Assert
        List<String> result = viewModel.getSelectedGenreNames();
        assertEquals(3, result.size());
        assertEquals("Action", result.get(0));
        assertEquals("Adventure", result.get(1));
        assertEquals("Comedy", result.get(2));
    }

    @Test
    void testSetSelectedGenreNamesWithNull() {
        // Act
        viewModel.setSelectedGenreNames(null);

        // Assert
        assertTrue(viewModel.getSelectedGenreNames().isEmpty(), "Should default to empty list when null");
    }

    @Test
    void testSetSelectedGenreNamesPerformsDefensiveCopy() {
        // Arrange
        List<String> genreNames = new java.util.ArrayList<>(Arrays.asList("Action", "Adventure"));

        // Act
        viewModel.setSelectedGenreNames(genreNames);
        genreNames.add("Comedy");

        // Assert
        assertEquals(2, viewModel.getSelectedGenreNames().size(), "External modification should not affect view model");
    }

    @Test
    void testSetErrorMessage() {
        // Arrange
        String errorMessage = "An error occurred";

        // Act
        viewModel.setErrorMessage(errorMessage);

        // Assert
        assertEquals(errorMessage, viewModel.getErrorMessage());
        assertTrue(viewModel.hasError());
    }

    @Test
    void testSetErrorMessageWithEmptyString() {
        // Act
        viewModel.setErrorMessage("");

        // Assert
        assertEquals("", viewModel.getErrorMessage());
        assertFalse(viewModel.hasError(), "Empty string should not set error flag");
    }

    @Test
    void testSetErrorMessageWithNull() {
        // Act
        viewModel.setErrorMessage(null);

        // Assert
        assertNull(viewModel.getErrorMessage());
        assertFalse(viewModel.hasError());
    }

    @Test
    void testClearError() {
        // Arrange
        viewModel.setErrorMessage("Some error");
        assertTrue(viewModel.hasError());

        // Act
        viewModel.clearError();

        // Assert
        assertNull(viewModel.getErrorMessage());
        assertFalse(viewModel.hasError());
    }

    @Test
    void testClearErrorWhenNoError() {
        // Act
        viewModel.clearError();

        // Assert
        assertNull(viewModel.getErrorMessage());
        assertFalse(viewModel.hasError());
    }

    @Test
    void testGetMovieCount() {
        // Arrange
        Movie movie1 = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1");
        Movie movie2 = new Movie("2", "Movie 2", "Plot", Arrays.asList(12), "2023-02-01", 8.0, 0.0, "poster2");
        Movie movie3 = new Movie("3", "Movie 3", "Plot", Arrays.asList(35), "2023-03-01", 6.5, 0.0, "poster3");

        // Act & Assert
        assertEquals(0, viewModel.getMovieCount());

        viewModel.setFilteredMovies(Arrays.asList(movie1));
        assertEquals(1, viewModel.getMovieCount());

        viewModel.setFilteredMovies(Arrays.asList(movie1, movie2));
        assertEquals(2, viewModel.getMovieCount());

        viewModel.setFilteredMovies(Arrays.asList(movie1, movie2, movie3));
        assertEquals(3, viewModel.getMovieCount());

        viewModel.setFilteredMovies(Collections.emptyList());
        assertEquals(0, viewModel.getMovieCount());
    }

    @Test
    void testPropertyChangeListenerForFilteredMovies() {
        // Arrange
        PropertyChangeListener listener = evt -> {
            propertyChangeFired = true;
            capturedEvent = evt;
        };
        viewModel.addPropertyChangeListener(listener);

        // Act
        Movie movie = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1");
        viewModel.setFilteredMovies(Collections.singletonList(movie));

        // Assert
        assertTrue(propertyChangeFired, "Property change should be fired");
        assertNotNull(capturedEvent);
        assertEquals("filteredMovies", capturedEvent.getPropertyName());
    }

    @Test
    void testPropertyChangeListenerForErrorMessage() {
        // Arrange
        PropertyChangeListener listener = evt -> {
            propertyChangeFired = true;
            capturedEvent = evt;
        };
        viewModel.addPropertyChangeListener(listener);

        // Act
        viewModel.setErrorMessage("Test error");

        // Assert
        assertTrue(propertyChangeFired, "Property change should be fired");
        assertNotNull(capturedEvent);
        assertEquals("errorMessage", capturedEvent.getPropertyName());
    }

    @Test
    void testRemovePropertyChangeListener() {
        // Arrange
        PropertyChangeListener listener = evt -> {
            propertyChangeFired = true;
            capturedEvent = evt;
        };
        viewModel.addPropertyChangeListener(listener);
        viewModel.removePropertyChangeListener(listener);

        // Act
        viewModel.setErrorMessage("Test error");

        // Assert
        assertFalse(propertyChangeFired, "Property change should not be fired after listener removed");
    }

    @Test
    void testMultiplePropertyChangeListeners() {
        // Arrange
        final boolean[] listener1Fired = {false};
        final boolean[] listener2Fired = {false};

        PropertyChangeListener listener1 = evt -> listener1Fired[0] = true;
        PropertyChangeListener listener2 = evt -> listener2Fired[0] = true;

        viewModel.addPropertyChangeListener(listener1);
        viewModel.addPropertyChangeListener(listener2);

        // Act
        viewModel.setErrorMessage("Test error");

        // Assert
        assertTrue(listener1Fired[0], "First listener should be notified");
        assertTrue(listener2Fired[0], "Second listener should be notified");
    }

    @Test
    void testHasErrorFlag() {
        // Initial state
        assertFalse(viewModel.hasError());

        // Set error message
        viewModel.setErrorMessage("Error 1");
        assertTrue(viewModel.hasError());

        // Set empty error message
        viewModel.setErrorMessage("");
        assertFalse(viewModel.hasError());

        // Set null error message
        viewModel.setErrorMessage(null);
        assertFalse(viewModel.hasError());

        // Clear error
        viewModel.setErrorMessage("Error 2");
        viewModel.clearError();
        assertFalse(viewModel.hasError());
    }

    @Test
    void testPropertyChangeEventValuesForFilteredMovies() {
        // Arrange
        PropertyChangeListener listener = evt -> capturedEvent = evt;
        viewModel.addPropertyChangeListener(listener);

        // Act
        Movie movie = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1");
        List<Movie> newMovies = Collections.singletonList(movie);
        viewModel.setFilteredMovies(newMovies);

        // Assert
        assertNotNull(capturedEvent);
        assertTrue(capturedEvent.getOldValue() instanceof List);
        assertTrue(capturedEvent.getNewValue() instanceof List);
    }

    @Test
    void testPropertyChangeEventValuesForErrorMessage() {
        // Arrange
        PropertyChangeListener listener = evt -> capturedEvent = evt;
        viewModel.addPropertyChangeListener(listener);

        // Act
        String errorMessage = "Test error message";
        viewModel.setErrorMessage(errorMessage);

        // Assert
        assertNotNull(capturedEvent);
        assertNull(capturedEvent.getOldValue());
        assertEquals(errorMessage, capturedEvent.getNewValue());
    }
}

