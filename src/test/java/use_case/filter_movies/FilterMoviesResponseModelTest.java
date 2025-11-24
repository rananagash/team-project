package use_case.filter_movies;

import entity.Movie;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterMoviesResponseModelTest {

    @Test
    void testConstructorWithValidData() {
        // Arrange
        List<Integer> requestedGenres = Arrays.asList(28, 12);
        List<String> requestedGenreNames = Arrays.asList("Action", "Adventure");
        Movie movie1 = new Movie("1", "Movie 1", "Plot 1",
                Arrays.asList(28), "2023-01-01", 8.5, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Movie 2", "Plot 2",
                Arrays.asList(12), "2023-02-01", 7.5, 0.0, "poster2.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2);

        // Act
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, movies);

        // Assert
        assertNotNull(responseModel);
        assertEquals(requestedGenres, responseModel.getRequestedGenres());
        assertEquals(requestedGenreNames, responseModel.getRequestedGenreNames());
        assertEquals(movies, responseModel.getMovies());
    }

    @Test
    void testConstructorWithEmptyMovies() {
        // Arrange
        List<Integer> requestedGenres = Collections.singletonList(28);
        List<String> requestedGenreNames = Collections.singletonList("Action");
        List<Movie> movies = Collections.emptyList();

        // Act
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, movies);

        // Assert
        assertEquals(requestedGenres, responseModel.getRequestedGenres());
        assertEquals(requestedGenreNames, responseModel.getRequestedGenreNames());
        assertTrue(responseModel.getMovies().isEmpty());
    }

    @Test
    void testConstructorWithNullMovies() {
        // Arrange
        List<Integer> requestedGenres = Arrays.asList(28, 12);
        List<String> requestedGenreNames = Arrays.asList("Action", "Adventure");

        // Act
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, null);

        // Assert
        assertEquals(requestedGenres, responseModel.getRequestedGenres());
        assertEquals(requestedGenreNames, responseModel.getRequestedGenreNames());
        assertNull(responseModel.getMovies());
    }

    @Test
    void testGetRequestedGenres() {
        // Arrange
        List<Integer> requestedGenres = Arrays.asList(35, 80, 99);
        List<String> requestedGenreNames = Arrays.asList("Comedy", "Crime", "Documentary");
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, Collections.emptyList());

        // Act
        List<Integer> returnedGenres = responseModel.getRequestedGenres();

        // Assert
        assertEquals(requestedGenres, returnedGenres);
        assertSame(requestedGenres, returnedGenres, "Should return the same reference");
    }

    @Test
    void testGetRequestedGenreNames() {
        // Arrange
        List<Integer> requestedGenres = Arrays.asList(27, 53);
        List<String> requestedGenreNames = Arrays.asList("Horror", "Thriller");
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, Collections.emptyList());

        // Act
        List<String> returnedNames = responseModel.getRequestedGenreNames();

        // Assert
        assertEquals(requestedGenreNames, returnedNames);
        assertSame(requestedGenreNames, returnedNames, "Should return the same reference");
    }

    @Test
    void testGetMovies() {
        // Arrange
        Movie movie = new Movie("1", "Test Movie", "Test Plot",
                Collections.singletonList(28), "2023-01-01", 8.0, 0.0, "poster.jpg");
        List<Movie> movies = Collections.singletonList(movie);
        List<Integer> requestedGenres = Collections.singletonList(28);
        List<String> requestedGenreNames = Collections.singletonList("Action");
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, movies);

        // Act
        List<Movie> returnedMovies = responseModel.getMovies();

        // Assert
        assertEquals(movies, returnedMovies);
        assertSame(movies, returnedMovies, "Should return the same reference");
    }

    @Test
    void testConstructorWithMismatchedGenresAndNames() {
        // Arrange
        List<Integer> requestedGenres = Arrays.asList(28, 12);
        List<String> requestedGenreNames = Arrays.asList("Action"); // Only one name for two genres

        // Act & Assert - Should still construct successfully
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, Collections.emptyList());

        assertNotNull(responseModel);
        assertEquals(2, responseModel.getRequestedGenres().size());
        assertEquals(1, responseModel.getRequestedGenreNames().size());
    }

    @Test
    void testConstructorWithMultipleMovies() {
        // Arrange
        List<Integer> requestedGenres = Collections.singletonList(35);
        List<String> requestedGenreNames = Collections.singletonList("Comedy");
        Movie movie1 = new Movie("1", "Comedy 1", "Plot 1",
                Collections.singletonList(35), "2023-01-01", 8.0, 0.0, "poster1.jpg");
        Movie movie2 = new Movie("2", "Comedy 2", "Plot 2",
                Collections.singletonList(35), "2023-02-01", 7.5, 0.0, "poster2.jpg");
        Movie movie3 = new Movie("3", "Comedy 3", "Plot 3",
                Collections.singletonList(35), "2023-03-01", 9.0, 0.0, "poster3.jpg");
        List<Movie> movies = Arrays.asList(movie1, movie2, movie3);

        // Act
        FilterMoviesResponseModel responseModel = new FilterMoviesResponseModel(
                requestedGenres, requestedGenreNames, movies);

        // Assert
        assertEquals(3, responseModel.getMovies().size());
        assertTrue(responseModel.getMovies().contains(movie1));
        assertTrue(responseModel.getMovies().contains(movie2));
        assertTrue(responseModel.getMovies().contains(movie3));
    }
}

