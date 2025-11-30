package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link User} entity class.
 */
class UserTest {

    private User user;
    private Movie testMovie;

    @BeforeEach
    void setUp() {
        user = new User("testuser", "password123");
        testMovie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot",
                List.of(1, 2),
                "2020-01-01",
                7.5,
                10.0,
                "poster-url"
        );
    }

    @Test
    void constructorCreatesUserWithDefaultWatchList() {
        assertEquals("testuser", user.getUserName());
        assertEquals("password123", user.getPassword());
        assertEquals(1, user.getWatchLists().size());
        assertEquals("testuser's Watch List", user.getWatchLists().get(0).getName());
    }

    @Test
    void constructorWithEmptyUsernameThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("", "password");
        });
    }

    @Test
    void constructorWithEmptyPasswordThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User("username", "");
        });
    }

    @Test
    void addWatchListAddsNewWatchList() {
        WatchList newList = new WatchList(user, "My Custom List");

        user.addWatchList(newList);

        assertEquals(2, user.getWatchLists().size());
        assertTrue(user.getWatchLists().contains(newList));
    }

    @Test
    void addWatchListWithNullThrows() {
        assertThrows(NullPointerException.class, () -> {
            user.addWatchList(null);
        });
    }

    @Test
    void addWatchListWithDifferentUserThrows() {
        User otherUser = new User("otheruser", "pass");
        WatchList otherList = new WatchList(otherUser, "Other List");

        assertThrows(IllegalArgumentException.class, () -> {
            user.addWatchList(otherList);
        });
    }

    @Test
    void getWatchListByNameFindsExistingList() {
        WatchList customList = new WatchList(user, "My Custom List");
        user.addWatchList(customList);

        Optional<WatchList> found = user.getWatchListByName("My Custom List");

        assertTrue(found.isPresent());
        assertEquals(customList, found.get());
    }

    @Test
    void getWatchListByNameIsCaseInsensitive() {
        WatchList customList = new WatchList(user, "My Custom List");
        user.addWatchList(customList);

        Optional<WatchList> found = user.getWatchListByName("my custom list");

        assertTrue(found.isPresent());
        assertEquals(customList, found.get());
    }

    @Test
    void getWatchListByNameReturnsEmptyForNonExistent() {
        Optional<WatchList> found = user.getWatchListByName("Non Existent List");

        assertFalse(found.isPresent());
    }

    @Test
    void clearWatchListsRemovesAllLists() {
        WatchList list1 = new WatchList(user, "List 1");
        WatchList list2 = new WatchList(user, "List 2");
        user.addWatchList(list1);
        user.addWatchList(list2);

        user.clearWatchLists();

        assertTrue(user.getWatchLists().isEmpty());
    }

    @Test
    void setWatchHistorySetsHistory() {
        WatchHistory history = new WatchHistory("history1", user);

        user.setWatchHistory(history);

        assertEquals(history, user.getWatchHistory());
    }

    @Test
    void setWatchHistoryWithNullIsAllowed() {
        WatchHistory history = new WatchHistory("history1", user);
        user.setWatchHistory(history);

        user.setWatchHistory(null);

        assertNull(user.getWatchHistory());
    }

    @Test
    void addReviewStoresReviewByMovieId() {
        Review review = new Review("r1", user, testMovie, 5, "Great movie!", null);

        user.addReview(review);

        Map<String, Review> reviews = user.getReviewsByMovieId();
        assertTrue(reviews.containsKey("m1"));
        assertEquals(review, reviews.get("m1"));
    }

    @Test
    void addReviewReplacesExistingReviewForSameMovie() {
        Review review1 = new Review("r1", user, testMovie, 4, "Good", null);
        Review review2 = new Review("r2", user, testMovie, 5, "Great!", null);

        user.addReview(review1);
        user.addReview(review2);

        Map<String, Review> reviews = user.getReviewsByMovieId();
        assertEquals(1, reviews.size());
        assertEquals(review2, reviews.get("m1"));
    }

    @Test
    void addReviewWithNullThrows() {
        assertThrows(NullPointerException.class, () -> {
            user.addReview(null);
        });
    }

    @Test
    void getReviewsByMovieIdReturnsImmutableMap() {
        Review review = new Review("r1", user, testMovie, 5, "Great!", null);
        user.addReview(review);

        Map<String, Review> reviews = user.getReviewsByMovieId();

        assertThrows(UnsupportedOperationException.class, () -> {
            reviews.put("m2", review);
        });
    }

    @Test
    void userCanHaveMultipleReviewsForDifferentMovies() {
        Movie movie2 = new Movie("m2", "Movie 2", "Plot 2", List.of(), "2021-01-01", 8.0, 10.0, "url2");
        Review review1 = new Review("r1", user, testMovie, 5, "Great!", null);
        Review review2 = new Review("r2", user, movie2, 4, "Good", null);

        user.addReview(review1);
        user.addReview(review2);

        Map<String, Review> reviews = user.getReviewsByMovieId();
        assertEquals(2, reviews.size());
        assertEquals(review1, reviews.get("m1"));
        assertEquals(review2, reviews.get("m2"));
    }

    @Test
    void defaultWatchListBelongsToUser() {
        WatchList defaultList = user.getWatchLists().get(0);

        assertEquals(user, defaultList.getUser());
    }
}

