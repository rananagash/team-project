package entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WatchListTest {

    @Test
    void constructorAndGettersWork() {
        User user = new User("test", "1234");
        WatchList wl = new WatchList(user, "My List");

        assertNotNull(wl.getWatchListId());
        assertEquals(user, wl.getUser());
        assertEquals("My List", wl.getName());
        assertNotNull(wl.getDateCreated());
        assertTrue(wl.getMovies().isEmpty());
    }

    @Test
    void secondConstructorWorks() {
        User user = new User("test", "1234");
        WatchList wl = user.getWatchLists().get(0);

        assertNotNull(wl.getWatchListId());
        assertEquals(user, wl.getUser());
        assertEquals("test's Watch List", wl.getName());
        assertNotNull(wl.getDateCreated());
        assertTrue(wl.getMovies().isEmpty());
    }

    @Test
    void addMovieAddsNewMovie() {
        User user = new User("test", "1234");
        WatchList wl = user.getWatchLists().get(0);
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot happens",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster-url"
        );

        boolean result = wl.addMovie(movie);

        assertTrue(result);
        assertEquals(1, wl.getMovies().size());
        assertTrue(wl.getMovies().contains(movie));
    }

    @Test
    void addMovieReturnsFalseOnDuplicate() {
        User user = new User("test", "1234");
        WatchList wl = user.getWatchLists().get(0);
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot happens",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster-url"
        );

        wl.addMovie(movie);
        boolean result = wl.addMovie(movie);

        assertFalse(result);
        assertEquals(1, wl.getMovies().size());
    }

    @Test
    void removeMovieRemovesMovie() {
        User user = new User("test", "1234");
        WatchList wl = user.getWatchLists().get(0);
        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "A test movie plot happens",
                List.of(1, 2),
                "2025-01-01",
                7.5,
                0.0,
                "poster-url"
        );

        wl.addMovie(movie);
        wl.removeMovie(movie);

        assertFalse(wl.getMovies().contains(movie));
        assertEquals(0, wl.getMovies().size());
    }

    @Test
    void stringToStringWorks() {
        User user = new User("test", "1234");
        WatchList wl = user.getWatchLists().get(0);

        assertEquals(user.getUserName() + "'s Watch List", wl.toString());
    }

    @Test
    void getMovieByIdReturnsNullWhenIdNull() {
        User user = new User("test", "1234");
        WatchList wl = user.getWatchLists().get(0);

        assertNull(wl.getMovieById(null));
    }

    @Test
    void getMovieByIdReturnsNullWhenNotFound() {
        User user = new User("test", "1234");
        WatchList wl = user.getWatchLists().get(0);

        assertNull(wl.getMovieById("missing"));
    }

    @Test
    void addMovieThrowsWhenNull() {
        User user = new User("test", "1234");
        WatchList wl = user.getWatchLists().get(0);

        assertThrows(NullPointerException.class, () -> wl.addMovie(null));
    }

    @Test
    void getMovieByIdFindsMovie() {
        User user = new User("test", "1234");
        WatchList wl = user.getWatchLists().get(0);

        Movie movie = new Movie(
                "m1",
                "Test Movie",
                "Plot",
                List.of(1),
                "2025",
                7.5,
                0.0,
                "poster-url"
        );

        wl.addMovie(movie);

        Movie found = wl.getMovieById("m1");

        assertNotNull(found);
        assertEquals(movie, found);
    }

    @Test
    void getMovieByIdIteratesThroughMultipleMoviesAndReturnsNull() {
        User user = new User("test", "1234");
        WatchList wl = user.getWatchLists().get(0);

        Movie movie1 = new Movie(
                "m1", "Movie 1", "Plot 1",
                List.of(1), "2025", 7.0, 0.0, "poster1"
        );
        Movie movie2 = new Movie(
                "m2", "Movie 2", "Plot 2",
                List.of(2), "2025", 8.0, 0.0, "poster2"
        );

        wl.addMovie(movie1);
        wl.addMovie(movie2);

        // This forces iteration over multiple items in the for-loop
        Movie result = wl.getMovieById("m3");

        assertNull(result);
    }
}
