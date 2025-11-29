// domain/usecases/AddToWatchHistoryUseCase.java
package use_case.usecases;

import entity.Movie;
import data_access.repositories.StorageRepository;

public class AddToWatchHistoryUseCase {
    private final StorageRepository storageRepository;

    public AddToWatchHistoryUseCase(StorageRepository storageRepository) {
        this.storageRepository = storageRepository;
    }

    public boolean execute(Movie movie) {
        try {
            var watchHistory = storageRepository.getWatchHistory();
            boolean alreadyExists = watchHistory.stream()
                    .anyMatch(m -> m.getMovieId() == movie.getMovieId());

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