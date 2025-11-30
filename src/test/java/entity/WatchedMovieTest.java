package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link WatchedMovie} entity class.
 */
class WatchedMovieTest {

    private Movie baseMovie;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        baseMovie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot",
                List.of(1, 2),
                "2020-01-01",
                7.5,
                12.3,
                "https://example.com/poster.jpg"
        );
    }

    @Test
    void constructorFromMoviePreservesAllProperties() {
        LocalDateTime watchedAt = LocalDateTime.of(2023, 6, 15, 20, 30);
        WatchedMovie watchedMovie = new WatchedMovie(baseMovie, watchedAt);

        assertEquals("m1", watchedMovie.getMovieId());
        assertEquals("Test Movie", watchedMovie.getTitle());
        assertEquals("A test movie plot", watchedMovie.getPlot());
        assertEquals(List.of(1, 2), watchedMovie.getGenreIds());
        assertEquals("2020-01-01", watchedMovie.getReleaseDate());
        assertEquals(7.5, watchedMovie.getRating(), 0.001);
        assertEquals(12.3, watchedMovie.getPopularity(), 0.001);
        assertEquals("https://example.com/poster.jpg", watchedMovie.getPosterUrl());
        assertEquals(watchedAt, watchedMovie.getWatchedDate());
    }

    @Test
    void constructorFromMovieWithNullWatchedDateUsesCurrentTime() {
        LocalDateTime before = LocalDateTime.now();
        WatchedMovie watchedMovie = new WatchedMovie(baseMovie, null);
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(watchedMovie.getWatchedDate());
        assertFalse(watchedMovie.getWatchedDate().isBefore(before.minusSeconds(1)));
        assertFalse(watchedMovie.getWatchedDate().isAfter(after.plusSeconds(1)));
    }

    @Test
    void constructorFromMovieWithNullMovieThrows() {
        assertThrows(NullPointerException.class, () -> {
            new WatchedMovie(null, LocalDateTime.now());
        });
    }

    @Test
    void directConstructorWorks() {
        LocalDateTime watchedAt = LocalDateTime.of(2023, 6, 15, 20, 30);
        WatchedMovie watchedMovie = new WatchedMovie(
                "m1",
                "Test Movie",
                "A test movie plot",
                List.of(1, 2),
                "2020-01-01",
                7.5,
                12.3,
                "https://example.com/poster.jpg",
                watchedAt
        );

        assertEquals("m1", watchedMovie.getMovieId());
        assertEquals("Test Movie", watchedMovie.getTitle());
        assertEquals(watchedAt, watchedMovie.getWatchedDate());
    }

    @Test
    void directConstructorWithNullWatchedDateUsesCurrentTime() {
        LocalDateTime before = LocalDateTime.now();
        WatchedMovie watchedMovie = new WatchedMovie(
                "m1",
                "Test Movie",
                "Plot",
                List.of(),
                "2020-01-01",
                7.5,
                10.0,
                "url",
                null
        );
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(watchedMovie.getWatchedDate());
        assertFalse(watchedMovie.getWatchedDate().isBefore(before.minusSeconds(1)));
        assertFalse(watchedMovie.getWatchedDate().isAfter(after.plusSeconds(1)));
    }

    @Test
    void directConstructorWithNullMovieIdThrows() {
        assertThrows(NullPointerException.class, () -> {
            new WatchedMovie(
                    null,
                    "Title",
                    "Plot",
                    List.of(),
                    "2020-01-01",
                    7.5,
                    10.0,
                    "url",
                    LocalDateTime.now()
            );
        });
    }

    @Test
    void directConstructorWithNullTitleThrows() {
        assertThrows(NullPointerException.class, () -> {
            new WatchedMovie(
                    "m1",
                    null,
                    "Plot",
                    List.of(),
                    "2020-01-01",
                    7.5,
                    10.0,
                    "url",
                    LocalDateTime.now()
            );
        });
    }

    @Test
    void watchedMovieInheritsMovieMethods() {
        LocalDateTime watchedAt = LocalDateTime.of(2023, 6, 15, 20, 30);
        WatchedMovie watchedMovie = new WatchedMovie(baseMovie, watchedAt);

        // Test inherited methods
        assertEquals(Integer.valueOf(2020), watchedMovie.getReleaseYear());
        assertEquals(7.5, watchedMovie.getRating(), 0.001);
        assertEquals(12.3, watchedMovie.getPopularity(), 0.001);

        // Test updateRating works
        watchedMovie.updateRating(8.0);
        assertEquals(8.0, watchedMovie.getRating(), 0.001);
    }

    @Test
    void watchedMovieCanBeCreatedWithNullFields() {
        Movie movieWithNulls = new Movie(
                "m1",
                "Title",
                null,
                null,
                null,
                5.0,
                10.0,
                null
        );

        LocalDateTime watchedAt = LocalDateTime.now();
        WatchedMovie watchedMovie = new WatchedMovie(movieWithNulls, watchedAt);

        assertNull(watchedMovie.getPlot());
        assertTrue(watchedMovie.getGenreIds().isEmpty());
        assertNull(watchedMovie.getReleaseDate());
        assertNull(watchedMovie.getPosterUrl());
        assertEquals(watchedAt, watchedMovie.getWatchedDate());
    }

    @Test
    void multipleWatchedMoviesCanHaveSameBaseMovie() {
        LocalDateTime date1 = LocalDateTime.of(2023, 1, 1, 10, 0);
        LocalDateTime date2 = LocalDateTime.of(2023, 2, 1, 10, 0);

        WatchedMovie watched1 = new WatchedMovie(baseMovie, date1);
        WatchedMovie watched2 = new WatchedMovie(baseMovie, date2);

        assertEquals(baseMovie.getMovieId(), watched1.getMovieId());
        assertEquals(baseMovie.getMovieId(), watched2.getMovieId());
        assertEquals(date1, watched1.getWatchedDate());
        assertEquals(date2, watched2.getWatchedDate());
    }
}

