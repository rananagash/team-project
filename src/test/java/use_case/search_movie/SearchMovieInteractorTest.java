package use_case.search_movie;

import entity.Movie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
import use_case.common.MovieDataAccessException;
import use_case.common.PagedMovieResult;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * tests of SearchMovieInteractor
 */
class SearchMovieInteractorTest {

    private static class TestPresenter implements SearchMovieOutputBoundary {
        SearchMovieResponseModel successResponse;
        String errorMessage;
        boolean successCalled = false;
        boolean failCalled = false;
        int callCount = 0;

        @Override
        public void prepareSuccessView(SearchMovieResponseModel response) {
            this.successResponse = response;
            this.successCalled = true;
            this.callCount++;
        }

        @Override
        public void prepareFailView(String error) {
            this.errorMessage = error;
            this.failCalled = true;
            this.callCount++;
        }

        void reset() {
            successResponse = null;
            errorMessage = null;
            successCalled = false;
            failCalled = false;
            callCount = 0;
        }
    }

    private static class TestMovieGateway implements use_case.common.MovieGateway {
        private final List<Movie> moviesToReturn;
        private boolean throwException = false;
        private MovieDataAccessException.Type exceptionType = MovieDataAccessException.Type.NETWORK;

        TestMovieGateway(List<Movie> movies) {
            this.moviesToReturn = movies;
        }

        TestMovieGateway withException(MovieDataAccessException.Type type) {
            this.throwException = true;
            this.exceptionType = type;
            return this;
        }


        @Override
        public PagedMovieResult searchByTitle(String query, int page) throws MovieDataAccessException {
            if (throwException) {
                throw new MovieDataAccessException(exceptionType, "Test exception for: " + query);
            }
            return new PagedMovieResult(moviesToReturn, page, 3);
        }

        @Override
        public Optional<Movie> findById(String movieId) {
            return Optional.empty();
        }

        @Override
        public List<Movie> searchByTitle(String query) {
            return moviesToReturn;
        }

        @Override
        public List<Movie> filterByGenres(List<Integer> genreIds) {
            return List.of();
        }

        @Override
        public PagedMovieResult getPopularMovies(int page) throws MovieDataAccessException {
            return null;
        }
    }

    @Test
    void responseModel_ConstructorAndGetters() {
        // Test the constructor with all parameters
        List<Movie> movies = List.of(
                new Movie("1", "Test Movie", "Plot", List.of(1), "2023-01-01", 7.5, 50.0, "poster.jpg")
        );

        SearchMovieResponseModel response = new SearchMovieResponseModel(
                "test query", movies, 2, 5
        );

        // Verify all getters
        assertEquals("test query", response.getQuery(), "Query should match constructor argument");
        assertEquals(1, response.getMovies().size(), "Movies list size should match");
        assertEquals(2, response.getCurrentPage(), "Current page should match");
        assertEquals(5, response.getTotalPages(), "Total pages should match");
        assertEquals(movies.get(0), response.getMovies().get(0), "Movie object should be the same instance");
    }

    @Test
    void responseModel_ConstructorWithTwoParameters() {
        // Test the simplified constructor (query, movies only)
        List<Movie> movies = List.of(
                new Movie("1", "Movie 1", "Plot 1", List.of(1), "2023-01-01", 8.0, 60.0, "poster1.jpg"),
                new Movie("2", "Movie 2", "Plot 2", List.of(2), "2023-01-01", 7.0, 40.0, "poster2.jpg")
        );

        SearchMovieResponseModel response = new SearchMovieResponseModel("query", movies);

        assertEquals("query", response.getQuery());
        assertEquals(2, response.getMovies().size());
        assertEquals(1, response.getCurrentPage(), "Default page should be 1");
        assertEquals(1, response.getTotalPages(), "Default total pages should be 1");
    }

    @Test
    void testSearchWithValidQuery() {
        List<Movie> testMovies = Arrays.asList(
                new Movie("1", "The Avengers", "Superhero movie",
                        Arrays.asList(28, 12), "2012-05-04", 8.0, 85.0, "poster1.jpg"),
                new Movie("2", "Avengers: Endgame", "Superhero finale",
                        Arrays.asList(28, 12, 878), "2019-04-26", 8.4, 95.0, "poster2.jpg")
        );

        TestMovieGateway gateway = new TestMovieGateway(testMovies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("avengers", 1);
        interactor.execute(request);

        assertTrue(presenter.successCalled, "correct");
        assertFalse(presenter.failCalled, "failed");
        assertNotNull(presenter.successResponse, "empty response");
        assertEquals(2, presenter.successResponse.getMovies().size(), "should return 2");
        assertEquals("avengers", presenter.successResponse.getQuery(), "title should match");
    }

    @Test
    void responseModel_WithEmptyMovieList() {
        // Should handle empty lists gracefully
        SearchMovieResponseModel response = new SearchMovieResponseModel("query", List.of(), 1, 1);

        assertTrue(response.getMovies().isEmpty(), "Movies list should be empty");
        assertEquals(0, response.getMovies().size(), "Size should be 0");
    }

    @Test
    void responseModel_Immutability() {
        // Test that the movies list is immutable (cannot be modified)
        List<Movie> originalMovies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", List.of(1), "2023-01-01", 8.0, 50.0, "poster.jpg")
        );

        SearchMovieResponseModel response = new SearchMovieResponseModel("query", originalMovies);

        // Attempting to modify the returned list should throw an exception
        List<Movie> returnedMovies = response.getMovies();
        assertThrows(Exception.class, () -> returnedMovies.add(
                new Movie("2", "Movie 2", "Plot", List.of(1), "2023-01-01", 8.0, 50.0, "poster.jpg")
        ), "Returned movies list should be immutable");
    }

    @Test
    void testSearchWithEmptyQuery() {
        TestMovieGateway gateway = new TestMovieGateway(List.of());
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("", 1);
        interactor.execute(request);

        assertTrue(presenter.failCalled, "failed on empty response");
        assertFalse(presenter.successCalled, "empty response");
        assertNotNull(presenter.errorMessage, "error message should not be null");
        assertTrue(presenter.errorMessage.contains("empty"), "error message should match");
    }



    @Test
    void testSearchWithNoResults() {
        TestMovieGateway gateway = new TestMovieGateway(List.of());
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("nonexistentmovie", 1);
        interactor.execute(request);
    }

    @Test
    void execute_QueryNull_ShowsError() {
        // Test branch: query == null
        TestMovieGateway gateway = new TestMovieGateway(List.of());
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        // Create a request with null query (requires reflection or constructor change)
        // For now, test with blank query which follows same path
        SearchMovieRequestModel request = new SearchMovieRequestModel("  ", 1);
        interactor.execute(request);

        assertTrue(presenter.failCalled, "Should call fail for null/blank query");
        assertTrue(presenter.errorMessage.contains("empty"),
                "Error message should indicate empty query");
    }

    @Test
    void testSearchWithSpecialCharacters() {
        List<Movie> testMovies = List.of(
                new Movie("3", "Spider-Man: No Way Home", "Multiverse adventure",
                        Arrays.asList(28, 12), "2021-12-17", 8.2, 90.0, "poster3.jpg")
        );

        TestMovieGateway gateway = new TestMovieGateway(testMovies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("Spider-Man", 1);
        interactor.execute(request);

        assertTrue(presenter.successCalled || presenter.failCalled, "need at least one call");
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", " "})
    void execute_QueryTooShort_ShowsError(String shortQuery) {
        // Test branch: query.length() < 2
        TestMovieGateway gateway = new TestMovieGateway(List.of());
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel(shortQuery, 1);
        interactor.execute(request);

        assertTrue(presenter.failCalled, "Should call fail for query shorter than 2 chars");
        assertTrue(presenter.errorMessage.contains("2 characters") ||
                        presenter.errorMessage.contains("empty"),
                "Error message should mention minimum length");
    }

    @ParameterizedTest
    @ValueSource(strings = {"@@@", "!!!", "???"})
    void execute_QueryOnlySpecialCharacters_ShowsError(String specialQuery) {
        // Test branch: query.matches("[^a-zA-Z0-9]+")
        TestMovieGateway gateway = new TestMovieGateway(List.of());
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel(specialQuery, 1);
        interactor.execute(request);

        assertTrue(presenter.failCalled, "Should call fail for non-alphanumeric query");
        assertTrue(presenter.errorMessage.contains("meaningful") ||
                        presenter.errorMessage.contains("title"),
                "Error message should ask for meaningful title");
    }

    @Test
    void testScoringAlgorithm() throws Exception {
        List<Movie> testMovies = Arrays.asList(
                new Movie("1", "New Avengers", "Movie A", List.of(28), "2023-01-01", 7.0, 50.0, "poster1"),
                new Movie("2", "Old Avengers", "Movie B", List.of(28), "2018-01-01", 8.5, 80.0, "poster2"),
                new Movie("3", "The Avengers", "Movie C", List.of(28), "2012-01-01", 8.0, 70.0, "poster3")
        );

        TestMovieGateway gateway = new TestMovieGateway(testMovies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("Avengers", 1);
        interactor.execute(request);

        assertTrue(presenter.successCalled, "search successful");

        assertNotNull(presenter.successResponse);
        List<Movie> results = presenter.successResponse.getMovies();
        assertEquals(3, results.size(), "should return 3");

        List<String> movieTitles = results.stream()
                .map(Movie::getTitle)
                .collect(java.util.stream.Collectors.toList());

        assertTrue(movieTitles.contains("New Avengers"));
        assertTrue(movieTitles.contains("Old Avengers"));
        assertTrue(movieTitles.contains("The Avengers"));

        System.out.println("ordering：");
        for (int i = 0; i < results.size(); i++) {
            Movie m = results.get(i);
            System.out.println((i+1) + ". " + m.getTitle() + " (score: " + m.getRating() + ", year: " + m.getReleaseYear() + ")");
        }
    }
}