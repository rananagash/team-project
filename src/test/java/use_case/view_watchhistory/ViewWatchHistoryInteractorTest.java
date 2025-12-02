package use_case.view_watchhistory;

import entity.Movie;
import entity.User;
import entity.WatchHistory;
import use_case.common.UserDataAccessInterface;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ViewWatchHistoryInteractorTest {

    @Test
    void successViewWatchHistory() {
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

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                // Should not be called in view use case
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

        ViewWatchHistoryRequestModel requestModel = new ViewWatchHistoryRequestModel("testuser");

        ViewWatchHistoryOutputBoundary successPresenter = new ViewWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewWatchHistoryResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertEquals(2, responseModel.getWatchedMovies().size());
                assertEquals("Movie 1", responseModel.getWatchedMovies().get(0).getTitle());
                assertEquals("Movie 2", responseModel.getWatchedMovies().get(1).getTitle());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        ViewWatchHistoryInteractor interactor = new ViewWatchHistoryInteractor(
                userDataAccessInterface, successPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void successViewEmptyWatchHistory() {
        // Setup
        User testUser = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory(UUID.randomUUID().toString(), testUser);
        testUser.setWatchHistory(watchHistory);

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
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

        ViewWatchHistoryRequestModel requestModel = new ViewWatchHistoryRequestModel("testuser");

        ViewWatchHistoryOutputBoundary successPresenter = new ViewWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewWatchHistoryResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertTrue(responseModel.getWatchedMovies().isEmpty());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        ViewWatchHistoryInteractor interactor = new ViewWatchHistoryInteractor(
                userDataAccessInterface, successPresenter
        );
        interactor.execute(requestModel);
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

        ViewWatchHistoryRequestModel requestModel = new ViewWatchHistoryRequestModel("nonexistent");

        ViewWatchHistoryOutputBoundary failPresenter = new ViewWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewWatchHistoryResponseModel responseModel) {
                fail("Should have failed - user not found");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User not found: nonexistent", errorMessage);
            }
        };

        // Execute
        ViewWatchHistoryInteractor interactor = new ViewWatchHistoryInteractor(
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

        ViewWatchHistoryRequestModel requestModel = new ViewWatchHistoryRequestModel("testuser");

        ViewWatchHistoryOutputBoundary failPresenter = new ViewWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewWatchHistoryResponseModel responseModel) {
                fail("Should have failed - no watch history");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("This user has no watch history yet.", errorMessage);
            }
        };

        // Execute
        ViewWatchHistoryInteractor interactor = new ViewWatchHistoryInteractor(
                userDataAccessInterface, failPresenter
        );
        interactor.execute(requestModel);
    }
}

