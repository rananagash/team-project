package entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the {@link Movie} entity class.
 */
class MovieTest {

    @Test
    void constructorAndGettersWork() {
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot",
                List.of(1, 2, 3),
                "2020-01-15",
                7.5,
                12.3,
                "https://example.com/poster.jpg"
        );

        assertEquals("m1", movie.getMovieId());
        assertEquals("Test Movie", movie.getTitle());
        assertEquals("A test movie plot", movie.getPlot());
        assertEquals(List.of(1, 2, 3), movie.getGenreIds());
        assertEquals("2020-01-15", movie.getReleaseDate());
        assertEquals(7.5, movie.getRating(), 0.001);
        assertEquals(12.3, movie.getPopularity(), 0.001);
        assertEquals("https://example.com/poster.jpg", movie.getPosterUrl());
    }

    @Test
    void constructorWithNullMovieIdThrows() {
        assertThrows(NullPointerException.class, () -> {
            new Movie(null, "Title", "Plot", List.of(), "2020-01-01", 5.0, 10.0, "url");
        });
    }

    @Test
    void constructorWithNullTitleThrows() {
        assertThrows(NullPointerException.class, () -> {
            new Movie("m1", null, "Plot", List.of(), "2020-01-01", 5.0, 10.0, "url");
        });
    }

    @Test
    void constructorWithNullGenreIdsUsesEmptyList() {
        Movie movie = new Movie(
                "m1",
                "Title",
                "Plot",
                null,
                "2020-01-01",
                5.0,
                10.0,
                "url"
        );

        assertTrue(movie.getGenreIds().isEmpty());
    }

    @Test
    void constructorWithNullPlotIsAllowed() {
        Movie movie = new Movie(
                "m1",
                "Title",
                null,
                List.of(),
                "2020-01-01",
                5.0,
                10.0,
                "url"
        );

        assertNull(movie.getPlot());
    }

    @Test
    void constructorWithNullReleaseDateIsAllowed() {
        Movie movie = new Movie(
                "m1",
                "Title",
                "Plot",
                List.of(),
                null,
                5.0,
                10.0,
                "url"
        );

        assertNull(movie.getReleaseDate());
    }

    @Test
    void constructorWithNullPosterUrlIsAllowed() {
        Movie movie = new Movie(
                "m1",
                "Title",
                "Plot",
                List.of(),
                "2020-01-01",
                5.0,
                10.0,
                null
        );

        assertNull(movie.getPosterUrl());
    }

    @Test
    void getGenreIdsReturnsUnmodifiableList() {
        Movie movie = new Movie(
                "m1",
                "Title",
                "Plot",
                List.of(1, 2),
                "2020-01-01",
                5.0,
                10.0,
                "url"
        );

        List<Integer> genres = movie.getGenreIds();

        assertThrows(UnsupportedOperationException.class, () -> {
            genres.add(3);
        });
    }

    @Test
    void getReleaseYearExtractsYearFromDate() {
        Movie movie = new Movie(
                "m1",
                "Title",
                "Plot",
                List.of(),
                "2020-01-15",
                5.0,
                10.0,
                "url"
        );

        assertEquals(Integer.valueOf(2020), movie.getReleaseYear());
    }

    @Test
    void getReleaseYearWithNullDateReturnsNull() {
        Movie movie = new Movie(
                "m1",
                "Title",
                "Plot",
                List.of(),
                null,
                5.0,
                10.0,
                "url"
        );

        assertNull(movie.getReleaseYear());
    }

    @Test
    void getReleaseYearWithShortDateReturnsNull() {
        Movie movie = new Movie(
                "m1",
                "Title",
                "Plot",
                List.of(),
                "202",
                5.0,
                10.0,
                "url"
        );

        assertNull(movie.getReleaseYear());
    }

    @Test
    void getReleaseYearWithInvalidDateReturnsNull() {
        Movie movie = new Movie(
                "m1",
                "Title",
                "Plot",
                List.of(),
                "abcd-01-01",
                5.0,
                10.0,
                "url"
        );

        assertNull(movie.getReleaseYear());
    }

    @Test
    void updateRatingChangesRating() {
        Movie movie = new Movie(
                "m1",
                "Title",
                "Plot",
                List.of(),
                "2020-01-01",
                5.0,
                10.0,
                "url"
        );

        movie.updateRating(8.5);

        assertEquals(8.5, movie.getRating(), 0.001);
    }

    @Test
    void popularityIsPreservedFromConstructor() {
        Movie movie = new Movie(
                "m1",
                "Title",
                "Plot",
                List.of(),
                "2020-01-01",
                5.0,
                15.7,
                "url"
        );

        assertEquals(15.7, movie.getPopularity(), 0.001);
    }

    @Test
    void movieWithAllFieldsSet() {
        Movie movie = new Movie(
                "m1",
                "The Matrix",
                "A computer hacker learns about the true nature of reality",
                List.of(28, 878),
                "1999-03-31",
                8.7,
                45.2,
                "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg"
        );

        assertEquals("m1", movie.getMovieId());
        assertEquals("The Matrix", movie.getTitle());
        assertEquals("A computer hacker learns about the true nature of reality", movie.getPlot());
        assertEquals(List.of(28, 878), movie.getGenreIds());
        assertEquals("1999-03-31", movie.getReleaseDate());
        assertEquals(Integer.valueOf(1999), movie.getReleaseYear());
        assertEquals(8.7, movie.getRating(), 0.001);
        assertEquals(45.2, movie.getPopularity(), 0.001);
        assertEquals("https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg", movie.getPosterUrl());
    }
}

