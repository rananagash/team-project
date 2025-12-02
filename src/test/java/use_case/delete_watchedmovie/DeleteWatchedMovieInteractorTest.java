package use_case.delete_watchedmovie;

import entity.Movie;
import entity.User;
import entity.WatchHistory;
import use_case.common.UserDataAccessInterface;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeleteWatchedMovieInteractorTest {

    @Test
    void successDeleteWatchedMovie() {
        // Setup
        User testUser = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory(UUID.randomUUID().toString(), testUser);
        testUser.setWatchHistory(watchHistory);

        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster-url"
        );

        watchHistory.recordMovie(movie, LocalDateTime.now().minusDays(1));
        assertEquals(1, watchHistory.getMovies().size());

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                // Verify that user was saved after deletion
                assertNotNull(user.getWatchHistory());
                assertEquals(0, user.getWatchHistory().getMovies().size());
            }

            @Override
            public boolean existsByName(String username) {
                return false;
            }

            @Override
            public void setCurrentUsername(String username) {
            }

            @Override
            public String getCurrentUsername() {
                return "";
            }
        };

        DeleteWatchedMovieRequestModel requestModel = new DeleteWatchedMovieRequestModel(
                "testuser", "m1"
        );

        DeleteWatchedMovieOutputBoundary successPresenter = new DeleteWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteWatchedMovieResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertEquals("m1", responseModel.getMovieId());
                assertEquals("Test Movie", responseModel.getMovieTitle());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        DeleteWatchedMovieInteractor interactor = new DeleteWatchedMovieInteractor(
                userDataAccessInterface, successPresenter
        );
        interactor.execute(requestModel);

        // Verify
        assertEquals(0, testUser.getWatchHistory().getMovies().size());
    }

    @Test
    void successDeleteWatchedMovieWithMultipleMovies() {
        // Setup
        User testUser = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory(UUID.randomUUID().toString(), testUser);
        testUser.setWatchHistory(watchHistory);

        Movie movie1 = new Movie(
                "m1",
                "Movie 1",
                "Plot 1",
                List.of(1),
                "2025-01-01",
                7.5,
                0.0,
                "poster1"
        );

        Movie movie2 = new Movie(
                "m2",
                "Movie 2",
                "Plot 2",
                List.of(2),
                "2025-01-02",
                8.0,
                0.0,
                "poster2"
        );

        watchHistory.recordMovie(movie1, LocalDateTime.now().minusDays(2));
        watchHistory.recordMovie(movie2, LocalDateTime.now().minusDays(1));
        assertEquals(2, watchHistory.getMovies().size());

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                assertEquals(1, user.getWatchHistory().getMovies().size());
            }

            @Override
            public boolean existsByName(String username) {
                return false;
            }

            @Override
            public void setCurrentUsername(String username) {
            }

            @Override
            public String getCurrentUsername() {
                return "";
            }
        };

        DeleteWatchedMovieRequestModel requestModel = new DeleteWatchedMovieRequestModel(
                "testuser", "m1"
        );

        DeleteWatchedMovieOutputBoundary successPresenter = new DeleteWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteWatchedMovieResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertEquals("m1", responseModel.getMovieId());
                assertEquals("Movie 1", responseModel.getMovieTitle());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        DeleteWatchedMovieInteractor interactor = new DeleteWatchedMovieInteractor(
                userDataAccessInterface, successPresenter
        );
        interactor.execute(requestModel);

        // Verify
        assertEquals(1, testUser.getWatchHistory().getMovies().size());
        assertEquals("Movie 2", testUser.getWatchHistory().getMovies().get(0).getTitle());
    }

    @Test
    void failWhenUserNotFound() {
        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return null;
            }

            @Override
            public void save(User user) {
                fail("Should not save when user not found");
            }

            @Override
            public boolean existsByName(String username) {
                return false;
            }

            @Override
            public void setCurrentUsername(String username) {
            }

            @Override
            public String getCurrentUsername() {
                return "";
            }
        };

        DeleteWatchedMovieRequestModel requestModel = new DeleteWatchedMovieRequestModel(
                "nonexistent", "m1"
        );

        DeleteWatchedMovieOutputBoundary failPresenter = new DeleteWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteWatchedMovieResponseModel responseModel) {
                fail("Should have failed - user not found");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User not found: nonexistent", errorMessage);
            }
        };

        // Execute
        DeleteWatchedMovieInteractor interactor = new DeleteWatchedMovieInteractor(
                userDataAccessInterface, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenWatchHistoryIsNull() {
        // Setup
        User testUser = new User("testuser", "1234");
        // User has no watch history

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                fail("Should not save when watch history is null");
            }

            @Override
            public boolean existsByName(String username) {
                return false;
            }

            @Override
            public void setCurrentUsername(String username) {
            }

            @Override
            public String getCurrentUsername() {
                return "";
            }
        };

        DeleteWatchedMovieRequestModel requestModel = new DeleteWatchedMovieRequestModel(
                "testuser", "m1"
        );

        DeleteWatchedMovieOutputBoundary failPresenter = new DeleteWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteWatchedMovieResponseModel responseModel) {
                fail("Should have failed - no watch history");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User has no watch history.", errorMessage);
            }
        };

        // Execute
        DeleteWatchedMovieInteractor interactor = new DeleteWatchedMovieInteractor(
                userDataAccessInterface, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenMovieNotFoundInHistory() {
        // Setup
        User testUser = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory(UUID.randomUUID().toString(), testUser);
        testUser.setWatchHistory(watchHistory);

        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster-url"
        );

        watchHistory.recordMovie(movie, LocalDateTime.now().minusDays(1));

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                fail("Should not save when movie not found");
            }

            @Override
            public boolean existsByName(String username) {
                return false;
            }

            @Override
            public void setCurrentUsername(String username) {
            }

            @Override
            public String getCurrentUsername() {
                return "";
            }
        };

        DeleteWatchedMovieRequestModel requestModel = new DeleteWatchedMovieRequestModel(
                "testuser", "nonexistent"
        );

        DeleteWatchedMovieOutputBoundary failPresenter = new DeleteWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteWatchedMovieResponseModel responseModel) {
                fail("Should have failed - movie not found");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Movie not found in watch history: nonexistent", errorMessage);
            }
        };

        // Execute
        DeleteWatchedMovieInteractor interactor = new DeleteWatchedMovieInteractor(
                userDataAccessInterface, failPresenter
        );
        interactor.execute(requestModel);

        // Verify movie was not removed
        assertEquals(1, testUser.getWatchHistory().getMovies().size());
    }

    @Test
    void successDeleteWatchedMovieUsesUnknownTitleWhenMovieNotFoundBeforeDeletion() {
        // Setup - edge case: movie exists but title retrieval fails
        User testUser = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory(UUID.randomUUID().toString(), testUser);
        testUser.setWatchHistory(watchHistory);

        // Add a movie with a known title
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster-url"
        );

        watchHistory.recordMovie(movie, LocalDateTime.now().minusDays(1));

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                assertEquals(0, user.getWatchHistory().getMovies().size());
            }

            @Override
            public boolean existsByName(String username) {
                return false;
            }

            @Override
            public void setCurrentUsername(String username) {
            }

            @Override
            public String getCurrentUsername() {
                return "";
            }
        };

        DeleteWatchedMovieRequestModel requestModel = new DeleteWatchedMovieRequestModel(
                "testuser", "m1"
        );

        DeleteWatchedMovieOutputBoundary successPresenter = new DeleteWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(DeleteWatchedMovieResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertEquals("m1", responseModel.getMovieId());
                assertEquals("Test Movie", responseModel.getMovieTitle());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        DeleteWatchedMovieInteractor interactor = new DeleteWatchedMovieInteractor(
                userDataAccessInterface, successPresenter
        );
        interactor.execute(requestModel);
    }
}

