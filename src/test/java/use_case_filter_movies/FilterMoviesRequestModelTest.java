package use_case_filter_movies;

import org.junit.jupiter.api.Test;
import use_case.filter_movies.FilterMoviesRequestModel;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilterMoviesRequestModelTest {

    @Test
    void testConstructorWithValidGenreIds() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12, 35);

        // Act
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);

        // Assert
        assertNotNull(requestModel);
        assertEquals(genreIds, requestModel.getGenreIds());
    }

    @Test
    void testConstructorWithEmptyList() {
        // Arrange
        List<Integer> genreIds = Collections.emptyList();

        // Act
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);

        // Assert
        assertNotNull(requestModel);
        assertTrue(requestModel.getGenreIds().isEmpty());
    }

    @Test
    void testConstructorWithNull() {
        // Arrange & Act
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(null);

        // Assert
        assertNotNull(requestModel);
        assertNull(requestModel.getGenreIds());
    }

    @Test
    void testConstructorWithSingleGenreId() {
        // Arrange
        List<Integer> genreIds = Collections.singletonList(28);

        // Act
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);

        // Assert
        assertEquals(1, requestModel.getGenreIds().size());
        assertEquals(28, requestModel.getGenreIds().get(0));
    }

    @Test
    void testGetGenreIdsReturnsSameReference() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12);
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);

        // Act
        List<Integer> returnedIds = requestModel.getGenreIds();

        // Assert
        assertSame(genreIds, returnedIds, "Should return the same reference");
    }

    @Test
    void testConstructorWithMultipleGenreIds() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12, 16, 35, 80);

        // Act
        FilterMoviesRequestModel requestModel = new FilterMoviesRequestModel(genreIds);

        // Assert
        assertEquals(5, requestModel.getGenreIds().size());
        assertTrue(requestModel.getGenreIds().contains(28));
        assertTrue(requestModel.getGenreIds().contains(12));
        assertTrue(requestModel.getGenreIds().contains(16));
        assertTrue(requestModel.getGenreIds().contains(35));
        assertTrue(requestModel.getGenreIds().contains(80));
    }
}

