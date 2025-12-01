package use_case.filter_movies;

import entity.Movie;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterMoviesRequestModelTest {

    @Test
    void testConstructorWithValidGenreIds() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12, 35);
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1"),
                new Movie("2", "Movie 2", "Plot", Arrays.asList(12), "2023-02-01", 8.0, 0.0, "poster2")
        );

        // Act
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, movies);

        // Assert
        assertNotNull(requestModel);
        assertEquals(genreIds, requestModel.getGenreIds());
        assertEquals(movies, requestModel.getMoviesToFilter());
    }

    @Test
    void testConstructorWithEmptyList() {
        // Arrange
        List<Integer> genreIds = Collections.emptyList();
        List<Movie> movies = Collections.emptyList();

        // Act
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, movies);

        // Assert
        assertNotNull(requestModel);
        assertTrue(requestModel.getGenreIds().isEmpty());
        assertTrue(requestModel.getMoviesToFilter().isEmpty());
    }

    @Test
    void testConstructorWithNull() {
        // Arrange & Act
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(null, null);

        // Assert
        assertNotNull(requestModel);
        assertNull(requestModel.getGenreIds());
        assertNull(requestModel.getMoviesToFilter());
    }

    @Test
    void testConstructorWithSingleGenreId() {
        // Arrange
        List<Integer> genreIds = Collections.singletonList(28);
        List<Movie> movies = Collections.singletonList(
                new Movie("1", "Movie", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster")
        );

        // Act
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, movies);

        // Assert
        assertEquals(1, requestModel.getGenreIds().size());
        assertEquals(28, requestModel.getGenreIds().get(0));
        assertEquals(1, requestModel.getMoviesToFilter().size());
    }

    @Test
    void testGetGenreIdsReturnsSameReference() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);
        List<Movie> movies = Collections.emptyList();
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, movies);

        // Act
        List<Integer> returnedIds = requestModel.getGenreIds();

        // Assert
        assertSame(genreIds, returnedIds, "Should return the same reference");
    }

    @Test
    void testConstructorWithMultipleGenreIds() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12, 16, 35, 80);
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1"),
                new Movie("2", "Movie 2", "Plot", Arrays.asList(12), "2023-02-01", 8.0, 0.0, "poster2")
        );

        // Act
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, movies);

        // Assert
        assertEquals(5, requestModel.getGenreIds().size());
        assertTrue(requestModel.getGenreIds().contains(28));
        assertTrue(requestModel.getGenreIds().contains(12));
        assertTrue(requestModel.getGenreIds().contains(16));
        assertTrue(requestModel.getGenreIds().contains(35));
        assertTrue(requestModel.getGenreIds().contains(80));
        assertEquals(2, requestModel.getMoviesToFilter().size());
    }
}

