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
        }

        @Override
        public void prepareFailView(String error) {
            this.errorMessage = error;
            this.failCalled = true;
        }
    }

    private static class TestMovieGateway implements use_case.common.MovieGateway {
        private final List<Movie> moviesToReturn;

        TestMovieGateway(List<Movie> movies) {
            this.moviesToReturn = movies;
        }

        @Override
        public use_case.common.PagedMovieResult searchByTitle(String query, int page) {
            return new use_case.common.PagedMovieResult(moviesToReturn, page, 3);
        }

        @Override
        public java.util.Optional<Movie> findById(String movieId) {
            return java.util.Optional.empty();
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
    void testSearchWithEmptyQuery() {
        TestMovieGateway gateway = new TestMovieGateway(List.of());
        TestPresenter presenter = new TestPresenter();
        SearchMovieInteractor interactor = new SearchMovieInteractor(gateway, presenter);

        SearchMovieRequestModel request = new SearchMovieRequestModel("", 1);
        interactor.execute(request);

        // 3. 验证
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