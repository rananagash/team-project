package use_case.view_watchlists;

import entity.Movie;
import entity.User;
import entity.WatchList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.common.UserDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ViewWatchListsInteractorTest {

    private FakeUserDAO userDAO;
    private FakePresenter presenter;
    private ViewWatchListsInteractor interactor;

    @BeforeEach
    public void setup() {
        userDAO = new FakeUserDAO();
        presenter = new FakePresenter();
        interactor = new ViewWatchListsInteractor(userDAO, presenter);
    }

    // --------------------------
    // Failure Cases
    // --------------------------

    @Test
    public void testUserNotFound() {
        ViewWatchListsRequestModel request = new ViewWatchListsRequestModel("missingUser", 0);

        interactor.execute(request);

        assertTrue(presenter.didFail);
        assertEquals("User not found.", presenter.errorMessage);
    }

    @Test
    public void testUserHasNoWatchlists() {
        User user = new User("alice", "pw");
        user.clearWatchLists();
        userDAO.save(user);

        ViewWatchListsRequestModel request = new ViewWatchListsRequestModel("alice", 0);

        interactor.execute(request);

        assertTrue(presenter.didFail);
        assertEquals("User has no watchlists.", presenter.errorMessage);
    }

    // --------------------------
    // Success Cases
    // --------------------------

    @Test
    public void testSuccessValidIndex() {
        setupUserWithTwoWatchlists();

        ViewWatchListsRequestModel request =
                new ViewWatchListsRequestModel("bob", 1);

        interactor.execute(request);

        assertFalse(presenter.didFail);
        ViewWatchListsResponseModel response = presenter.successResponse;

        assertEquals("bob", response.getUsername());
        assertEquals(2, response.getWatchLists().size());
        assertEquals(1, response.getSelectedIndex());

        // Selected list should have 1 movie
        assertEquals(1, response.getMovies().size());
        assertEquals("M2", response.getMovies().get(0).getId());
    }

    @Test
    public void testSuccessIndexTooLargeDefaultsToZero() {
        setupUserWithTwoWatchlists();

        ViewWatchListsRequestModel request =
                new ViewWatchListsRequestModel("bob", 99);

        interactor.execute(request);

        assertFalse(presenter.didFail);
        ViewWatchListsResponseModel response = presenter.successResponse;

        assertEquals(0, response.getSelectedIndex());
        assertEquals("M1", response.getMovies().get(0).getId());
    }

    @Test
    public void testSuccessNegativeIndexDefaultsToZero() {
        setupUserWithTwoWatchlists();

        ViewWatchListsRequestModel request =
                new ViewWatchListsRequestModel("bob", -5);

        interactor.execute(request);

        assertFalse(presenter.didFail);
        ViewWatchListsResponseModel response = presenter.successResponse;

        assertEquals(0, response.getSelectedIndex());
        assertEquals("M1", response.getMovies().get(0).getId());
    }

    @Test
    public void testUserWithSingleWatchlist() {
        User user = new User("alice", "pw");

        WatchList wl = user.getWatchLists().get(0);
        wl.addMovie(new Movie("M1", "One Movie", "Plot",
                List.of(10), "2020", 8.0, 0, "url"));

        userDAO.save(user);

        ViewWatchListsRequestModel request =
                new ViewWatchListsRequestModel("alice", 0);

        interactor.execute(request);

        assertFalse(presenter.didFail);

        ViewWatchListsResponseModel response = presenter.successResponse;
        assertEquals("alice", response.getUsername());
        assertEquals(1, response.getWatchLists().size());
        assertEquals(0, response.getSelectedIndex());
        assertEquals(1, response.getMovies().size());
        assertEquals("M1", response.getMovies().get(0).getId());
    }

    // --------------------------
    // Helpers
    // --------------------------

    private void setupUserWithTwoWatchlists() {
        User user = new User("bob", "pw");

        //Watchlist 1
        WatchList wl1 = user.getWatchLists().get(0);
        wl1.addMovie(new Movie(
                "M1",
                "Movie One",
                "Plot 1",
                List.of(1, 2),
                "2020-01-01",
                5.0,
                0.0,
                "url1"
        ));

        //Watchlist 2
        WatchList wl2 = new WatchList(user, "Second");
        wl2.addMovie(new Movie(
                "M2",
                "Movie Two",
                "Plot 2",
                List.of(1, 2),
                "2020-01-01",
                5.0,
                0.0,
                "url2"
        ));
        user.addWatchList(wl2);

        userDAO.save(user);
    }

    // --------------------------
    // Fake (Stub) Classes
    // --------------------------

    private static class FakeUserDAO implements UserDataAccessInterface {

        private final List<User> users = new ArrayList<>();

        @Override
        public User getUser(String userName) {
            return users.stream()
                    .filter(u -> u.getUserName().equals(userName))
                    .findFirst()
                    .orElse(null);
        }

        @Override
        public void save(User user) {
            users.removeIf(u -> u.getUserName().equals(user.getUserName()));
            users.add(user);
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
    }

    private static class FakePresenter implements ViewWatchListsOutputBoundary {
        boolean didFail = false;
        String errorMessage;
        ViewWatchListsResponseModel successResponse;


        @Override
        public void prepareSuccessView(ViewWatchListsResponseModel responseModel) {
            this.successResponse = responseModel;
        }

        @Override
        public void prepareFailureView(String errorMessage) {
            this.didFail = true;
            this.errorMessage  = errorMessage;
        }
    }
}
