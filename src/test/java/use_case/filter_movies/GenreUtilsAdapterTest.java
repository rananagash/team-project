package use_case.filter_movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreUtilsAdapterTest {

    private GenreUtilsAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new GenreUtilsAdapter();
    }

    @Test
    void testConvertGenreIdsToNamesWithValidIds() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12, 35); // Action, Adventure, Comedy

        // Act
        List<String> result = adapter.convertGenreIdsToNames(genreIds);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Action", result.get(0));
        assertEquals("Adventure", result.get(1));
        assertEquals("Comedy", result.get(2));
    }

    @Test
    void testConvertGenreIdsToNamesWithSingleId() {
        // Arrange
        List<Integer> genreIds = Collections.singletonList(27); // Horror

        // Act
        List<String> result = adapter.convertGenreIdsToNames(genreIds);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Horror", result.get(0));
    }

    @Test
    void testConvertGenreIdsToNamesWithUnknownId() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 999); // Action, Unknown

        // Act
        List<String> result = adapter.convertGenreIdsToNames(genreIds);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Action", result.get(0));
        assertEquals("Unknown Genre (999)", result.get(1));
    }

    @Test
    void testConvertGenreIdsToNamesWithNullId() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, null, 35);

        // Act
        List<String> result = adapter.convertGenreIdsToNames(genreIds);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Action", result.get(0));
        assertEquals("Unknown Genre", result.get(1));
        assertEquals("Comedy", result.get(2));
    }

    @Test
    void testConvertGenreIdsToNamesWithEmptyList() {
        // Arrange
        List<Integer> genreIds = Collections.emptyList();

        // Act
        List<String> result = adapter.convertGenreIdsToNames(genreIds);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testConvertGenreIdsToNamesWithMultipleKnownGenres() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(28, 12, 16, 35, 80, 99, 18); // Action, Adventure, Animation, Comedy, Crime, Documentary, Drama

        // Act
        List<String> result = adapter.convertGenreIdsToNames(genreIds);

        // Assert
        assertNotNull(result);
        assertEquals(7, result.size());
        assertEquals("Action", result.get(0));
        assertEquals("Adventure", result.get(1));
        assertEquals("Animation", result.get(2));
        assertEquals("Comedy", result.get(3));
        assertEquals("Crime", result.get(4));
        assertEquals("Documentary", result.get(5));
        assertEquals("Drama", result.get(6));
    }

    @Test
    void testConvertGenreIdsToNamesWithAllUnknownIds() {
        // Arrange
        List<Integer> genreIds = Arrays.asList(999, 1000, 1001);

        // Act
        List<String> result = adapter.convertGenreIdsToNames(genreIds);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Unknown Genre (999)", result.get(0));
        assertEquals("Unknown Genre (1000)", result.get(1));
        assertEquals("Unknown Genre (1001)", result.get(2));
    }

    @Test
    void testConvertGenreIdsToNamesWithNullList() {
        // Act
        List<String> result = adapter.convertGenreIdsToNames(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Should return empty list when input is null");
    }
}

