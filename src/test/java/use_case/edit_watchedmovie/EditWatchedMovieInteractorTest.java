package use_case.edit_watchedmovie;

import entity.Movie;
import entity.Review;
import entity.User;
import entity.WatchHistory;
import use_case.common.MovieGateway;
import use_case.common.UserDataAccessInterface;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EditWatchedMovieInteractorTest {

    @Test
    void successEditWatchedMovieWithRatingAndReview() {
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

        LocalDateTime originalDate = LocalDateTime.now().minusDays(2);
        watchHistory.recordMovie(movie, originalDate);
        assertEquals(1, watchHistory.getMovies().size());

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser;
            }

            @Override
            public void save(User user) {
                // Verify that user was saved
                assertNotNull(user.getWatchHistory());
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

        LocalDateTime newDate = LocalDateTime.now().minusDays(1);
        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "m1", newDate, 5, "Great movie!"
        );

        EditWatchedMovieOutputBoundary successPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertEquals("m1", responseModel.getMovieId());
                assertEquals("Test Movie", responseModel.getMovieTitle());
                assertEquals(newDate, responseModel.getWatchedDate());
                assertEquals(5, responseModel.getRating());
                assertEquals("Great movie!", responseModel.getReview());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, movieGateway, successPresenter
        );
        interactor.execute(requestModel);

        // Verify
        assertEquals(1, testUser.getWatchHistory().getMovies().size());
        assertEquals(newDate, testUser.getWatchHistory().getMovies().get(0).getWatchedDate());
        assertTrue(testUser.getReviewsByMovieId().containsKey("m1"));
        Review review = testUser.getReviewsByMovieId().get("m1");
        assertEquals(5, review.getRating());
        assertEquals("Great movie!", review.getComment());
    }

    @Test
    void successEditWatchedMovieOnlyDate() {
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

        LocalDateTime originalDate = LocalDateTime.now().minusDays(2);
        watchHistory.recordMovie(movie, originalDate);

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

        LocalDateTime newDate = LocalDateTime.now().minusDays(1);
        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "m1", newDate, null, null
        );

        EditWatchedMovieOutputBoundary successPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertEquals("m1", responseModel.getMovieId());
                assertEquals(newDate, responseModel.getWatchedDate());
                assertNull(responseModel.getRating());
                assertNull(responseModel.getReview());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, movieGateway, successPresenter
        );
        interactor.execute(requestModel);

        // Verify
        assertEquals(1, testUser.getWatchHistory().getMovies().size());
        assertEquals(newDate, testUser.getWatchHistory().getMovies().get(0).getWatchedDate());
    }

    @Test
    void successEditWatchedMovieKeepsOriginalDateWhenNull() {
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

        LocalDateTime originalDate = LocalDateTime.now().minusDays(2);
        watchHistory.recordMovie(movie, originalDate);

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

        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "m1", null, 4, "Good movie"
        );

        EditWatchedMovieOutputBoundary successPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                assertEquals(originalDate, responseModel.getWatchedDate());
                assertEquals(4, responseModel.getRating());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, movieGateway, successPresenter
        );
        interactor.execute(requestModel);

        // Verify original date is preserved
        assertEquals(originalDate, testUser.getWatchHistory().getMovies().get(0).getWatchedDate());
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

        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "nonexistent", "m1", LocalDateTime.now(), 5, "Review"
        );

        EditWatchedMovieOutputBoundary failPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                fail("Should have failed - user not found");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User not found: nonexistent", errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, null, failPresenter
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

        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "m1", LocalDateTime.now(), 5, "Review"
        );

        EditWatchedMovieOutputBoundary failPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                fail("Should have failed - no watch history");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("User has no watch history.", errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, null, failPresenter
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

        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "nonexistent", LocalDateTime.now(), 5, "Review"
        );

        EditWatchedMovieOutputBoundary failPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                fail("Should have failed - movie not found in history");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Movie not found in watch history: nonexistent", errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, null, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenMovieNotFoundInGateway() {
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
                fail("Should not save when movie not found in gateway");
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

        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "m1", LocalDateTime.now(), 5, "Review"
        );

        EditWatchedMovieOutputBoundary failPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                fail("Should have failed - movie not found in gateway");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Movie not found: m1", errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, movieGateway, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenWatchedDateIsInFuture() {
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
                fail("Should not save when date is in future");
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

        LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "m1", futureDate, null, null
        );

        EditWatchedMovieOutputBoundary failPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                fail("Should have failed - date in future");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Watched date cannot be in the future.", errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, movieGateway, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenRatingIsLessThanOne() {
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
                fail("Should not save when rating is invalid");
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

        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "m1", LocalDateTime.now().minusDays(1), 0, null
        );

        EditWatchedMovieOutputBoundary failPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                fail("Should have failed - rating too low");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Rating must be between 1 and 5.", errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, movieGateway, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void failWhenRatingIsGreaterThanFive() {
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
                fail("Should not save when rating is invalid");
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

        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "m1", LocalDateTime.now().minusDays(1), 6, null
        );

        EditWatchedMovieOutputBoundary failPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                fail("Should have failed - rating too high");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("Rating must be between 1 and 5.", errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, movieGateway, failPresenter
        );
        interactor.execute(requestModel);
    }

    @Test
    void successEditWatchedMovieWithOnlyReviewText() {
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

        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "m1", null, null, "Just a review text"
        );

        EditWatchedMovieOutputBoundary successPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertEquals("m1", responseModel.getMovieId());
                assertNull(responseModel.getRating());
                assertEquals("Just a review text", responseModel.getReview());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, movieGateway, successPresenter
        );
        interactor.execute(requestModel);

        // Verify review was created
        assertTrue(testUser.getReviewsByMovieId().containsKey("m1"));
        Review review = testUser.getReviewsByMovieId().get("m1");
        assertEquals(3, review.getRating()); // Default rating when not provided
        assertEquals("Just a review text", review.getComment());
    }

    @Test
    void successEditWatchedMovieWithEmptyReviewText() {
        // Setup - when review text is empty or whitespace, should not create review
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

        EditWatchedMovieRequestModel requestModel = new EditWatchedMovieRequestModel(
                "testuser", "m1", null, null, "   "
        );

        EditWatchedMovieOutputBoundary successPresenter = new EditWatchedMovieOutputBoundary() {
            @Override
            public void prepareSuccessView(EditWatchedMovieResponseModel responseModel) {
                assertEquals("testuser", responseModel.getUserName());
                assertEquals("m1", responseModel.getMovieId());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        // Execute
        EditWatchedMovieInteractor interactor = new EditWatchedMovieInteractor(
                userDataAccessInterface, movieGateway, successPresenter
        );
        interactor.execute(requestModel);

        // Verify no review was created when text is empty/whitespace and no rating
        assertFalse(testUser.getReviewsByMovieId().containsKey("m1"));
    }
}

