package use_case_filter_movies;

import entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.common.MovieGateway;
import use_case.filter_movies.FilterMoviesInteractor;
import use_case.filter_movies.FilterMoviesOutputBoundary;
import use_case.filter_movies.FilterMoviesRequestModel;
import use_case.filter_movies.FilterMoviesResponseModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterMoviesInteractorTest {

    private FilterMoviesInteractor interactor;
    private MockMovieGateway mockMovieGateway;
    private MockFilterMoviesOutputBoundary mockPresenter;

    @BeforeEach
    void setUp() {
        mockMovieGateway = new MockMovieGateway();
        mockPresenter = new MockFilterMoviesOutputBoundary();
        interactor = new FilterMoviesInteractor(mockMovieGateway, mockPresenter);
    }

    @Test
    void testExecuteWithNullGenreIds() {
        // Arrange
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(null);

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.failViewCalled, "Fail view should be called");
        assertEquals("Select at least one genre.", mockPresenter.errorMessage);
        assertFalse(mockPresenter.successViewCalled, "Success view should not be called");
        assertNull(mockPresenter.responseModel, "Response model should be null on failure");
    }

    @Test
    void testExecuteWithEmptyGenreIds() {
        // Arrange
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(Collections.emptyList());

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.failViewCalled, "Fail view should be called");
        assertEquals("Select at least one genre.", mockPresenter.errorMessage);
        assertFalse(mockPresenter.successViewCalled, "Success view should not be called");
    }

    @Test
    void testExecuteWithValidGenreIds() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12); // Action, Adventure
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);

        Movie movie1 = new Movie("1", "Test Movie 1", "Plot 1",
                Arrays.asList(28, 12), "2023-01-01", 8.5, "poster1.jpg");
        Movie movie2 = new Movie("2", "Test Movie 2", "Plot 2",
                Arrays.asList(28), "2023-02-01", 7.5, "poster2.jpg");
        List<Movie> expectedMovies = Arrays.asList(movie1, movie2);
        mockMovieGateway.setMoviesToReturn(expectedMovies);

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.successViewCalled, "Success view should be called");
        assertFalse(mockPresenter.failViewCalled, "Fail view should not be called");
        assertNotNull(mockPresenter.responseModel, "Response model should not be null");
        assertEquals(genreIds, mockPresenter.responseModel.getRequestedGenres());
        assertEquals(expectedMovies, mockPresenter.responseModel.getMovies());
        assertEquals(Arrays.asList("Action", "Adventure"), mockPresenter.responseModel.getRequestedGenreNames());
        assertEquals(genreIds, mockMovieGateway.getLastGenreIdsCalled());
    }

    @Test
    void testExecuteWithSingleGenreId() {
        // Arrange
        List<Integer> genreIds = Collections.singletonList(35); // Comedy
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);

        Movie movie = new Movie("3", "Comedy Movie", "Funny plot",
                Collections.singletonList(35), "2023-03-01", 9.0, "poster3.jpg");
        mockMovieGateway.setMoviesToReturn(Collections.singletonList(movie));

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.successViewCalled);
        assertEquals(Collections.singletonList(35), mockPresenter.responseModel.getRequestedGenres());
        assertEquals(Collections.singletonList("Comedy"), mockPresenter.responseModel.getRequestedGenreNames());
        assertEquals(1, mockPresenter.responseModel.getMovies().size());
        assertEquals(movie, mockPresenter.responseModel.getMovies().get(0));
    }

    @Test
    void testExecuteWithMultipleGenreIds() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12, 16); // Action, Adventure, Animation
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);

        List<Movie> movies = new ArrayList<>();
        mockMovieGateway.setMoviesToReturn(movies);

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.successViewCalled);
        assertEquals(genreIds, mockPresenter.responseModel.getRequestedGenres());
        assertEquals(Arrays.asList("Action", "Adventure", "Animation"),
                mockPresenter.responseModel.getRequestedGenreNames());
        assertEquals(movies, mockPresenter.responseModel.getMovies());
        assertEquals(genreIds, mockMovieGateway.getLastGenreIdsCalled());
    }

    @Test
    void testExecuteWithUnknownGenreId() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 999); // Action and unknown genre
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);

        Movie movie = new Movie("1", "Test Movie", "Plot",
                Arrays.asList(28), "2023-01-01", 8.0, "poster.jpg");
        mockMovieGateway.setMoviesToReturn(Collections.singletonList(movie));

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.successViewCalled);
        List<String> genreNames = mockPresenter.responseModel.getRequestedGenreNames();
        assertEquals(2, genreNames.size());
        assertEquals("Action", genreNames.get(0));
        assertEquals("Unknown Genre (999)", genreNames.get(1));
    }

    @Test
    void testExecuteWithEmptyMovieList() {
        // Arrange
        List<Integer> genreIds = Collections.singletonList(27); // Horror
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);
        mockMovieGateway.setMoviesToReturn(Collections.emptyList());

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.successViewCalled);
        assertTrue(mockPresenter.responseModel.getMovies().isEmpty());
        assertEquals(Collections.singletonList("Horror"), mockPresenter.responseModel.getRequestedGenreNames());
    }

    @Test
    void testGetGenreName() {
        // Test valid genre IDs
        assertEquals("Action", FilterMoviesInteractor.getGenreName(28));
        assertEquals("Comedy", FilterMoviesInteractor.getGenreName(35));
        assertEquals("Horror", FilterMoviesInteractor.getGenreName(27));
        assertEquals("Science Fiction", FilterMoviesInteractor.getGenreName(878));
        assertEquals("Adventure", FilterMoviesInteractor.getGenreName(12));
        assertEquals("Drama", FilterMoviesInteractor.getGenreName(18));

        // Test invalid genre ID
        assertNull(FilterMoviesInteractor.getGenreName(999));
        assertNull(FilterMoviesInteractor.getGenreName(-1));
    }

    @Test
    void testGetAllGenres() {
        // Act
        var allGenres = FilterMoviesInteractor.getAllGenres();

        // Assert
        assertNotNull(allGenres);
        assertFalse(allGenres.isEmpty());
        assertEquals(19, allGenres.size(), "Should have 19 genres");
        assertEquals("Action", allGenres.get(28));
        assertEquals("Comedy", allGenres.get(35));
        assertEquals("Horror", allGenres.get(27));
        assertEquals("Science Fiction", allGenres.get(878));
    }

    @Test
    void testGetAllGenresReturnsCopy() {
        // Act
        var allGenres1 = FilterMoviesInteractor.getAllGenres();
        var allGenres2 = FilterMoviesInteractor.getAllGenres();

        // Assert - should be different instances
        assertNotSame(allGenres1, allGenres2, "Should return a copy, not the same instance");
    }

    // Mock classes for testing
    private static class MockMovieGateway implements MovieGateway {
        private List<Movie> moviesToReturn = new ArrayList<>();
        private List<Integer> lastGenreIdsCalled;

        public void setMoviesToReturn(List<Movie> movies) {
            this.moviesToReturn = movies != null ? new ArrayList<>(movies) : new ArrayList<>();
        }

        public List<Integer> getLastGenreIdsCalled() {
            return lastGenreIdsCalled;
        }

        @Override
        public java.util.Optional<Movie> findById(String movieId) {
            return java.util.Optional.empty();
        }

        @Override
        public List<Movie> searchByTitle(String query) {
            return Collections.emptyList();
        }

        @Override
        public List<Movie> filterByGenres(List<Integer> genreIds) {
            this.lastGenreIdsCalled = genreIds != null ? new ArrayList<>(genreIds) : null;
            return new ArrayList<>(moviesToReturn);
        }
    }

    private static class MockFilterMoviesOutputBoundary implements FilterMoviesOutputBoundary {
        boolean successViewCalled = false;
        boolean failViewCalled = false;
        FilterMoviesResponseModel responseModel;
        String errorMessage;

        @Override
        public void prepareSuccessView(FilterMoviesResponseModel responseModel) {
            this.successViewCalled = true;
            this.responseModel = responseModel;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failViewCalled = true;
            this.errorMessage = errorMessage;
        }
    }
}

