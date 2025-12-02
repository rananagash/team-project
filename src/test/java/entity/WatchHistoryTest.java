package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WatchHistoryTest {

    @Test
    void constructorAndGettersWork() {
        User user = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory("history1", user);

        assertEquals("history1", watchHistory.getWatchHistoryId());
        assertEquals(user, watchHistory.getUser());
        assertNotNull(watchHistory.getMovies());
        assertTrue(watchHistory.getMovies().isEmpty());
    }

    @Test
    void constructorThrowsExceptionWhenWatchHistoryIdIsNull() {
        User user = new User("testuser", "1234");
        assertThrows(NullPointerException.class, () -> {
            new WatchHistory(null, user);
        });
    }

    @Test
    void constructorThrowsExceptionWhenUserIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new WatchHistory("history1", null);
        });
    }

    @Test
    void getMoviesReturnsImmutableList() {
        User user = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory("history1", user);

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

        watchHistory.recordMovie(movie, LocalDateTime.now());
        List<WatchedMovie> movies = watchHistory.getMovies();

        // Attempting to modify the returned list should throw an exception
        assertThrows(UnsupportedOperationException.class, () -> {
            movies.add(new WatchedMovie(movie, LocalDateTime.now()));
        });
    }

    @Test
    void addWatchedMovieAddsMovie() {
        User user = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory("history1", user);

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
        WatchedMovie watchedMovie = new WatchedMovie(movie, watchedAt);

        watchHistory.addWatchedMovie(watchedMovie);

        assertEquals(1, watchHistory.getMovies().size());
        assertEquals(watchedMovie, watchHistory.getMovies().get(0));
        assertEquals("Test Movie", watchHistory.getMovies().get(0).getTitle());
        assertEquals(watchedAt, watchHistory.getMovies().get(0).getWatchedDate());
    }

    @Test
    void addWatchedMovieThrowsExceptionWhenNull() {
        User user = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory("history1", user);

        assertThrows(NullPointerException.class, () -> {
            watchHistory.addWatchedMovie(null);
        });
    }

    @Test
    void recordMovieCreatesAndAddsWatchedMovie() {
        User user = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory("history1", user);

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
        WatchedMovie recordedMovie = watchHistory.recordMovie(movie, watchedAt);

        assertNotNull(recordedMovie);
        assertEquals(1, watchHistory.getMovies().size());
        assertEquals(recordedMovie, watchHistory.getMovies().get(0));
        assertEquals("Test Movie", recordedMovie.getTitle());
        assertEquals(watchedAt, recordedMovie.getWatchedDate());
    }

    @Test
    void recordMovieAllowsMultipleMovies() {
        User user = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory("history1", user);

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

        assertEquals(2, watchHistory.getMovies().size());
        assertEquals("Movie 1", watchHistory.getMovies().get(0).getTitle());
        assertEquals("Movie 2", watchHistory.getMovies().get(1).getTitle());
    }

    @Test
    void recordMovieAllowsSameMovieMultipleTimes() {
        User user = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory("history1", user);

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

        watchHistory.recordMovie(movie, firstWatch);
        watchHistory.recordMovie(movie, secondWatch);

        assertEquals(2, watchHistory.getMovies().size());
        assertEquals(firstWatch, watchHistory.getMovies().get(0).getWatchedDate());
        assertEquals(secondWatch, watchHistory.getMovies().get(1).getWatchedDate());
    }

    @Test
    void recordMovieWithNullWatchedAtUsesCurrentTime() {
        User user = new User("testuser", "1234");
        WatchHistory watchHistory = new WatchHistory("history1", user);

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

        LocalDateTime beforeRecord = LocalDateTime.now();
        WatchedMovie recordedMovie = watchHistory.recordMovie(movie, null);
        LocalDateTime afterRecord = LocalDateTime.now();

        assertNotNull(recordedMovie.getWatchedDate());
        assertTrue(recordedMovie.getWatchedDate().isAfter(beforeRecord.minusSeconds(1)));
        assertTrue(recordedMovie.getWatchedDate().isBefore(afterRecord.plusSeconds(1)));
    }

    @Test
    void removeMovieByMovieIdReturnsFalseWhenIdNull() {
        User user = new User("test", "1234");
        WatchHistory history = new WatchHistory("h1", user);

        assertFalse(history.removeMovieByMovieId(null));
    }

    @Test
    void removeMovieByMovieIdReturnsFalseWhenNotFound() {
        User user = new User("test", "1234");
        WatchHistory history = new WatchHistory("h1", user);

        assertFalse(history.removeMovieByMovieId("missing"));
    }

    @Test
    void removeMovieByMovieIdRemovesMovie() {
        User user = new User("test", "1234");
        WatchHistory history = new WatchHistory("h1", user);

        Movie movie = new Movie(
                "m1", "Test", "Plot", List.of(1),
                "2025", 7.0, 0.0, "poster"
        );

        history.recordMovie(movie, LocalDateTime.now());

        assertTrue(history.removeMovieByMovieId("m1"));
        assertTrue(history.getMovies().isEmpty());
    }

    @Test
    void removeMovieReturnsFalseWhenNull() {
        User user = new User("test", "1234");
        WatchHistory history = new WatchHistory("h1", user);

        assertFalse(history.removeMovie(null));
    }

    @Test
    void removeMovieReturnsFalseWhenNotPresent() {
        User user = new User("test", "1234");
        WatchHistory history = new WatchHistory("h1", user);

        Movie movie = new Movie(
                "m1", "Test", "Plot", List.of(1),
                "2025", 7.0, 0.0, "poster"
        );
        WatchedMovie wm = new WatchedMovie(movie, LocalDateTime.now());

        assertFalse(history.removeMovie(wm));
    }

    @Test
    void removeMovieRemovesExistingMovie() {
        User user = new User("test", "1234");
        WatchHistory history = new WatchHistory("h1", user);

        Movie movie = new Movie(
                "m1", "Test", "Plot", List.of(1),
                "2025", 7.0, 0.0, "poster"
        );
        WatchedMovie wm = new WatchedMovie(movie, LocalDateTime.now());

        history.addWatchedMovie(wm);

        assertTrue(history.removeMovie(wm));
        assertTrue(history.getMovies().isEmpty());
    }
}

