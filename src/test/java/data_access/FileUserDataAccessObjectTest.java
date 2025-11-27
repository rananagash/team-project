package data_access;

import entity.*;
import entity.factories.UserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class FileUserDataAccessObjectTest {

    private Path tempFile;
    private UserFactory userFactory;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("users", ".json");
        userFactory = new UserFactory();
    }

    @AfterEach
    void cleanup() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    // ---------------------------------------------------
    // 1. BASIC SAVE + LOAD
    // ---------------------------------------------------

    @Test
    void testSaveAndLoadEmptyUser() {
        FileUserDataAccessObject dao = new FileUserDataAccessObject(tempFile.toString(), userFactory);

        User u = userFactory.create("alice", "pw123");
        dao.save(u);

        // Reload from file
        FileUserDataAccessObject dao2 = new FileUserDataAccessObject(tempFile.toString(), userFactory);

        User loaded = dao2.getUser("alice");
        assertNotNull(loaded);
        assertEquals("alice", loaded.getUserName());
        assertEquals("pw123", loaded.getPassword());

        // default watchlist exists
        assertEquals(1, loaded.getWatchLists().size());
    }

    // ---------------------------------------------------
    // 2. WATCHLISTS PERSIST
    // ---------------------------------------------------

    @Test
    void testSaveAndReloadWithWatchlists() {
        FileUserDataAccessObject dao = new FileUserDataAccessObject(tempFile.toString(), userFactory);

        User u = userFactory.create("bob", "secret");
        WatchList wl = new WatchList(u, "Favorites");

        Movie m = new Movie(
                "100",
                "Inception",
                "dream heist",
                List.of(28, 12),
                "2010-07-16",
                9.0,
                1000.0,
                "poster.jpg"
        );
        wl.addMovie(m);
        u.addWatchList(wl);

        dao.save(u);

        // reload
        FileUserDataAccessObject dao2 = new FileUserDataAccessObject(tempFile.toString(), userFactory);
        User loaded = dao2.getUser("bob");

        assertNotNull(loaded);
        assertEquals(2, loaded.getWatchLists().size()); // includes default WL

        WatchList loadedWL = loaded.getWatchListByName("Favorites").orElse(null);
        assertNotNull(loadedWL);

        assertEquals(1, loadedWL.getMovies().size());
        assertEquals("Inception", loadedWL.getMovies().get(0).getTitle());
    }

    // ---------------------------------------------------
    // 3. REVIEWS PERSIST
    // ---------------------------------------------------

    @Test
    void testSaveAndReloadWithReviews() {
        FileUserDataAccessObject dao = new FileUserDataAccessObject(tempFile.toString(), userFactory);

        User u = userFactory.create("carol", "1234");

        Movie m = new Movie(
                "500",
                "Frozen",
                "let it go",
                List.of(16),
                "2013-11-27",
                8.0,
                800.0,
                "frozen.jpg"
        );

        Review r = new Review(
                UUID.randomUUID().toString(),
                u,
                m,
                4,
                "Pretty good",
                LocalDateTime.now()
        );

        u.addReview(r);
        dao.save(u);

        // reload
        FileUserDataAccessObject dao2 = new FileUserDataAccessObject(tempFile.toString(), userFactory);
        User loaded = dao2.getUser("carol");

        assertNotNull(loaded);
        assertEquals(1, loaded.getReviewsByMovieId().size());
        Review loadedReview = loaded.getReviewsByMovieId().values().iterator().next();

        assertEquals("Pretty good", loadedReview.getComment());
        assertEquals(4, loadedReview.getRating());
        assertEquals("Frozen", loadedReview.getMovie().getTitle());
    }

    // ---------------------------------------------------
    // 4. WATCH HISTORY PERSIST
    // ---------------------------------------------------

    @Test
    void testSaveAndReloadWatchHistory() {
        FileUserDataAccessObject dao = new FileUserDataAccessObject(tempFile.toString(), userFactory);

        User u = userFactory.create("dave", "xyz");

        WatchHistory wh = new WatchHistory(UUID.randomUUID().toString(), u);

        Movie m = new Movie(
                "900",
                "The Matrix",
                "red pill",
                List.of(878),
                "1999-03-31",
                10.0,
                5000.0,
                "matrix.jpg"
        );

        wh.recordMovie(m, LocalDateTime.of(2023, 1, 1, 12, 0));
        u.setWatchHistory(wh);

        dao.save(u);

        // reload
        FileUserDataAccessObject dao2 = new FileUserDataAccessObject(tempFile.toString(), userFactory);
        User loaded = dao2.getUser("dave");

        assertNotNull(loaded.getWatchHistory());
        assertEquals(1, loaded.getWatchHistory().getMovies().size());

        WatchedMovie wm = loaded.getWatchHistory().getMovies().get(0);
        assertEquals("The Matrix", wm.getTitle());
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0), wm.getWatchedDate());
    }

    // ---------------------------------------------------
    // 5. CURRENT USER PERSISTENCE
    // ---------------------------------------------------

    @Test
    void testCurrentUserPersistence() {
        FileUserDataAccessObject dao = new FileUserDataAccessObject(tempFile.toString(), userFactory);

        dao.setCurrentUsername("eve");

        FileUserDataAccessObject dao2 = new FileUserDataAccessObject(tempFile.toString(), userFactory);

        assertEquals("eve", dao2.getCurrentUsername());
    }

    // ---------------------------------------------------
    // 6. EXISTS BY NAME
    // ---------------------------------------------------

    @Test
    void testExistsByName() {
        FileUserDataAccessObject dao = new FileUserDataAccessObject(tempFile.toString(), userFactory);

        User u = userFactory.create("frank", "pw");
        dao.save(u);

        assertTrue(dao.existsByName("frank"));
        assertFalse(dao.existsByName("notAUser"));
    }
}
