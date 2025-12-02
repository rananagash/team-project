package use_case.view_watchlists;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ViewWatchListResponseModelTest {

    @Test
    void responseModelStoresAllValues() {
        ViewWatchListsResponseModel.WatchListInfo wl1 =
                new ViewWatchListsResponseModel.WatchListInfo("id1", "List One");
        ViewWatchListsResponseModel.WatchListInfo wl2 =
                new ViewWatchListsResponseModel.WatchListInfo("id2", "List Two");

        ViewWatchListsResponseModel.MovieInfo movie =
                new ViewWatchListsResponseModel.MovieInfo(
                        "m1",
                        "Title",
                        "Plot",
                        List.of(10, 20),
                        "2020-01-01",
                        8.5,
                        "poster-url"
                );

        ViewWatchListsResponseModel response =
                new ViewWatchListsResponseModel(
                        List.of(wl1, wl2),
                        1,
                        List.of(movie),
                        "alice"
                );

        assertEquals("alice", response.getUsername());
        assertEquals(1, response.getSelectedIndex());
        assertEquals(2, response.getWatchLists().size());
        assertEquals(1, response.getMovies().size());
    }

    @Test
    void watchListInfoStoresValues() {
        ViewWatchListsResponseModel.WatchListInfo wl =
                new ViewWatchListsResponseModel.WatchListInfo("id1", "My List");

        assertEquals("id1", wl.getId());
        assertEquals("My List", wl.getName());
    }

    @Test
    void movieInfoStoresValues() {
        List<Integer> genres = List.of(1, 2, 3);

        ViewWatchListsResponseModel.MovieInfo movie =
                new ViewWatchListsResponseModel.MovieInfo(
                        "m1",
                        "Movie Title",
                        "A plot",
                        genres,
                        "2021-05-05",
                        7.8,
                        "url"
                );

        assertEquals("m1", movie.getId());
        assertEquals("Movie Title", movie.getTitle());
        assertEquals("A plot", movie.getPlot());
        assertEquals(genres, movie.getGenreIds());
        assertEquals("2021-05-05", movie.getReleaseDate());
        assertEquals(7.8, movie.getRating());
        assertEquals("url", movie.getPosterUrl());
    }
}
