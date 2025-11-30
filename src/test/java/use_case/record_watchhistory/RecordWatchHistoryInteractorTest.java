package use_case.record_watchhistory;

import entity.Movie;
import entity.User;
import entity.WatchHistory;
import entity.WatchedMovie;
import use_case.common.MovieGateway;
import use_case.common.UserDataAccessInterface;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RecordWatchHistoryInteractorTest {

    @Test
    void successRecordMovieWithExistingWatchHistory() {
        // Setup
        User testUser = new User("testuser", "1234");
        WatchHistory existingHistory = new WatchHistory("history1", testUser);
        testUser.setWatchHistory(existingHistory);

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

        LocalDateTime watchedAt = LocalDateTime.now().minusDays(1);

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                // Verify that user was saved
                assertNotNull(user.getWatchHistory());
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

        MovieGateway movieGateway = new MovieGateway() {
            @Override
            public Optional<Movie> findById(String movieId) {
                return Optional.of(movie);
            }

            @Override
            public List<Movie> searchByTitle(String query) {
                return List.of();
            }

            @Override
            public use_case.common.PagedMovieResult searchByTitle(String query, int page)
                    throws use_case.common.MovieDataAccessException {
                throw new use_case.common.MovieDataAccessException(
                        use_case.common.MovieDataAccessException.Type.UNKNOWN,
                        "Not implemented in test"
                );
            }

            @Override
            public List<Movie> filterByGenres(List<Integer> genreIds) {
                return List.of();
            }

            @Override
            public use_case.common.PagedMovieResult getPopularMovies(int page)
                    throws use_case.common.MovieDataAccessException {
                throw new use_case.common.MovieDataAccessException(
                        use_case.common.MovieDataAccessException.Type.UNKNOWN,
                        "Not implemented in test"
                );
            }
        };

        RecordWatchHistoryRequestModel requestModel = new RecordWatchHistoryRequestModel(
                "testuser", "m1", watchedAt
        );

        RecordWatchHistoryOutputBoundary successPresenter = new RecordWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertEquals("m1", responseModel.getMovieId());
                assertEquals("Test Movie", responseModel.getMovieTitle());
                assertEquals(watchedAt, responseModel.getWatchedAt());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(
                userDataAccessInterface, movieGateway, successPresenter
        );
        interactor.execute(requestModel);

        // Verify
        assertNotNull(testUser.getWatchHistory());
        assertEquals(1, testUser.getWatchHistory().getMovies().size());
        WatchedMovie recordedMovie = testUser.getWatchHistory().getMovies().get(0);
        assertEquals("Test Movie", recordedMovie.getTitle());
        assertEquals(watchedAt, recordedMovie.getWatchedDate());
    }

    @Test
    void successRecordMovieCreatesWatchHistoryIfNotExists() {
        // Setup
        User testUser = new User("testuser", "1234");
        // User has no watch history initially

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

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                // Verify that watch history was created
                assertNotNull(user.getWatchHistory());
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

        MovieGateway movieGateway = new MovieGateway() {
            @Override
            public Optional<Movie> findById(String movieId) {
                return Optional.of(movie);
            }

            @Override
            public List<Movie> searchByTitle(String query) {
                return List.of();
            }

            @Override
            public use_case.common.PagedMovieResult searchByTitle(String query, int page)
                    throws use_case.common.MovieDataAccessException {
                throw new use_case.common.MovieDataAccessException(
                        use_case.common.MovieDataAccessException.Type.UNKNOWN,
                        "Not implemented in test"
                );
            }

            @Override
            public List<Movie> filterByGenres(List<Integer> genreIds) {
                return List.of();
            }

            @Override
            public use_case.common.PagedMovieResult getPopularMovies(int page)
                    throws use_case.common.MovieDataAccessException {
                throw new use_case.common.MovieDataAccessException(
                        use_case.common.MovieDataAccessException.Type.UNKNOWN,
                        "Not implemented in test"
                );
            }
        };

        RecordWatchHistoryRequestModel requestModel = new RecordWatchHistoryRequestModel(
                "testuser", "m1", null // null watchedAt should use current time
        );

        RecordWatchHistoryOutputBoundary successPresenter = new RecordWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertEquals("m1", responseModel.getMovieId());
                assertEquals("Test Movie", responseModel.getMovieTitle());
                assertNotNull(responseModel.getWatchedAt());
                // WatchedAt should be close to now (within 1 second)
                assertTrue(LocalDateTime.now().minusSeconds(1).isBefore(responseModel.getWatchedAt()));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(
                userDataAccessInterface, movieGateway, successPresenter
        );
        interactor.execute(requestModel);

        // Verify
        assertNotNull(testUser.getWatchHistory());
        assertEquals(1, testUser.getWatchHistory().getMovies().size());
    }

    @Test
    void successRecordMovieAllowsDuplicates() {
        // Setup - same movie can be recorded multiple times
        User testUser = new User("testuser", "1234");
        WatchHistory history = new WatchHistory("history1", testUser);
        testUser.setWatchHistory(history);

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

        LocalDateTime firstWatch = LocalDateTime.now().minusDays(2);
        LocalDateTime secondWatch = LocalDateTime.now().minusDays(1);

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                // User should be saved
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

        MovieGateway movieGateway = new MovieGateway() {
            @Override
            public Optional<Movie> findById(String movieId) {
                return Optional.of(movie);
            }

            @Override
            public List<Movie> searchByTitle(String query) {
                return List.of();
            }

            @Override
            public use_case.common.PagedMovieResult searchByTitle(String query, int page)
                    throws use_case.common.MovieDataAccessException {
                throw new use_case.common.MovieDataAccessException(
                        use_case.common.MovieDataAccessException.Type.UNKNOWN,
                        "Not implemented in test"
                );
            }

            @Override
            public List<Movie> filterByGenres(List<Integer> genreIds) {
                return List.of();
            }

            @Override
            public use_case.common.PagedMovieResult getPopularMovies(int page)
                    throws use_case.common.MovieDataAccessException {
                throw new use_case.common.MovieDataAccessException(
                        use_case.common.MovieDataAccessException.Type.UNKNOWN,
                        "Not implemented in test"
                );
            }
        };

        // Record first time
        RecordWatchHistoryRequestModel firstRequest = new RecordWatchHistoryRequestModel(
                "testuser", "m1", firstWatch
        );

        RecordWatchHistoryOutputBoundary presenter = new RecordWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
                // Success
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(
                userDataAccessInterface, movieGateway, presenter
        );
        interactor.execute(firstRequest);

        // Record second time (same movie)
        RecordWatchHistoryRequestModel secondRequest = new RecordWatchHistoryRequestModel(
                "testuser", "m1", secondWatch
        );
        interactor.execute(secondRequest);

        // Verify both entries exist
        assertEquals(2, testUser.getWatchHistory().getMovies().size());
    }

    @Test
    void failWhenUserNameIsNull() {
        RecordWatchHistoryRequestModel requestModel = new RecordWatchHistoryRequestModel(
                null, "m1", LocalDateTime.now()
        );

        RecordWatchHistoryOutputBoundary failPresenter = new RecordWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
                fail("Should have failed with null username");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User name cannot be empty.", errorMessage);
            }
        };

        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(
                null, null, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenUserNameIsBlank() {
        RecordWatchHistoryRequestModel requestModel = new RecordWatchHistoryRequestModel(
                "   ", "m1", LocalDateTime.now()
        );

        RecordWatchHistoryOutputBoundary failPresenter = new RecordWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
                fail("Should have failed with blank username");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User name cannot be empty.", errorMessage);
            }
        };

        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(
                null, null, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenMovieIdIsNull() {
        RecordWatchHistoryRequestModel requestModel = new RecordWatchHistoryRequestModel(
                "testuser", null, LocalDateTime.now()
        );

        RecordWatchHistoryOutputBoundary failPresenter = new RecordWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
                fail("Should have failed with null movie ID");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Movie ID cannot be empty.", errorMessage);
            }
        };

        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(
                null, null, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenMovieIdIsBlank() {
        RecordWatchHistoryRequestModel requestModel = new RecordWatchHistoryRequestModel(
                "testuser", "", LocalDateTime.now()
        );

        RecordWatchHistoryOutputBoundary failPresenter = new RecordWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
                fail("Should have failed with blank movie ID");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Movie ID cannot be empty.", errorMessage);
            }
        };

        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(
                null, null, failPresenter
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
                // Should not be called
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

        RecordWatchHistoryRequestModel requestModel = new RecordWatchHistoryRequestModel(
                "nonexistent", "m1", LocalDateTime.now()
        );

        RecordWatchHistoryOutputBoundary failPresenter = new RecordWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
                fail("Should have failed - user not found");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User not found: nonexistent", errorMessage);
            }
        };

        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(
                userDataAccessInterface, null, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenMovieNotFound() {
        User testUser = new User("testuser", "1234");

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                // Should not be called
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
            public use_case.common.PagedMovieResult searchByTitle(String query, int page)
                    throws use_case.common.MovieDataAccessException {
                throw new use_case.common.MovieDataAccessException(
                        use_case.common.MovieDataAccessException.Type.UNKNOWN,
                        "Not implemented in test"
                );
            }

            @Override
            public List<Movie> filterByGenres(List<Integer> genreIds) {
                return List.of();
            }

            @Override
            public use_case.common.PagedMovieResult getPopularMovies(int page)
                    throws use_case.common.MovieDataAccessException {
                throw new use_case.common.MovieDataAccessException(
                        use_case.common.MovieDataAccessException.Type.UNKNOWN,
                        "Not implemented in test"
                );
            }
        };

        RecordWatchHistoryRequestModel requestModel = new RecordWatchHistoryRequestModel(
                "testuser", "nonexistent", LocalDateTime.now()
        );

        RecordWatchHistoryOutputBoundary failPresenter = new RecordWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
                fail("Should have failed - movie not found");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Movie not found: nonexistent", errorMessage);
            }
        };

        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(
                userDataAccessInterface, movieGateway, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenWatchedAtIsInFuture() {
        User testUser = new User("testuser", "1234");

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

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                // Should not be called
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

        MovieGateway movieGateway = new MovieGateway() {
            @Override
            public Optional<Movie> findById(String movieId) {
                return Optional.of(movie);
            }

            @Override
            public List<Movie> searchByTitle(String query) {
                return List.of();
            }

            @Override
            public use_case.common.PagedMovieResult searchByTitle(String query, int page)
                    throws use_case.common.MovieDataAccessException {
                throw new use_case.common.MovieDataAccessException(
                        use_case.common.MovieDataAccessException.Type.UNKNOWN,
                        "Not implemented in test"
                );
            }

            @Override
            public List<Movie> filterByGenres(List<Integer> genreIds) {
                return List.of();
            }

            @Override
            public use_case.common.PagedMovieResult getPopularMovies(int page)
                    throws use_case.common.MovieDataAccessException {
                throw new use_case.common.MovieDataAccessException(
                        use_case.common.MovieDataAccessException.Type.UNKNOWN,
                        "Not implemented in test"
                );
            }
        };

        LocalDateTime futureTime = LocalDateTime.now().plusDays(1);
        RecordWatchHistoryRequestModel requestModel = new RecordWatchHistoryRequestModel(
                "testuser", "m1", futureTime
        );

        RecordWatchHistoryOutputBoundary failPresenter = new RecordWatchHistoryOutputBoundary() {
            @Override
            public void prepareSuccessView(RecordWatchHistoryResponseModel responseModel) {
                fail("Should have failed - watched time in future");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Watched time cannot be in the future.", errorMessage);
            }
        };

        RecordWatchHistoryInteractor interactor = new RecordWatchHistoryInteractor(
                userDataAccessInterface, movieGateway, failPresenter
        );
        interactor.execute(requestModel);
    }
}

