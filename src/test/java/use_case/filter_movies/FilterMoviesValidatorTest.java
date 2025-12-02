package use_case.filter_movies;

import entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterMoviesValidatorTest {

    private FilterMoviesValidator validator;

    @BeforeEach
    void setUp() {
        validator = new FilterMoviesValidator();
    }

    @Test
    void testValidateWithNullRequestModel() {
        // Act
        String result = validator.validate(null);

        // Assert
        assertNotNull(result);
        assertEquals("Request cannot be null.", result);
    }

    @Test
    void testValidateWithNullGenreIds() {
        // Arrange
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster")
        );
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(null, movies);

        // Act
        String result = validator.validate(requestModel);

        // Assert
        assertNotNull(result);
        assertEquals("Select at least one genre.", result);
    }

    @Test
    void testValidateWithEmptyGenreIds() {
        // Arrange
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster")
        );
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(Collections.emptyList(), movies);

        // Act
        String result = validator.validate(requestModel);

        // Assert
        assertNotNull(result);
        assertEquals("Select at least one genre.", result);
    }

    @Test
    void testValidateWithNullMovies() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, null);

        // Act
        String result = validator.validate(requestModel);

        // Assert
        assertNotNull(result);
        assertEquals("No movies to filter.", result);
    }

    @Test
    void testValidateWithEmptyMovies() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, Collections.emptyList());

        // Act
        String result = validator.validate(requestModel);

        // Assert
        assertNotNull(result);
        assertEquals("No movies to filter.", result);
    }

    @Test
    void testValidateWithValidRequest() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster")
        );
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, movies);

        // Act
        String result = validator.validate(requestModel);

        // Assert
        assertNull(result, "Validation should pass with valid request");
    }

    @Test
    void testValidateWithSingleGenreAndSingleMovie() {
        // Arrange
        List<Integer> genreIds = Collections.singletonList(28);
        List<Movie> movies = Collections.singletonList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster")
        );
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, movies);

        // Act
        String result = validator.validate(requestModel);

        // Assert
        assertNull(result, "Validation should pass with single genre and movie");
    }

    @Test
    void testValidateWithMultipleGenresAndMultipleMovies() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12, 35);
        List<Movie> movies = Arrays.asList(
                new Movie("1", "Movie 1", "Plot", Arrays.asList(28), "2023-01-01", 7.5, 0.0, "poster1"),
                new Movie("2", "Movie 2", "Plot", Arrays.asList(12), "2023-02-01", 8.0, 0.0, "poster2"),
                new Movie("3", "Movie 3", "Plot", Arrays.asList(35), "2023-03-01", 6.5, 0.0, "poster3")
        );
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds, movies);

        // Act
        String result = validator.validate(requestModel);

        // Assert
        assertNull(result, "Validation should pass with multiple genres and movies");
    }
}

