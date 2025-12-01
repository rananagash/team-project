package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WatchedMovieTest {

    @Test
    void constructorWithMovieAndDateWorks() {
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
        LocalDateTime watchedDate = LocalDateTime.now().minusDays(1);

        WatchedMovie watchedMovie = new WatchedMovie(movie, watchedDate);

        assertEquals("m1", watchedMovie.getMovieId());
        assertEquals("Test Movie", watchedMovie.getTitle());
        assertEquals("A test movie plot", watchedMovie.getPlot());
        assertEquals(List.of(1, 2), watchedMovie.getGenreIds());
        assertEquals("2025-01-01", watchedMovie.getReleaseDate());
        assertEquals(7.5, watchedMovie.getRating());
        assertEquals(0.0, watchedMovie.getPopularity());
        assertEquals("poster-url", watchedMovie.getPosterUrl());
        assertEquals(watchedDate, watchedMovie.getWatchedDate());
    }

    @Test
    void constructorWithMovieAndNullDateUsesCurrentTime() {
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

        LocalDateTime before = LocalDateTime.now();
        WatchedMovie watchedMovie = new WatchedMovie(movie, null);
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(watchedMovie.getWatchedDate());
        assertFalse(watchedMovie.getWatchedDate().isBefore(before.minusSeconds(1)));
        assertFalse(watchedMovie.getWatchedDate().isAfter(after.plusSeconds(1)));
    }

    @Test
    void constructorWithAllParametersWorks() {
        LocalDateTime watchedDate = LocalDateTime.now().minusDays(2);

        WatchedMovie watchedMovie = new WatchedMovie(
                "m1",
                "Test Movie",
                "A test movie plot",
                List.of(1, 2, 3),
                "2025-01-01",
                7.5,
                0.0,
                "poster-url",
                watchedDate
        );

        assertEquals("m1", watchedMovie.getMovieId());
        assertEquals("Test Movie", watchedMovie.getTitle());
        assertEquals("A test movie plot", watchedMovie.getPlot());
        assertEquals(List.of(1, 2, 3), watchedMovie.getGenreIds());
        assertEquals("2025-01-01", watchedMovie.getReleaseDate());
        assertEquals(7.5, watchedMovie.getRating());
        assertEquals(0.0, watchedMovie.getPopularity());
        assertEquals("poster-url", watchedMovie.getPosterUrl());
        assertEquals(watchedDate, watchedMovie.getWatchedDate());
    }

    @Test
    void constructorWithAllParametersAndNullDateUsesCurrentTime() {
        LocalDateTime before = LocalDateTime.now();
        WatchedMovie watchedMovie = new WatchedMovie(
                "m1",
                "Test Movie",
                "Plot",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster",
                null
        );
        LocalDateTime after = LocalDateTime.now();

        assertNotNull(watchedMovie.getWatchedDate());
        assertFalse(watchedMovie.getWatchedDate().isBefore(before.minusSeconds(1)));
        assertFalse(watchedMovie.getWatchedDate().isAfter(after.plusSeconds(1)));
    }

    @Test
    void getWatchedDateReturnsCorrectDate() {
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
        LocalDateTime specificDate = LocalDateTime.of(2024, 12, 25, 10, 30);

        WatchedMovie watchedMovie = new WatchedMovie(movie, specificDate);

        assertEquals(specificDate, watchedMovie.getWatchedDate());
    }

    @Test
    void inheritsMovieProperties() {
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
        LocalDateTime watchedDate = LocalDateTime.now();

        WatchedMovie watchedMovie = new WatchedMovie(movie, watchedDate);

        // Verify all Movie properties are accessible
        assertEquals(movie.getMovieId(), watchedMovie.getMovieId());
        assertEquals(movie.getTitle(), watchedMovie.getTitle());
        assertEquals(movie.getPlot(), watchedMovie.getPlot());
        assertEquals(movie.getGenreIds(), watchedMovie.getGenreIds());
        assertEquals(movie.getReleaseDate(), watchedMovie.getReleaseDate());
        assertEquals(movie.getRating(), watchedMovie.getRating());
        assertEquals(movie.getPopularity(), watchedMovie.getPopularity());
        assertEquals(movie.getPosterUrl(), watchedMovie.getPosterUrl());
    }

    @Test
    void updateRatingWorksOnWatchedMovie() {
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
        WatchedMovie watchedMovie = new WatchedMovie(movie, LocalDateTime.now());

        watchedMovie.updateRating(8.5);

        assertEquals(8.5, watchedMovie.getRating());
    }
}

