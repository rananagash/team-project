package use_case.usecases;

import com.moviesearch.domain.entities.Movie;
import com.moviesearch.data.repositories.StorageRepository;

public class AddToWatchlistUseCase {
    private final StorageRepository storageRepository;

    public AddToWatchlistUseCase(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public boolean execute(Movie movie) {
        try {
            var watchlist = storageRepository.getWatchlist();
            boolean alreadyExists = watchlist.stream()
                    .anyMatch(m -> m.getId() == movie.getId());

            if (!alreadyExists) {
                watchlist.add(movie);
                storageRepository.setWatchlist(watchlist);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}