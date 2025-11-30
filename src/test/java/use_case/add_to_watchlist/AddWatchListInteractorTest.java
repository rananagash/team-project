package use_case.add_to_watchlist;

import data_access.CachedUserDataAccessObject;
import entity.Movie;
import entity.User;
import entity.WatchList;
import use_case.common.UserDataAccessInterface;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AddWatchListInteractorTest {

    @Test
    void successAddMovieToWatchList() {

        User testUser = new User("testuser", "1234");
        WatchList watchList = testUser.getWatchLists().get(0); // default Watch List created on initialization

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

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser ;
            }

            @Override
            public void save(User user) {
                System.out.println("User saved: " + user.getUserName());
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

        AddWatchListRequestModel inputData = new AddWatchListRequestModel(testUser, movie, watchList);


        AddWatchListOutputBoundary successPresenter = new AddWatchListOutputBoundary() {
            @Override
            public void prepareSuccessView(AddWatchListResponseModel responseModel) {
                assertEquals(movie.getTitle() + " successfully added to " + watchList.getName() + "!", responseModel.getMessage());
                assertTrue(watchList.getMovies().contains(movie));
                assertTrue(responseModel.isSuccess());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Unexpected failure: " + errorMessage);
            }
        };

        AddWatchListInputBoundary interactor = new AddWatchListInteractor(successPresenter, (CachedUserDataAccessObject) userDataAccessInterface);
        interactor.execute(inputData);
    }

    @Test
    void failAddMovieAlreadyInWatchList() {
        User testUser = new User("testuser", "1234");
        WatchList watchList = testUser.getWatchLists().get(0); // default Watch List created on initialization

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

        UserDataAccessInterface userDataAccessInterface = new UserDataAccessInterface() {
            @Override
            public User getUser(String userName) {
                return testUser ;
            }

            @Override
            public void save(User user) {
                System.out.println("User saved: " + user.getUserName());
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

        AddWatchListRequestModel inputData = new AddWatchListRequestModel(testUser, movie, watchList);


        AddWatchListOutputBoundary successPresenter = new AddWatchListOutputBoundary() {
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

        AddWatchListInputBoundary interactor = new AddWatchListInteractor(successPresenter, (CachedUserDataAccessObject) userDataAccessInterface);
        interactor.execute(inputData);
    }
}
