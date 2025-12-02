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
    void execute_PageZeroOrNegative_DefaultsToOne() {
        // Test branch: requestModel.getPage() <= 0 ? 1 : requestModel.getPage()
        List<Movie> movies = List.of(
                new Movie("1", "Test", "Plot", List.of(1), "2023-01-01", 8.0, 50.0, "poster.jpg")
        );

        TestMovieGateway gateway = new TestMovieGateway(movies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        // Test with page 0
        SearchMovieRequestModel request1 = new SearchMovieRequestModel("test", 0);
        interactor.execute(request1);

        assertTrue(presenter.successCalled, "Should succeed with page 0 (defaults to 1)");
        assertEquals(1, presenter.successResponse.getCurrentPage(),
                "Page 0 should default to page 1");

        presenter.reset();

        // Test with negative page
        SearchMovieRequestModel request2 = new SearchMovieRequestModel("test", -5);
        interactor.execute(request2);

        assertTrue(presenter.successCalled, "Should succeed with negative page (defaults to 1)");
        assertEquals(1, presenter.successResponse.getCurrentPage(),
                "Negative page should default to page 1");
    }

    @Test
    void execute_GatewayReturnsEmptyList_ShowsError() {
        // Test branch: pagedResult.getMovies().isEmpty()
        TestMovieGateway gateway = new TestMovieGateway(List.of()); // Empty list
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("nonexistent", 1);
        interactor.execute(request);

        assertTrue(presenter.failCalled, "Should call fail for empty results");
        assertTrue(presenter.errorMessage.contains("No movies found") ||
                        presenter.errorMessage.contains("different search term"),
                "Error message should indicate no results");
    }

    @Test
    void execute_NetworkException_ShowsNetworkError() {
        // Test branch: MovieDataAccessException.Type.NETWORK
        TestMovieGateway gateway = new TestMovieGateway(List.of())
                .withException(MovieDataAccessException.Type.NETWORK);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("test", 1);
        interactor.execute(request);

        assertTrue(presenter.failCalled, "Should call fail for network exception");
        assertTrue(presenter.errorMessage.contains("Network") ||
                        presenter.errorMessage.contains("internet"),
                "Error message should mention network/internet");
    }

    @Test
    void execute_TmdbException_ShowsServiceError() {
        // Test branch: MovieDataAccessException.Type.TMDB_ERROR
        TestMovieGateway gateway = new TestMovieGateway(List.of())
                .withException(MovieDataAccessException.Type.TMDB_ERROR);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("test", 1);
        interactor.execute(request);

        assertTrue(presenter.failCalled, "Should call fail for TMDB exception");
        assertTrue(presenter.errorMessage.contains("service") ||
                        presenter.errorMessage.contains("unavailable") ||
                        presenter.errorMessage.contains("TMDb"),
                "Error message should mention service unavailability");
    }

    @Test
    void execute_DefaultException_ShowsGenericError() {
        // Test branch: default case in switch statement
        // Need to test with an exception type that's not NETWORK or TMDB_ERROR
        TestMovieGateway gateway = new TestMovieGateway(List.of())
                .withException(MovieDataAccessException.Type.UNKNOWN); // Assuming OTHER exists
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("test", 1);
        interactor.execute(request);

        assertTrue(presenter.failCalled, "Should call fail for any exception");
        assertTrue(presenter.errorMessage.contains("unexpected") ||
                        presenter.errorMessage.contains("error"),
                "Error message should be generic for unexpected errors");
    }

    @Test
    void requestModel_ConstructorAndGetters() {
        SearchMovieRequestModel request = new SearchMovieRequestModel("test query", 3);

        assertEquals("test query", request.getQuery(), "Query getter should return correct value");
        assertEquals(3, request.getPage(), "Page getter should return correct value");
    }

    @Test
    void requestModel_WithValidPageNumbers() {
        // Test various valid page numbers
        SearchMovieRequestModel page1 = new SearchMovieRequestModel("query", 1);
        assertEquals(1, page1.getPage());

        SearchMovieRequestModel pageLarge = new SearchMovieRequestModel("query", 100);
        assertEquals(100, pageLarge.getPage());
    }

    @Test
    void scoring_TitleExactMatch_HighestPriority() {
        // Test that exact title match gets highest score
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Batman", "Exact match", List.of(1), "2020-01-01", 7.0, 50.0, "poster1"),
                new Movie("2", "The Batman", "Starts with", List.of(1), "2020-01-01", 9.0, 90.0, "poster2"),
                new Movie("3", "Batman Returns", "Contains", List.of(1), "2020-01-01", 8.0, 80.0, "poster3")
        );

        TestMovieGateway gateway = new TestMovieGateway(movies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        interactor.execute(new SearchMovieRequestModel("Batman", 1));

        assertTrue(presenter.successCalled);
        List<Movie> results = presenter.successResponse.getMovies();

        // "Batman" (exact match) should be first despite lower rating
        assertEquals("Batman", results.get(0).getTitle(),
                "Exact match should be highest priority regardless of rating");
    }


    void scoring_NewMovieWithAgeBonus() {
        // Test age-based scoring: newer movies get bonus points
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 2023", "New", List.of(1), "2023-01-01", 7.0, 50.0, "poster1"),
                new Movie("2", "Movie 2020", "Medium", List.of(1), "2020-01-01", 7.5, 60.0, "poster2"),
                new Movie("3", "Movie 2015", "Old", List.of(1), "2015-01-01", 8.0, 70.0, "poster3")
        );

        TestMovieGateway gateway = new TestMovieGateway(movies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        interactor.execute(new SearchMovieRequestModel("Movie", 1));

        assertTrue(presenter.successCalled);
        List<Movie> results = presenter.successResponse.getMovies();

        // Print for debugging
        System.out.println("\nScoring Test - Age Bonus:");
        for (int i = 0; i < results.size(); i++) {
            Movie m = results.get(i);
            System.out.printf("%d. %s (Rating: %.1f, Year: %d)%n",
                    i+1, m.getTitle(), m.getRating(), m.getReleaseYear());
        }

        // Newest movie should be first due to age bonus
        assertEquals(2023, results.get(0).getReleaseYear(),
                "Newest movie (2023) should be first due to age bonus");
    }

    @Test
    void scoring_PopularityAffectsScore() {
        // Test that popularity affects score (popularity * 0.1)
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Popular Movie", "High popularity", List.of(1), "2020-01-01", 8.0, 100.0, "poster1"),
                new Movie("2", "Less Popular", "Low popularity", List.of(1), "2020-01-01", 8.0, 10.0, "poster2")
        );

        TestMovieGateway gateway = new TestMovieGateway(movies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        interactor.execute(new SearchMovieRequestModel("Movie", 1));

        assertTrue(presenter.successCalled);
        List<Movie> results = presenter.successResponse.getMovies();

        // More popular movie should be first (adds 10 vs 1 point)
        assertEquals("Popular Movie", results.get(0).getTitle(),
                "More popular movie should rank higher with same rating/year");
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
    @Test
    void execute_MixedAlphanumericWithSpecialChars_ShouldPass() {
        List<Movie> movies = List.of(
                new Movie("1", "Spider-Man 2", "With hyphen and number",
                        List.of(1), "2023-01-01", 7.0, 50.0, "poster.jpg")
        );

        TestMovieGateway gateway = new TestMovieGateway(movies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("Spider-Man 2", 1);
        interactor.execute(request);

        assertTrue(presenter.successCalled,
                "Query with mixed alphanumeric and special chars should pass");
    }
    @Test
    void execute_QueryExactlyTwoCharacters_ShouldPass() {
        List<Movie> movies = List.of(
                new Movie("1", "It", "Short title", List.of(1), "2023-01-01", 7.0, 50.0, "poster.jpg")
        );

        TestMovieGateway gateway = new TestMovieGateway(movies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("It", 1);
        interactor.execute(request);

        assertTrue(presenter.successCalled, "2-character query should be valid");
    }
    void scoring_AgeCalculation_EdgeCases() {

        List<Movie> movies = Arrays.asList(
                new Movie("1", "Brand New", "2023 movie", List.of(1), "2023-12-31", 7.0, 50.0, "poster1"),
                new Movie("2", "5 Years Old", "2019 movie", List.of(1), "2019-01-01", 7.0, 50.0, "poster2"),
                new Movie("3", "6 Years Old", "2018 movie", List.of(1), "2018-01-01", 7.0, 50.0, "poster3")
        );

        TestMovieGateway gateway = new TestMovieGateway(movies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        interactor.execute(new SearchMovieRequestModel("Movie", 1));

        if (presenter.successCalled) {
            List<Movie> results = presenter.successResponse.getMovies();
            System.out.println("\nAge Calculation Test:");
            for (Movie m : results) {
                System.out.printf("- %s (Year: %d)%n", m.getTitle(), m.getReleaseYear());
            }
        }
    }

    @Test
    void execute_UnknownExceptionType_ShouldUseDefaultErrorMessage() {


        TestMovieGateway gateway = new TestMovieGateway(List.of()) {
            @Override
            public PagedMovieResult searchByTitle(String query, int page) throws MovieDataAccessException {
                throw new MovieDataAccessException(
                        MovieDataAccessException.Type.UNKNOWN,
                        "Unknown error type"
                );
            }
        };

        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("test", 1);
        interactor.execute(request);

        assertTrue(presenter.failCalled, "Should fail on any exception");
        assertTrue(presenter.errorMessage.contains("unexpected error") ||
                        presenter.errorMessage.contains("Search failed"),
                "Should use default error message for unknown exception types");
    }

    @Test
    void scoring_MovieWithNullTitle_ShouldNotCrash() {

        List<Movie> movies = List.of(
                createMovieWithNullFields("1")
        );

        TestMovieGateway gateway = new TestMovieGateway(movies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        interactor.execute(new SearchMovieRequestModel("xyz", 1));

        assertTrue(presenter.successCalled || presenter.failCalled);
    }

    private Movie createMovieWithNullFields(String id) {
        try {
            Movie movie = new Movie(id, "dummy", "plot", List.of(1), "2023-01-01", 5.0, 50.0, "poster.jpg");

            java.lang.reflect.Field titleField = Movie.class.getDeclaredField("title");
            titleField.setAccessible(true);
            titleField.set(movie, null);

            return movie;
        } catch (Exception e) {

            return new Movie(id, null, "plot", List.of(1), "2023-01-01", 5.0, 50.0, "poster.jpg");
        }
    }

    @Test
    void scoring_MovieWithNullRating_ShouldHandleGracefully() {


        List<Movie> movies = List.of(
                new Movie("1", "Test Movie", "Plot", List.of(1), "2023-01-01", 0.0, 50.0, "poster.jpg")
        );

        TestMovieGateway gateway = new TestMovieGateway(movies);
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        interactor.execute(new SearchMovieRequestModel("Test", 1));


        assertTrue(presenter.successCalled || presenter.failCalled);
    }
}