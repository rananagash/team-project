package entity.factories;

import entity.User;
import entity.WatchList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link UserFactory} class.
 */
class UserFactoryTest {

    private UserFactory userFactory;

    @BeforeEach
    void setUp() {
        userFactory = new UserFactory();
    }

    @Test
    void createUserWithValidNameAndPassword() {
        User user = userFactory.create("testuser", "password123");

        assertNotNull(user);
        assertEquals("testuser", user.getUserName());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void createUserInitializesDefaultWatchList() {
        User user = userFactory.create("alice", "secret");

        assertEquals(1, user.getWatchLists().size());
        WatchList defaultList = user.getWatchLists().get(0);
        assertEquals("alice's Watch List", defaultList.getName());
        assertEquals(user, defaultList.getUser());
    }

    @Test
    void createUserWithEmptyUsernameThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            userFactory.create("", "password");
        });
    }

    @Test
    void createUserWithEmptyPasswordThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            userFactory.create("username", "");
        });
    }

    @Test
    void createUserWithNullUsernameThrows() {
        assertThrows(NullPointerException.class, () -> {
            userFactory.create(null, "password");
        });
    }

    @Test
    void createUserWithNullPasswordThrows() {
        assertThrows(NullPointerException.class, () -> {
            userFactory.create("username", null);
        });
    }

    @Test
    void createMultipleUsersReturnsDifferentInstances() {
        User user1 = userFactory.create("user1", "pass1");
        User user2 = userFactory.create("user2", "pass2");

        assertNotSame(user1, user2);
        assertNotEquals(user1, user2);
    }

    @Test
    void createUserWithSpecialCharactersInName() {
        User user = userFactory.create("user_name-123", "password");

        assertEquals("user_name-123", user.getUserName());
        assertEquals("password", user.getPassword());
    }

    @Test
    void createUserWithLongPassword() {
        String longPassword = "a".repeat(100);
        User user = userFactory.create("test", longPassword);

        assertEquals(longPassword, user.getPassword());
    }

    @Test
    void createUserWithWhitespaceInName() {
        // Note: User constructor doesn't trim whitespace, so this should work
        User user = userFactory.create(" test user ", "password");

        assertEquals(" test user ", user.getUserName());
    }

    @Test
    void createUserReturnsUserWithEmptyWatchHistory() {
        User user = userFactory.create("test", "pass");

        assertNull(user.getWatchHistory());
    }

    @Test
    void createUserReturnsUserWithEmptyReviews() {
        User user = userFactory.create("test", "pass");

        assertTrue(user.getReviewsByMovieId().isEmpty());
    }

    @Test
    void createUserCanAddAdditionalWatchLists() {
        User user = userFactory.create("test", "pass");
        WatchList customList = new WatchList(user, "My Custom List");

        user.addWatchList(customList);

        assertEquals(2, user.getWatchLists().size());
        assertTrue(user.getWatchLists().contains(customList));
    }
}

