package use_case.filter_movies;

import entity.Movie;
import common.GenreUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.common.MovieGateway;

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
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster")
        );
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(null, movies);

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
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster")
        );
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(Collections.emptyList(), movies);

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.failViewCalled, "Fail view should be called");
        assertEquals("Select at least one genre.", mockPresenter.errorMessage);
        assertFalse(mockPresenter.successViewCalled, "Success view should not be called");
    }

    @Test
    void testExecuteWithNullMovies() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, null);

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.failViewCalled, "Fail view should be called");
        assertEquals("No movies to filter.", mockPresenter.errorMessage);
        assertFalse(mockPresenter.successViewCalled, "Success view should not be called");
    }

    @Test
    void testExecuteWithEmptyMovies() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, Collections.emptyList());

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.failViewCalled, "Fail view should be called");
        assertEquals("No movies to filter.", mockPresenter.errorMessage);
        assertFalse(mockPresenter.successViewCalled, "Success view should not be called");
    }

    @Test
    void testExecuteWithValidGenreIds() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12); // Action, Adventure
        Movie movie1 = new Movie("1", "Test Movie 1", "Plot 1",
                Arrays.asList(28, 12), "2023-01-01", 8.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Test Movie 2", "Plot 2",
                Arrays.asList(28), "2023-02-01", 7.5, 0.0, "poster2.jpg");
        Movie movie3 = new Movie("3", "Test Movie 3", "Plot 3",
                Arrays.asList(35), "2023-03-01", 6.5, 0.0, "poster3.jpg");
        List<Movie> moviesToFilter = Arrays.asList(movie1, movie2, movie3);
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, moviesToFilter);

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.successViewCalled, "Success view should be called");
        assertFalse(mockPresenter.failViewCalled, "Fail view should not be called");
        assertNotNull(mockPresenter.responseModel, "Response model should not be null");
        assertEquals(genreIds, mockPresenter.responseModel.getRequestedGenres());
        // Should only contain movies with genres 28 or 12 (movie1 and movie2, not movie3)
        assertEquals(2, mockPresenter.responseModel.getMovies().size());
        assertTrue(mockPresenter.responseModel.getMovies().contains(movie1));
        assertTrue(mockPresenter.responseModel.getMovies().contains(movie2));
        assertFalse(mockPresenter.responseModel.getMovies().contains(movie3));
        assertEquals(Arrays.asList("Action", "Adventure"), mockPresenter.responseModel.getRequestedGenreNames());
    }

    @Test
    void testExecuteWithSingleGenreId() {
        // Arrange
        List<Integer> genreIds = Collections.singletonList(35); // Comedy
        Movie movie = new Movie("3", "Comedy Movie", "Funny plot",
                Collections.singletonList(35), "2023-03-01", 9.0, 0.0, "poster3.jpg");
        Movie otherMovie = new Movie("4", "Action Movie", "Action plot",
                Collections.singletonList(28), "2023-04-01", 8.0, 0.0, "poster4.jpg");
        List<Movie> moviesToFilter = Arrays.asList(movie, otherMovie);
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, moviesToFilter);

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
        Movie movie1 = new Movie("1", "Action Movie", "Plot",
                Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Adventure Movie", "Plot",
                Arrays.asList(12), "2023-02-01", 8.0, 0.0, "poster2.jpg");
        Movie movie3 = new Movie("3", "Animation Movie", "Plot",
                Arrays.asList(16), "2023-03-01", 8.5, 0.0, "poster3.jpg");
        Movie movie4 = new Movie("4", "Other Movie", "Plot",
                Arrays.asList(35), "2023-04-01", 6.5, 0.0, "poster4.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2, movie3, movie4);
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, movies);

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.successViewCalled);
        assertFalse(mockPresenter.failViewCalled);
        assertEquals(genreIds, mockPresenter.responseModel.getRequestedGenres());
        assertEquals(Arrays.asList("Action", "Adventure", "Animation"),
                mockPresenter.responseModel.getRequestedGenreNames());
        // Should contain movies 1, 2, 3 (with genres 28, 12, 16) but not movie4 (genre 35)
        assertEquals(3, mockPresenter.responseModel.getMovies().size());
        assertTrue(mockPresenter.responseModel.getMovies().contains(movie1));
        assertTrue(mockPresenter.responseModel.getMovies().contains(movie2));
        assertTrue(mockPresenter.responseModel.getMovies().contains(movie3));
        assertFalse(mockPresenter.responseModel.getMovies().contains(movie4));
    }

    @Test
    void testExecuteWithUnknownGenreId() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 999); // Action and unknown genre
        Movie movie = new Movie("1", "Test Movie", "Plot",
                Arrays.asList(28), "2023-01-01", 8.0, 0.0, "poster.jpg");
        List<Movie> moviesToFilter = Collections.singletonList(movie);
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, moviesToFilter);

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.successViewCalled);
        List<String> genreNames = mockPresenter.responseModel.getRequestedGenreNames();
        assertEquals(2, genreNames.size());
        assertEquals("Action", genreNames.get(0));
        assertEquals("Unknown Genre (999)", genreNames.get(1));
        // Movie should be included because it has genre 28
        assertEquals(1, mockPresenter.responseModel.getMovies().size());
        assertEquals(movie, mockPresenter.responseModel.getMovies().get(0));
    }

    @Test
    void testExecuteWithEmptyMovieList() {
        // Arrange
        List<Integer> genreIds = Collections.singletonList(27); // Horror
        // Movies that don't match the Horror genre (27)
        Movie movie1 = new Movie("1", "Action Movie", "Plot",
                Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Comedy Movie", "Plot",
                Arrays.asList(35), "2023-02-01", 8.0, 0.0, "poster2.jpg");
        List<Movie> moviesToFilter = Arrays.asList(movie1, movie2);
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, moviesToFilter);

        // Act
        interactor.execute(requestModel);

        // Assert
        assertTrue(mockPresenter.successViewCalled, "Success view should be called even with empty filtered results");
        assertFalse(mockPresenter.failViewCalled, "Fail view should not be called");
        assertTrue(mockPresenter.responseModel.getMovies().isEmpty(), "Filtered movies should be empty");
        assertEquals(Collections.singletonList("Horror"), mockPresenter.responseModel.getRequestedGenreNames());
    }

    @Test
    void testGetGenreName() {
        // Test valid genre IDs
        assertEquals("Action", GenreUtils.getGenreName(28));
        assertEquals("Comedy", GenreUtils.getGenreName(35));
        assertEquals("Horror", GenreUtils.getGenreName(27));
        assertEquals("Science Fiction", GenreUtils.getGenreName(878));
        assertEquals("Adventure", GenreUtils.getGenreName(12));
        assertEquals("Drama", GenreUtils.getGenreName(18));

        // Test invalid genre ID
        assertNull(GenreUtils.getGenreName(999));
        assertNull(GenreUtils.getGenreName(-1));
    }

    @Test
    void testGetAllGenres() {
        // Act
        var allGenres = GenreUtils.getAllGenres();

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
        var allGenres1 = GenreUtils.getAllGenres();
        var allGenres2 = GenreUtils.getAllGenres();

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
        public use_case.common.PagedMovieResult searchByTitle(String query, int page)
                throws use_case.common.MovieDataAccessException {
            return new use_case.common.PagedMovieResult(Collections.emptyList(), page, 1);
        }

        @Override
        public List<Movie> filterByGenres(List<Integer> genreIds) {
            this.lastGenreIdsCalled = genreIds != null ? new ArrayList<>(genreIds) : null;
            return new ArrayList<>(moviesToReturn);
        }

        @Override
        public use_case.common.PagedMovieResult getPopularMovies(int page)
                throws use_case.common.MovieDataAccessException {
            return new use_case.common.PagedMovieResult(Collections.emptyList(), page, 1);
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

