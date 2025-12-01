package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructorAndGettersWork() {
        User user = new User("testuser", "password123");

        assertEquals("testuser", user.getUserName());
        assertEquals("password123", user.getPassword());
        assertNotNull(user.getWatchLists());
        assertEquals(1, user.getWatchLists().size()); // Default watchlist created
        assertNull(user.getWatchHistory());
        assertNotNull(user.getReviewsByMovieId());
        assertTrue(user.getReviewsByMovieId().isEmpty());
    }

    @Test
    void constructorThrowsExceptionWhenUsernameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(null, "password");
        });
    }

    @Test
    void constructorThrowsExceptionWhenUsernameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("", "password");
        });
    }

    @Test
    void constructorThrowsExceptionWhenPasswordIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("testuser", null);
        });
    }

    @Test
    void constructorThrowsExceptionWhenPasswordIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("testuser", "");
        });
    }

    @Test
    void constructorCreatesDefaultWatchList() {
        User user = new User("testuser", "password");

        assertEquals(1, user.getWatchLists().size());
        WatchList defaultList = user.getWatchLists().get(0);
        assertEquals(user, defaultList.getUser());
        assertEquals("testuser's Watch List", defaultList.getName());
    }

    @Test
    void addWatchListAddsWatchList() {
        User user = new User("testuser", "password");
        WatchList newList = new WatchList(user, "My Custom List");

        user.addWatchList(newList);

        assertEquals(2, user.getWatchLists().size());
        assertTrue(user.getWatchLists().contains(newList));
    }

    @Test
    void addWatchListThrowsExceptionWhenNull() {
        User user = new User("testuser", "password");

        assertThrows(NullPointerException.class, () -> {
            user.addWatchList(null);
        });
    }

    @Test
    void addWatchListThrowsExceptionWhenDifferentUser() {
        User user1 = new User("user1", "password");
        User user2 = new User("user2", "password");
        WatchList list = new WatchList(user2, "List");

        assertThrows(IllegalArgumentException.class, () -> {
            user1.addWatchList(list);
        });
    }

    @Test
    void clearWatchListsRemovesAllWatchLists() {
        User user = new User("testuser", "password");
        WatchList list = new WatchList(user, "Custom List");
        user.addWatchList(list);

        user.clearWatchLists();

        assertTrue(user.getWatchLists().isEmpty());
    }

    @Test
    void getWatchListByNameFindsWatchList() {
        User user = new User("testuser", "password");
        WatchList customList = new WatchList(user, "My List");
        user.addWatchList(customList);

        Optional<WatchList> found = user.getWatchListByName("My List");

        assertTrue(found.isPresent());
        assertEquals(customList, found.get());
    }

    @Test
    void getWatchListByNameIsCaseInsensitive() {
        User user = new User("testuser", "password");
        WatchList customList = new WatchList(user, "My List");
        user.addWatchList(customList);

        Optional<WatchList> found = user.getWatchListByName("my list");

        assertTrue(found.isPresent());
        assertEquals(customList, found.get());
    }

    @Test
    void getWatchListByNameReturnsEmptyWhenNotFound() {
        User user = new User("testuser", "password");

        Optional<WatchList> found = user.getWatchListByName("NonExistent");

        assertFalse(found.isPresent());
    }

    @Test
    void getWatchListByIdFindsWatchList() {
        User user = new User("testuser", "password");
        WatchList customList = new WatchList(user, "My List");
        user.addWatchList(customList);
        String listId = customList.getWatchListId();

        WatchList found = user.getWatchListById(listId);

        assertNotNull(found);
        assertEquals(customList, found);
    }

    @Test
    void getWatchListByIdReturnsNullWhenNotFound() {
        User user = new User("testuser", "password");

        WatchList found = user.getWatchListById("nonExistentId");

        assertNull(found);
    }

    @Test
    void getWatchListByIdReturnsNullWhenIdIsNull() {
        User user = new User("testuser", "password");

        WatchList found = user.getWatchListById(null);

        assertNull(found);
    }

    @Test
    void setWatchHistorySetsWatchHistory() {
        User user = new User("testuser", "password");
        WatchHistory history = new WatchHistory("history1", user);

        user.setWatchHistory(history);

        assertEquals(history, user.getWatchHistory());
    }

    @Test
    void addReviewAddsReview() {
        User user = new User("testuser", "password");
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster"
        );
        Review review = new Review("r1", user, movie, 5, "Great movie", LocalDateTime.now());

        user.addReview(review);

        Map<String, Review> reviews = user.getReviewsByMovieId();
        assertEquals(1, reviews.size());
        assertTrue(reviews.containsKey("m1"));
        assertEquals(review, reviews.get("m1"));
    }

    @Test
    void addReviewReplacesExistingReview() {
        User user = new User("testuser", "password");
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster"
        );
        Review review1 = new Review("r1", user, movie, 4, "Good", LocalDateTime.now());
        Review review2 = new Review("r2", user, movie, 5, "Great", LocalDateTime.now());

        user.addReview(review1);
        user.addReview(review2);

        Map<String, Review> reviews = user.getReviewsByMovieId();
        assertEquals(1, reviews.size());
        assertEquals(review2, reviews.get("m1")); // Should be the second review
    }

    @Test
    void addReviewThrowsExceptionWhenNull() {
        User user = new User("testuser", "password");

        assertThrows(NullPointerException.class, () -> {
            user.addReview(null);
        });
    }

    @Test
    void getReviewsByMovieIdReturnsImmutableMap() {
        User user = new User("testuser", "password");
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster"
        );
        Review review = new Review("r1", user, movie, 5, "Great", LocalDateTime.now());
        user.addReview(review);

        Map<String, Review> reviews = user.getReviewsByMovieId();

        // Attempting to modify the returned map should throw an exception
        assertThrows(UnsupportedOperationException.class, () -> {
            reviews.put("m2", review);
        });
    }
}

