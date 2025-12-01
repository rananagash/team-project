package entity;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void constructorAndGettersWork() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot",
                Arrays.asList(1, 2, 3),
                "2025-01-01",
                7.5,
                8.2,
                "poster-url"
        );

        assertEquals("m1", movie.getMovieId());
        assertEquals("Test Movie", movie.getTitle());
        assertEquals("A test movie plot", movie.getPlot());
        assertEquals(Arrays.asList(1, 2, 3), movie.getGenreIds());
        assertEquals("2025-01-01", movie.getReleaseDate());
        assertEquals(7.5, movie.getRating());
        assertEquals(0.0, movie.getPopularity()); // Note: constructor sets popularity to 0.0
        assertEquals("poster-url", movie.getPosterUrl());
    }

    @Test
    void constructorThrowsExceptionWhenMovieIdIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new Movie(
                    null,
                    "Title",
                    "Plot",
                    Collections.emptyList(),
                    "2025-01-01",
                    7.5,
                    0.0,
                    "poster"
            );
        });
    }

    @Test
    void constructorThrowsExceptionWhenTitleIsNull() {
        assertThrows(NullPointerException.class, () -> {
            new Movie(
                    "m1",
                    null,
                    "Plot",
                    Collections.emptyList(),
                    "2025-01-01",
                    7.5,
                    0.0,
                    "poster"
            );
        });
    }

    @Test
    void constructorHandlesNullGenreIds() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                null,
                "2025-01-01",
                7.5,
                0.0,
                "poster"
        );

        assertTrue(movie.getGenreIds().isEmpty());
    }

    @Test
    void constructorHandlesNullPlot() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                null,
                Collections.emptyList(),
                "2025-01-01",
                7.5,
                0.0,
                "poster"
        );

        assertNull(movie.getPlot());
    }

    @Test
    void constructorHandlesNullReleaseDate() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                Collections.emptyList(),
                null,
                7.5,
                0.0,
                "poster"
        );

        assertNull(movie.getReleaseDate());
    }

    @Test
    void constructorHandlesNullPosterUrl() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                Collections.emptyList(),
                "2025-01-01",
                7.5,
                0.0,
                null
        );

        assertNull(movie.getPosterUrl());
    }

    @Test
    void getGenreIdsReturnsImmutableList() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                Arrays.asList(1, 2, 3),
                "2025-01-01",
                7.5,
                0.0,
                "poster"
        );

        List<Integer> genreIds = movie.getGenreIds();

        // Attempting to modify the returned list should throw an exception
        assertThrows(UnsupportedOperationException.class, () -> {
            genreIds.add(4);
        });
    }

    @Test
    void updateRatingUpdatesRating() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                Collections.emptyList(),
                "2025-01-01",
                7.5,
                0.0,
                "poster"
        );

        movie.updateRating(8.5);

        assertEquals(8.5, movie.getRating());
    }

    @Test
    void getReleaseYearExtractsYearFromDate() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                Collections.emptyList(),
                "2025-01-01",
                7.5,
                0.0,
                "poster"
        );

        assertEquals(2025, movie.getReleaseYear());
    }

    @Test
    void getReleaseYearHandlesShortDate() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                Collections.emptyList(),
                "202",
                7.5,
                0.0,
                "poster"
        );

        assertNull(movie.getReleaseYear());
    }

    @Test
    void getReleaseYearHandlesNullDate() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                Collections.emptyList(),
                null,
                7.5,
                0.0,
                "poster"
        );

        assertNull(movie.getReleaseYear());
    }

    @Test
    void getReleaseYearHandlesInvalidDate() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                Collections.emptyList(),
                "abcd-01-01",
                7.5,
                0.0,
                "poster"
        );

        assertNull(movie.getReleaseYear());
    }

    @Test
    void popularityIsAlwaysZero() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                Collections.emptyList(),
                "2025-01-01",
                7.5,
                99.9, // Even if we pass a value, it should be 0.0
                "poster"
        );

        assertEquals(0.0, movie.getPopularity());
    }
}

