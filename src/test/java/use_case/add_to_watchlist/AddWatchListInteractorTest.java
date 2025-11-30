package use_case.add_to_watchlist;

import entity.Movie;
import entity.User;
import entity.WatchList;
import use_case.common.MovieDataAccessException;
import use_case.common.MovieGateway;
import use_case.common.PagedMovieResult;
import use_case.common.UserDataAccessInterface;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AddWatchListInteractorTest {

    @Test
    void successAddMovieToWatchList() {

        User testUser = new User("testuser", "1234");
        WatchList watchList = testUser.getWatchLists().get(0);

        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot happens",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster-url"
        );

        UserDataAccessInterface userDataAccessObject = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser ;
            }

            @Override
            public void save(User user) {
                assertEquals("testuser", user.getUserName());
            }

            @Override
            public boolean existsByName(String username) {
                return true;
            }

            @Override
            public void setCurrentUsername(String username) {}

            @Override
            public String getCurrentUsername() {
                return "testuser";
            }
        };

        // Mock MovieGateway
        MovieGateway movieGateway = new MovieGateway() {
            @Override
            public Optional<Movie> findById(String movieId) {
                if (movieId.equals("m1")) {
                    return Optional.of(movie);
                }
                return Optional.empty();
            }

            @Override
            public List<Movie> searchByTitle(String query) {
                return List.of();
            }

            @Override
            public PagedMovieResult searchByTitle(String query, int page) throws MovieDataAccessException {
                return null;
            }

            @Override
            public List<Movie> filterByGenres(List<Integer> genreIds) {
                return List.of();
            }
        };

        // Input Model
        AddWatchListRequestModel inputData = new AddWatchListRequestModel(
                "testuser",
                "m1",
                watchList.getWatchListId());

        // Mock Presenter
        AddWatchListOutputBoundary presenter = new AddWatchListOutputBoundary() {
            @Override
            public void prepareSuccessView(AddWatchListResponseModel responseModel) {
                assertTrue(responseModel.isSuccess());
                assertEquals(
                        "Test Movie successfully added to " + watchList.getName() + "!", responseModel.getMessage());
                assertTrue(watchList.getMovies().contains(movie));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        AddWatchListInputBoundary interactor = new AddWatchListInteractor(presenter, userDataAccessObject, movieGateway);
        interactor.execute(inputData);
    }

    @Test
    void failAddMovieAlreadyInWatchList() {
        User testUser = new User("testuser", "1234");
        WatchList watchList = testUser.getWatchLists().get(0);

        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot happens",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster-url"
        );

        // add movie to WatchList
        watchList.addMovie(movie);

        // Mock User DAO
        UserDataAccessInterface userDataAccessObject = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser ;
            }

            @Override
            public void save(User user) {
                fail("Should not save when movie already exists.");
            }

            @Override
            public boolean existsByName(String username) {
                return true;
            }

            @Override
            public void setCurrentUsername(String username) {}

            @Override
            public String getCurrentUsername() {
                return "testuser";
            }
        };

        // Mock MovieGateway
        MovieGateway movieGateway = new MovieGateway() {
            @Override
            public Optional<Movie> findById(String movieId) {
                if (movieId.equals("m1")) {
                    return Optional.of(movie);
                }
                return Optional.empty();
            }

            @Override
            public List<Movie> searchByTitle(String query) {
                return List.of();
            }

            @Override
            public PagedMovieResult searchByTitle(String query, int page) throws MovieDataAccessException {
                return null;
            }

            @Override
            public List<Movie> filterByGenres(List<Integer> genreIds) {
                return List.of();
            }
        };

        // Input
        AddWatchListRequestModel inputData = new AddWatchListRequestModel(
                "testuser",
                "m1",
                watchList.getWatchListId()
        );

        // Mock Presenter
        AddWatchListOutputBoundary presenter = new AddWatchListOutputBoundary() {
            @Override
            public void prepareSuccessView(AddWatchListResponseModel responseModel) {
                fail("Should not have succeeded, movie already in WatchList.");
                assertFalse(responseModel.isSuccess());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(movie.getTitle() + " is already in " + watchList.getName() + ".", errorMessage);
            }
        };

        AddWatchListInputBoundary interactor = new AddWatchListInteractor(presenter, userDataAccessObject, movieGateway);
        interactor.execute(inputData);
    }

    @Test void failUserNotFound() {
        UserDataAccessInterface userDataAccessObject = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return null;
            }

            @Override
            public void save(User user) {}

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
        };

        MovieGateway movieGateway = new MovieGateway() {

            @Override
            public Optional<Movie> findById(String movieId) {
                return Optional.empty();
            }

            @Override
            public List<Movie> searchByTitle(String query) {
                return List.of();
            }

            @Override
            public PagedMovieResult searchByTitle(String query, int page) throws MovieDataAccessException {
                return null;
            }

            @Override
            public List<Movie> filterByGenres(List<Integer> genreIds) {
                return List.of();
            }
        };

        AddWatchListRequestModel inputData = new AddWatchListRequestModel("ghost", "m1", "wl1");

        AddWatchListOutputBoundary presenter = new AddWatchListOutputBoundary() {
            @Override
            public void prepareSuccessView(AddWatchListResponseModel responseModel) {
                fail("Should not succeed when user does not exist.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User not found: ghost", errorMessage);
            }
        };

        AddWatchListInteractor interactor = new AddWatchListInteractor(presenter, userDataAccessObject, movieGateway);
        interactor.execute(inputData);
    }

    @Test
    void failWatchListNotFound() {
        User user = new User("testuser", "1234");

        UserDataAccessInterface userDAO = new UserDataAccessInterface() {
            @Override public User getUser(String name) { return user; }
            @Override public void save(User user) {}
            @Override public boolean existsByName(String u) { return true; }
            @Override public void setCurrentUsername(String u) {}
            @Override public String getCurrentUsername() { return "testuser"; }
        };

        MovieGateway movieGateway = new MovieGateway() {
            @Override public Optional<Movie> findById(String id) { return Optional.empty(); }
            @Override public List<Movie> searchByTitle(String q) { return List.of(); }
            @Override public PagedMovieResult searchByTitle(String q, int p) { return null; }
            @Override public List<Movie> filterByGenres(List<Integer> ids) { return List.of(); }
        };

        AddWatchListRequestModel request =
                new AddWatchListRequestModel("testuser", "m1", "nonExistentWL");

        AddWatchListOutputBoundary presenter = new AddWatchListOutputBoundary() {
            @Override public void prepareSuccessView(AddWatchListResponseModel responseModel) {
                fail("Should not succeed if watchlist missing");
            }

            @Override public void prepareFailView(String errorMessage) {
                assertEquals("WatchList not found: nonExistentWL", errorMessage);
            }
        };

        AddWatchListInteractor interactor =
                new AddWatchListInteractor(presenter, userDAO, movieGateway);
        interactor.execute(request);
    }

    @Test
    void failMovieNotFound() {
        User user = new User("testuser", "1234");
        WatchList wl = user.getWatchLists().get(0);

        UserDataAccessInterface userDAO = new UserDataAccessInterface() {
            @Override public User getUser(String name) { return user; }
            @Override public void save(User user) {}
            @Override public boolean existsByName(String u) { return true; }
            @Override public void setCurrentUsername(String u) {}
            @Override public String getCurrentUsername() { return "testuser"; }
        };

        MovieGateway movieGateway = new MovieGateway() {
            @Override public Optional<Movie> findById(String id) {
                return Optional.empty(); // <-- trigger movie-not-found branch
            }
            @Override public List<Movie> searchByTitle(String q) { return List.of(); }
            @Override public PagedMovieResult searchByTitle(String q, int p) { return null; }
            @Override public List<Movie> filterByGenres(List<Integer> ids) { return List.of(); }
        };

        AddWatchListRequestModel request =
                new AddWatchListRequestModel("testuser", "noSuchMovie", wl.getWatchListId());

        AddWatchListOutputBoundary presenter = new AddWatchListOutputBoundary() {
            @Override public void prepareSuccessView(AddWatchListResponseModel responseModel) {
                fail("Should not succeed when movie not found");
            }

            @Override public void prepareFailView(String errorMessage) {
                assertEquals("Movie not found: noSuchMovie", errorMessage);
            }
        };

        AddWatchListInteractor interactor =
                new AddWatchListInteractor(presenter, userDAO, movieGateway);
        interactor.execute(request);
    }
}
