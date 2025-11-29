package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReviewTest {

    private final User user = new User("user1", "password");
    private final Movie movie = new Movie(
            "m1",
            "Some Title",
            "Plot",
            List.of(1, 2),
            "2020-01-01",
            7.3,
            12.0,
            null
    );

    @Test
    void validReviewCreatesSuccessfully() {
        LocalDateTime now = LocalDateTime.now();
        Review r = new Review("r1", user, movie, 4, "Nice movie", now);

        assertEquals("r1", r.getReviewId());
        assertEquals(user, r.getUser());
        assertEquals(movie, r.getMovie());
        assertEquals(4, r.getRating());
        assertEquals("Nice movie", r.getComment());
        assertEquals(now, r.getCreatedAt());
    }

    @Test
    void ratingOutOfRangeThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new Review("r1", user, movie, 0, "x", LocalDateTime.now()));
        assertThrows(IllegalArgumentException.class,
                () -> new Review("r1", user, movie, 6, "x", LocalDateTime.now()));
    }

    @Test
    void nullIdThrows() {
        assertThrows(NullPointerException.class,
                () -> new Review(null, user, movie, 3, "x", LocalDateTime.now()));
    }

    @Test
    void nullUserThrows() {
        assertThrows(NullPointerException.class,
                () -> new Review("r1", null, movie, 3, "x", LocalDateTime.now()));
    }

    @Test
    void nullMovieThrows() {
        assertThrows(NullPointerException.class,
                () -> new Review("r1", user, null, 3, "x", LocalDateTime.now()));
    }

    @Test
    void nullCommentBecomesEmptyString() {
        Review r = new Review("r1", user, movie, 4, null, LocalDateTime.now());
        assertEquals("", r.getComment());
    }

    @Test
    void nullCreatedAtDefaultsToNow() {
        Review r = new Review("r1", user, movie, 5, "ok", null);
        LocalDateTime now = LocalDateTime.now();

        // createdAt should be very close to now
        assertFalse(r.getCreatedAt().isAfter(now));
        assertFalse(r.getCreatedAt().isBefore(now.minusSeconds(2)));
    }

    @Test
    void addingReviewToUserStoresByMovieId() {
        Review r = new Review("r1", user, movie, 5, "great", LocalDateTime.now());
        user.addReview(r);

        assertTrue(user.getReviewsByMovieId().containsKey(movie.getMovieId()));
        assertSame(r, user.getReviewsByMovieId().get(movie.getMovieId()));
    }
}