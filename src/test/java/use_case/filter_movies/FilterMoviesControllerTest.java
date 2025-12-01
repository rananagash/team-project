package use_case.filter_movies;

import entity.Movie;
import interface_adapter.filter_movies.FilterMoviesController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.filter_movies.FilterMoviesInputBoundary;
import use_case.filter_movies.FilterMoviesRequestModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterMoviesControllerTest {

    private FilterMoviesController controller;
    private MockFilterMoviesInputBoundary mockInteractor;

    @BeforeEach
    void setUp() {
        mockInteractor = new MockFilterMoviesInputBoundary();
        controller = new FilterMoviesController(mockInteractor);
    }

    @Test
    void testFilterByGenresWithValidInput() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);
        Movie movie1 = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1");
        Movie movie2 = new Movie("2", "Movie 2", "Plot", Arrays.asList(12), "2023-02-01", 8.0, 0.0, "poster2");
        List<Movie> moviesToFilter = Arrays.asList(movie1, movie2);

        // Act
        controller.filterByGenres(genreIds, moviesToFilter);

        // Assert
        assertEquals(1, mockInteractor.getExecuteCallCount(), "Execute should be called once");
        assertNotNull(mockInteractor.getLastRequestModel(), "Request model should not be null");
        assertEquals(genreIds, mockInteractor.getLastRequestModel().getGenreIds());
        assertEquals(moviesToFilter, mockInteractor.getLastRequestModel().getMoviesToFilter());
    }

    @Test
    void testFilterByGenresWithSingleGenre() {
        // Arrange
        List<Integer> genreIds = Collections.singletonList(35);
        Movie movie = new Movie("1", "Movie 1", "Plot", Arrays.asList(35), "2023-01-01", 7.5, 0.0, "poster");
        List<Movie> moviesToFilter = Collections.singletonList(movie);

        // Act
        controller.filterByGenres(genreIds, moviesToFilter);

        // Assert
        assertEquals(1, mockInteractor.getExecuteCallCount());
        assertEquals(genreIds, mockInteractor.getLastRequestModel().getGenreIds());
        assertEquals(moviesToFilter, mockInteractor.getLastRequestModel().getMoviesToFilter());
    }

    @Test
    void testFilterByGenresWithEmptyGenreIds() {
        // Arrange
        List<Integer> genreIds = Collections.emptyList();
        Movie movie = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster");
        List<Movie> moviesToFilter = Collections.singletonList(movie);

        // Act
        controller.filterByGenres(genreIds, moviesToFilter);

        // Assert
        assertEquals(1, mockInteractor.getExecuteCallCount());
        assertEquals(genreIds, mockInteractor.getLastRequestModel().getGenreIds());
    }

    @Test
    void testFilterByGenresWithEmptyMovies() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);
        List<Movie> moviesToFilter = Collections.emptyList();

        // Act
        controller.filterByGenres(genreIds, moviesToFilter);

        // Assert
        assertEquals(1, mockInteractor.getExecuteCallCount());
        assertEquals(moviesToFilter, mockInteractor.getLastRequestModel().getMoviesToFilter());
    }

    @Test
    void testFilterByGenresWithNullGenreIds() {
        // Arrange
        Movie movie = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster");
        List<Movie> moviesToFilter = Collections.singletonList(movie);

        // Act
        controller.filterByGenres(null, moviesToFilter);

        // Assert
        assertEquals(1, mockInteractor.getExecuteCallCount());
        assertNull(mockInteractor.getLastRequestModel().getGenreIds());
        assertEquals(moviesToFilter, mockInteractor.getLastRequestModel().getMoviesToFilter());
    }

    @Test
    void testFilterByGenresWithNullMovies() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);

        // Act
        controller.filterByGenres(genreIds, null);

        // Assert
        assertEquals(1, mockInteractor.getExecuteCallCount());
        assertEquals(genreIds, mockInteractor.getLastRequestModel().getGenreIds());
        assertNull(mockInteractor.getLastRequestModel().getMoviesToFilter());
    }

    @Test
    void testFilterByGenresWithMultipleMovies() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12, 35);
        Movie movie1 = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1");
        Movie movie2 = new Movie("2", "Movie 2", "Plot", Arrays.asList(12), "2023-02-01", 8.0, 0.0, "poster2");
        Movie movie3 = new Movie("3", "Movie 3", "Plot", Arrays.asList(35), "2023-03-01", 6.5, 0.0, "poster3");
        List<Movie> moviesToFilter = Arrays.asList(movie1, movie2, movie3);

        // Act
        controller.filterByGenres(genreIds, moviesToFilter);

        // Assert
        assertEquals(1, mockInteractor.getExecuteCallCount());
        assertEquals(genreIds, mockInteractor.getLastRequestModel().getGenreIds());
        assertEquals(3, mockInteractor.getLastRequestModel().getMoviesToFilter().size());
    }

    @Test
    void testFilterByGenresCalledMultipleTimes() {
        // Arrange
        List<Integer> genreIds1 = Arrays.asList(28);
        List<Integer> genreIds2 = Arrays.asList(12);
        Movie movie = new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster");
        List<Movie> moviesToFilter = Collections.singletonList(movie);

        // Act
        controller.filterByGenres(genreIds1, moviesToFilter);
        controller.filterByGenres(genreIds2, moviesToFilter);

        // Assert
        assertEquals(2, mockInteractor.getExecuteCallCount(), "Execute should be called twice");
        assertEquals(genreIds2, mockInteractor.getLastRequestModel().getGenreIds(), "Last call should have genreIds2");
    }

    // Mock class for testing
    private static class MockFilterMoviesInputBoundary implements FilterMoviesInputBoundary {
        private int executeCallCount = 0;
        private FilterMoviesRequestModel lastRequestModel;

        @Override
        public void execute(FilterMoviesRequestModel requestModel) {
            executeCallCount++;
            this.lastRequestModel = requestModel;
        }

        public int getExecuteCallCount() {
            return executeCallCount;
        }

        public FilterMoviesRequestModel getLastRequestModel() {
            return lastRequestModel;
        }
    }
}

