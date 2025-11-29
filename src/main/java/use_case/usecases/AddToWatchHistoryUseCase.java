// domain/usecases/AddToWatchHistoryUseCase.java
package use_case.usecases;

import com.moviesearch.domain.entities.Movie;
import com.moviesearch.data.repositories.StorageRepository;

public class AddToWatchHistoryUseCase {
    private final StorageRepository storageRepository;

    public AddToWatchHistoryUseCase(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public boolean execute(Movie movie) {
        try {
            var watchHistory = storageRepository.getWatchHistory();
            boolean alreadyExists = watchHistory.stream()
                    .anyMatch(m -> m.getId() == movie.getId());

            if (!alreadyExists) {
                watchHistory.add(movie);
                storageRepository.setWatchHistory(watchHistory);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}