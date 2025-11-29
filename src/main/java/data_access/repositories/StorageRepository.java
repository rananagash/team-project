package data_access.repositories;

import com.moviesearch.domain.entities.Movie;
import java.util.List;

public interface StorageRepository {
    List<Movie> getWatchlist();
    void setWatchlist(List<Movie> movies);
    List<Movie> getWatchHistory();
    void setWatchHistory(List<Movie> movies);
}