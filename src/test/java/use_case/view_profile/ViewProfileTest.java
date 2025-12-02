package use_case.view_profile;

import data_access.CachedUserDataAccessObject;
import entity.User;
import org.junit.jupiter.api.Test;
import use_case.common.UserDataAccessInterface;

import static org.junit.jupiter.api.Assertions.*;

class ViewProfileTest {

    @Test
    void testViewProfileInputData() {
        ViewProfileInputData input = new ViewProfileInputData("testuser");
        assertEquals("testuser", input.getUsername());
    }

    @Test
    void testViewProfileOutputData() {
        User user = new User("testuser", "1234");
        ProfileStats stats = new ProfileStats(2, 5, 10);

        ViewProfileOutputData output = new ViewProfileOutputData(user, stats);

        assertEquals(user, output.getUser());
        assertEquals(stats, output.getProfileStats());
        assertEquals("testuser", output.getUser().getUserName());
        assertEquals(2, output.getProfileStats().getWatchlistCount());
        assertEquals(5, output.getProfileStats().getReviewCount());
        assertEquals(10, output.getProfileStats().getWatchedMoviesCount());
    }

    @Test
    void testProfileStats() {
        ProfileStats stats = new ProfileStats(3, 7, 12);

        assertEquals(3, stats.getWatchlistCount());
        assertEquals(7, stats.getReviewCount());
        assertEquals(12, stats.getWatchedMoviesCount());
    }

    @Test
    void testInteractorSuccess() {
        // Setup - Create a CachedUserDataAccessObject with a fake persistent store
        User testUser = new User("testuser", "1234");

        UserDataAccessInterface fakePersistentStore = new UserDataAccessInterface() {
            @Override public User getUser(String u) { return testUser; }
            @Override public void save(User u) {}
            @Override public boolean existsByName(String u) { return true; }
            @Override public void setCurrentUsername(String u) {}
            @Override public String getCurrentUsername() { return "testuser"; }
        };

        CachedUserDataAccessObject userDAO = new CachedUserDataAccessObject(fakePersistentStore) {
            // Override getUserStats to return our test stats
            @Override
            public ProfileStats getUserStats(String username) {
                return new ProfileStats(2, 5, 10);
            }
        };

        ViewProfileInputData input = new ViewProfileInputData("testuser");

        ViewProfileOutputBoundary presenter = new ViewProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewProfileOutputData output) {
                assertEquals("testuser", output.getUser().getUserName());
                assertEquals(2, output.getProfileStats().getWatchlistCount());
                assertEquals(5, output.getProfileStats().getReviewCount());
                assertEquals(10, output.getProfileStats().getWatchedMoviesCount());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not call prepareFailView for success case");
            }
        };

        // Execute
        ViewProfileInteractor interactor = new ViewProfileInteractor(userDAO, presenter);
        interactor.execute(input);
    }

    @Test
    void testInteractorUserNotFound() {
        // Setup - Create CachedUserDataAccessObject that returns null for getUser
        UserDataAccessInterface fakePersistentStore = new UserDataAccessInterface() {
            @Override public User getUser(String u) { return null; }
            @Override public void save(User u) {}
            @Override public boolean existsByName(String u) { return false; }
            @Override public void setCurrentUsername(String u) {}
            @Override public String getCurrentUsername() { return ""; }
        };

        CachedUserDataAccessObject userDAO = new CachedUserDataAccessObject(fakePersistentStore);

        ViewProfileInputData input = new ViewProfileInputData("ghost");

        ViewProfileOutputBoundary presenter = new ViewProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewProfileOutputData output) {
                fail("Should not call prepareSuccessView when user not found");
            }

            @Override
            public void prepareFailView(String error) {
                assertEquals("User not found: ghost", error);
            }
        };

        ViewProfileInteractor interactor = new ViewProfileInteractor(userDAO, presenter);
        interactor.execute(input);
    }

    @Test
    void testInteractorWithZeroStats() {
        User testUser = new User("newuser", "1234");

        UserDataAccessInterface fakePersistentStore = new UserDataAccessInterface() {
            @Override public User getUser(String u) { return testUser; }
            @Override public void save(User u) {}
            @Override public boolean existsByName(String u) { return true; }
            @Override public void setCurrentUsername(String u) {}
            @Override public String getCurrentUsername() { return "newuser"; }
        };

        CachedUserDataAccessObject userDAO = new CachedUserDataAccessObject(fakePersistentStore) {
            @Override
            public ProfileStats getUserStats(String username) {
                return new ProfileStats(0, 0, 0);
            }
        };

        ViewProfileInputData input = new ViewProfileInputData("newuser");

        ViewProfileOutputBoundary presenter = new ViewProfileOutputBoundary() {
            @Override
            public void prepareSuccessView(ViewProfileOutputData output) {
                assertEquals("newuser", output.getUser().getUserName());
                assertEquals(0, output.getProfileStats().getWatchlistCount());
                assertEquals(0, output.getProfileStats().getReviewCount());
                assertEquals(0, output.getProfileStats().getWatchedMoviesCount());
            }

            @Override
            public void prepareFailView(String error) {
                fail("Should not fail for user with zero stats");
            }
        };

        ViewProfileInteractor interactor = new ViewProfileInteractor(userDAO, presenter);
        interactor.execute(input);
    }
}