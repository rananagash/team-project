package use_case.filter_movies;

import entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreMatchFilterStrategyTest {

    private GenreMatchFilterStrategy filterStrategy;

    @BeforeEach
    void setUp() {
        filterStrategy = new GenreMatchFilterStrategy();
    }

    @Test
    void testFilterWithNullMovies() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);

        // Act
        List<Movie> result = filterStrategy.filter(null, genreIds);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list when movies is null");
    }

    @Test
    void testFilterWithNullGenreIds() {
        // Arrange
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster")
        );

        // Act
        List<Movie> result = filterStrategy.filter(movies, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list when genreIds is null");
    }

    @Test
    void testFilterWithBothNull() {
        // Act
        List<Movie> result = filterStrategy.filter(null, null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list when both parameters are null");
    }

    @Test
    void testFilterWithEmptyMovies() {
        // Arrange
        List<Movie> movies = Collections.emptyList();
        List<Integer> genreIds = Arrays.asList(28, 12);

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list when movies list is empty");
    }

    @Test
    void testFilterWithEmptyGenreIds() {
        // Arrange
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster")
        );
        List<Integer> genreIds = Collections.emptyList();

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list when genreIds is empty");
    }

    @Test
    void testFilterWithMatchingMovies() {
        // Arrange
        Movie movie1 = new Movie("1", "Action Movie", "Plot",
                Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Adventure Movie", "Plot",
                Arrays.asList(12), "2023-02-01", 8.0, 0.0, "poster2.jpg");
        Movie movie3 = new Movie("3", "Comedy Movie", "Plot",
                Arrays.asList(35), "2023-03-01", 6.5, 0.0, "poster3.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2, movie3);
        List<Integer> genreIds = Arrays.asList(28, 12); // Action, Adventure

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size(), "Should filter to 2 movies");
        assertTrue(result.contains(movie1), "Should contain Action movie");
        assertTrue(result.contains(movie2), "Should contain Adventure movie");
        assertFalse(result.contains(movie3), "Should not contain Comedy movie");
    }

    @Test
    void testFilterWithMovieHavingMultipleGenres() {
        // Arrange
        Movie movie1 = new Movie("1", "Action Adventure Movie", "Plot",
                Arrays.asList(28, 12), "2023-01-01", 8.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Adventure Comedy Movie", "Plot",
                Arrays.asList(12, 35), "2023-02-01", 7.5, 0.0, "poster2.jpg");
        Movie movie3 = new Movie("3", "Drama Movie", "Plot",
                Arrays.asList(18), "2023-03-01", 9.0, 0.0, "poster3.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2, movie3);
        List<Integer> genreIds = Arrays.asList(28); // Action only

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert
        assertEquals(1, result.size(), "Should filter to 1 movie");
        assertTrue(result.contains(movie1), "Should contain movie with Action genre");
        assertFalse(result.contains(movie2), "Should not contain movie without Action");
        assertFalse(result.contains(movie3), "Should not contain Drama movie");
    }

    @Test
    void testFilterWithMovieHavingNullGenres() {
        // Arrange
        Movie movie1 = new Movie("1", "Movie 1", "Plot",
                null, "2023-01-01", 7.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Movie 2", "Plot",
                Arrays.asList(28), "2023-02-01", 8.0, 0.0, "poster2.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2);
        List<Integer> genreIds = Arrays.asList(28, 12);

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert
        assertEquals(1, result.size(), "Should filter to 1 movie");
        assertFalse(result.contains(movie1), "Should not contain movie with null genres");
        assertTrue(result.contains(movie2), "Should contain movie with matching genres");
    }

    @Test
    void testFilterWithMovieHavingEmptyGenres() {
        // Arrange
        Movie movie1 = new Movie("1", "Movie 1", "Plot",
                Collections.emptyList(), "2023-01-01", 7.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Movie 2", "Plot",
                Arrays.asList(28), "2023-02-01", 8.0, 0.0, "poster2.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2);
        List<Integer> genreIds = Arrays.asList(28, 12);

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert
        assertEquals(1, result.size(), "Should filter to 1 movie");
        assertFalse(result.contains(movie1), "Should not contain movie with empty genres");
        assertTrue(result.contains(movie2), "Should contain movie with matching genres");
    }

    @Test
    void testFilterWithSingleGenreMatch() {
        // Arrange
        Movie movie1 = new Movie("1", "Action Movie", "Plot",
                Collections.singletonList(28), "2023-01-01", 7.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Comedy Movie", "Plot",
                Collections.singletonList(35), "2023-02-01", 8.0, 0.0, "poster2.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2);
        List<Integer> genreIds = Collections.singletonList(28);

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert
        assertEquals(1, result.size(), "Should filter to 1 movie");
        assertEquals(movie1, result.get(0), "Should contain only Action movie");
    }

    @Test
    void testFilterWithNoMatches() {
        // Arrange
        Movie movie1 = new Movie("1", "Horror Movie", "Plot",
                Collections.singletonList(27), "2023-01-01", 7.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Drama Movie", "Plot",
                Collections.singletonList(18), "2023-02-01", 8.0, 0.0, "poster2.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2);
        List<Integer> genreIds = Arrays.asList(28, 12); // Action, Adventure

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list when no movies match");
    }

    @Test
    void testFilterWithAllMoviesMatching() {
        // Arrange
        Movie movie1 = new Movie("1", "Action Movie 1", "Plot",
                Arrays.asList(28, 12), "2023-01-01", 7.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Action Movie 2", "Plot",
                Arrays.asList(28), "2023-02-01", 8.0, 0.0, "poster2.jpg");
        Movie movie3 = new Movie("3", "Adventure Movie", "Plot",
                Arrays.asList(12, 28), "2023-03-01", 8.5, 0.0, "poster3.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2, movie3);
        List<Integer> genreIds = Arrays.asList(28, 12); // Action, Adventure

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert
        assertEquals(3, result.size(), "Should include all movies");
        assertTrue(result.contains(movie1));
        assertTrue(result.contains(movie2));
        assertTrue(result.contains(movie3));
    }

    @Test
    void testFilterWithMixedMoviesSomeWithGenresSomeWithout() {
        // Arrange - mix of movies with valid genres, null genres (treated as empty), and empty genres
        Movie movieWithGenres = new Movie("1", "Action Movie", "Plot",
                Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1.jpg");
        Movie movieWithNullGenres = new Movie("2", "No Genre Movie", "Plot",
                null, "2023-02-01", 8.0, 0.0, "poster2.jpg");
        Movie movieWithEmptyGenres = new Movie("3", "Empty Genre Movie", "Plot",
                Collections.emptyList(), "2023-03-01", 6.5, 0.0, "poster3.jpg");
        Movie movieWithMatchingGenres = new Movie("4", "Matching Movie", "Plot",
                Arrays.asList(12, 28), "2023-04-01", 9.0, 0.0, "poster4.jpg");
        List<Movie> movies = Arrays.asList(movieWithGenres, movieWithNullGenres, movieWithEmptyGenres, movieWithMatchingGenres);
        List<Integer> genreIds = Arrays.asList(28, 12);

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert
        assertEquals(2, result.size(), "Should only include movies with matching genres");
        assertTrue(result.contains(movieWithGenres));
        assertTrue(result.contains(movieWithMatchingGenres));
        assertFalse(result.contains(movieWithNullGenres));
        assertFalse(result.contains(movieWithEmptyGenres));
    }

    @Test
    void testFilterWhereAnyMatchReturnsTrueForFirstGenre() {
        // Arrange - movie has multiple genres, should match on first one in list
        Movie movie = new Movie("1", "Action Adventure Movie", "Plot",
                Arrays.asList(28, 12, 35), "2023-01-01", 8.0, 0.0, "poster.jpg");
        List<Movie> movies = Collections.singletonList(movie);
        List<Integer> genreIds = Arrays.asList(28); // Only Action

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert - Should match because movie has genre 28 (Action)
        assertEquals(1, result.size());
        assertTrue(result.contains(movie));
    }

    @Test
    void testFilterWhereAnyMatchReturnsTrueForLastGenre() {
        // Arrange - movie has multiple genres, should match on last one in list
        Movie movie = new Movie("1", "Adventure Comedy Movie", "Plot",
                Arrays.asList(12, 35), "2023-01-01", 8.0, 0.0, "poster.jpg");
        List<Movie> movies = Collections.singletonList(movie);
        List<Integer> genreIds = Arrays.asList(35); // Only Comedy

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert - Should match because movie has genre 35 (Comedy)
        assertEquals(1, result.size());
        assertTrue(result.contains(movie));
    }

    @Test
    void testFilterWhereAnyMatchReturnsFalse() {
        // Arrange - movie genres don't match any of the requested genres
        Movie movie = new Movie("1", "Horror Movie", "Plot",
                Arrays.asList(27), "2023-01-01", 8.0, 0.0, "poster.jpg");
        List<Movie> movies = Collections.singletonList(movie);
        List<Integer> genreIds = Arrays.asList(28, 12, 35); // Action, Adventure, Comedy

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert - Should not match because movie has genre 27 (Horror) which is not in the list
        assertTrue(result.isEmpty());
    }

    @Test
    void testFilterWithMovieHavingGenreMatchingMiddleOfGenreIdsList() {
        // Arrange - Test matching when genre is in the middle of the genreIds list
        Movie movie = new Movie("1", "Adventure Movie", "Plot",
                Collections.singletonList(12), "2023-01-01", 8.0, 0.0, "poster.jpg");
        List<Movie> movies = Collections.singletonList(movie);
        List<Integer> genreIds = Arrays.asList(28, 12, 35); // Action, Adventure (middle), Comedy

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert - Should match because movie has genre 12 (Adventure) which is in the list
        assertEquals(1, result.size());
        assertTrue(result.contains(movie));
    }

    @Test
    void testFilterWithMovieHavingMultipleGenresWhereOneMatches() {
        // Arrange - Movie has multiple genres, only one matches
        Movie movie = new Movie("1", "Mixed Genre Movie", "Plot",
                Arrays.asList(27, 28, 35), "2023-01-01", 8.0, 0.0, "poster.jpg");
        List<Movie> movies = Collections.singletonList(movie);
        List<Integer> genreIds = Arrays.asList(28); // Only Action

        // Act
        List<Movie> result = filterStrategy.filter(movies, genreIds);

        // Assert - Should match because movie has genre 28 (Action) even though it has other genres
        assertEquals(1, result.size());
        assertTrue(result.contains(movie));
    }
}

