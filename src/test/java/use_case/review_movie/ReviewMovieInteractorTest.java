package use_case.review_movie;

import entity.Movie;
import entity.Review;
import entity.User;
import use_case.common.MovieGateway;
import use_case.common.MovieDataAccessException;
import use_case.common.PagedMovieResult;
import use_case.common.UserDataAccessInterface;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ReviewMovieInteractorTest {

    // Fake Presenter
    static class FakePresenter implements ReviewMovieOutputBoundary {
        ReviewMovieResponseModel success;
        String failure;

        @Override
        public void prepareSuccessView(ReviewMovieResponseModel model) {
            this.success = model;
        }

        @Override
        public void prepareFailView(String message) {
            this.failure = message;
        }
    }

    // Fake User DAO
    static class FakeUserDAO implements UserDataAccessInterface {
        User storedUser;
        boolean saved;

        @Override
        public User getUser(String userName) {
            return storedUser;
        }

        @Override
        public void save(User user) {
            saved = true;
        }

        @Override
        public boolean existsByName(String username) {
            return false;
        }

        @Override
        public void setCurrentUsername(String username) {}

        @Override
        public String getCurrentUsername() {
            return "";
        }
    }

    // Fake Movie Gateway
    // Fake Movie Gateway
    static class FakeMovieGateway implements MovieGateway {

        Optional<Movie> movie = Optional.empty();

        @Override
        public Optional<Movie> findById(String id) {
            return movie;
        }

        @Override
        public List<Movie> searchByTitle(String query) {
            return List.of();
        }

        @Override
        public PagedMovieResult searchByTitle(String query, int page) throws MovieDataAccessException {
            return new PagedMovieResult(List.of(), 1, 0);
        }

        @Override
        public List<Movie> filterByGenres(List<Integer> genres) {
            return List.of();
        }

        @Override
        public PagedMovieResult getPopularMovies(int page) throws MovieDataAccessException {
            return new PagedMovieResult(List.of(), page, 1);
        }
    }

    @Test
    void successReviewMovie() {

        FakeUserDAO userDAO = new FakeUserDAO();
        FakeMovieGateway movieGateway = new FakeMovieGateway();
        FakePresenter presenter = new FakePresenter();

        User user = new User("john", "1234");
        Movie movie = new Movie(
                "M001",
                "Test Movie",
                "Plot",
                List.of(1,2),
                "2025",
                5.0,
                0.0,
                "poster"
        );

        userDAO.storedUser = user;
        movieGateway.movie = Optional.of(movie);

        ReviewMovieInteractor interactor = new ReviewMovieInteractor(userDAO, movieGateway, presenter);

        ReviewMovieRequestModel request = new ReviewMovieRequestModel(
                "john",
                "M001",
                4,
                "Good movie"
        );

        interactor.execute(request);

        assertNotNull(presenter.success);
        assertNull(presenter.failure);

        assertEquals("john", presenter.success.getUserName());
        assertEquals("M001", presenter.success.getMovieId());
        assertEquals(4, presenter.success.getRating());
        assertEquals("Good movie", presenter.success.getComment());
        assertTrue(userDAO.saved);
    }

    @Test
    void failInvalidRating() {
        FakeUserDAO userDAO = new FakeUserDAO();
        FakeMovieGateway movieGateway = new FakeMovieGateway();
        FakePresenter presenter = new FakePresenter();

        ReviewMovieInteractor interactor = new ReviewMovieInteractor(userDAO, movieGateway, presenter);

        ReviewMovieRequestModel request = new ReviewMovieRequestModel(
                "john",
                "M001",
                7,
                "bad"
        );

        interactor.execute(request);

        assertNull(presenter.success);
        assertEquals("Rating must be between 1 and 5.", presenter.failure);
    }

    @Test
    void failInvalidRatingTooLow() {

        FakeUserDAO userDAO = new FakeUserDAO();
        FakeMovieGateway movieGateway = new FakeMovieGateway();
        FakePresenter presenter = new FakePresenter();

        ReviewMovieInteractor interactor = new ReviewMovieInteractor(userDAO, movieGateway, presenter);

        ReviewMovieRequestModel request = new ReviewMovieRequestModel(
                "john",
                "M001",
                0,
                "bad"
        );

        interactor.execute(request);

        assertNull(presenter.success);
        assertEquals("Rating must be between 1 and 5.", presenter.failure);
    }

    @Test
    void failUserNotFound() {

        FakeUserDAO userDAO = new FakeUserDAO();
        FakeMovieGateway movieGateway = new FakeMovieGateway();
        FakePresenter presenter = new FakePresenter();

        // userDAO.storedUser stays null

        movieGateway.movie = Optional.of(
                new Movie("M001", "Test", "Plot", List.of(1), "2025", 1.0, 0.0, "poster")
        );

        ReviewMovieInteractor interactor = new ReviewMovieInteractor(userDAO, movieGateway, presenter);

        ReviewMovieRequestModel request = new ReviewMovieRequestModel(
                "ghost",
                "M001",
                3,
                "ok"
        );

        interactor.execute(request);

        assertNull(presenter.success);
        assertEquals("User not found: ghost", presenter.failure);
    }

    @Test
    void failMovieNotFound() {

        FakeUserDAO userDAO = new FakeUserDAO();
        FakeMovieGateway movieGateway = new FakeMovieGateway();
        FakePresenter presenter = new FakePresenter();

        userDAO.storedUser = new User("john", "1234");

        movieGateway.movie = Optional.empty();

        ReviewMovieInteractor interactor = new ReviewMovieInteractor(userDAO, movieGateway, presenter);

        ReviewMovieRequestModel request = new ReviewMovieRequestModel(
                "john",
                "M001",
                3,
                "ok"
        );

        interactor.execute(request);

        assertNull(presenter.success);
        assertEquals("Movie not found: M001", presenter.failure);
    }

    @Test
    void responseModelAllGetters() {

        ReviewMovieResponseModel model = new ReviewMovieResponseModel(
                "R123",
                "john",
                "M001",
                "Test Movie",
                5,
                "Nice"
        );

        assertEquals("R123", model.getReviewId());
        assertEquals("john", model.getUserName());
        assertEquals("M001", model.getMovieId());
        assertEquals("Test Movie", model.getMovieName());
        assertEquals(5, model.getRating());
        assertEquals("Nice", model.getComment());
    }


}