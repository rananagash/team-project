
package interface_adapter.search_movie;

import com.moviesearch.domain.entities.Movie;
import com.moviesearch.domain.entities.SearchResult;
import com.moviesearch.data.repositories.MovieRepository;
import com.moviesearch.data.repositories.StorageRepository;
import com.moviesearch.domain.usecases.*;
import com.moviesearch.presentation.views.MovieSearchView;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchMoviePresenter {
    private final MovieSearchView view;
    private final SearchMoviesUseCase searchMoviesUseCase;
    private final AddToWatchlistUseCase addToWatchlistUseCase;
    private final AddToWatchHistoryUseCase addToWatchHistoryUseCase;
    private final StorageRepository storageRepository;
    private final FilterMoviesUseCase filterMoviesUseCase;
    private final ExecutorService executor;

    private List<Movie> currentMovies = List.of();
    private List<Movie> filteredMovies = List.of();

    public SearchMoviePresenter(MovieSearchView view,
                                MovieRepository movieRepository,
                                StorageRepository storageRepository) {
        this.view = view;
        this.storageRepository = storageRepository;
        this.searchMoviesUseCase = new SearchMoviesUseCase(movieRepository);
        this.addToWatchlistUseCase = new AddToWatchlistUseCase(storageRepository);
        this.addToWatchHistoryUseCase = new AddToWatchHistoryUseCase(storageRepository);
        this.filterMoviesUseCase = new FilterMoviesUseCase();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void searchMovies() {
        String query = view.getSearchQuery();
        if (query == null || query.trim().isEmpty()) {
            view.showError("Please enter the title");
            return;
        }

        view.showLoading();

        executor.execute(() -> {
            SearchResult result = searchMoviesUseCase.execute(query);

            // return to UI refresh stack
            javax.swing.SwingUtilities.invokeLater(() -> {
                view.hideLoading();

                if (result.isSuccess()) {
                    this.currentMovies = result.getMovies();
                    this.filteredMovies = result.getMovies();
                    view.showSearchResults(filteredMovies);
                } else {
                    view.showError(result.getErrorMessage());
                }
            });
        });
    }

    public void filterMovies(String genreId) {
        filteredMovies = filterMoviesUseCase.execute(currentMovies, genreId);
        view.showSearchResults(filteredMovies);
    }

    //may be buggy
    public void addToWatchlist(Movie movie) {
        boolean success = addToWatchlistUseCase.execute(movie);
        if (success) {
            view.updateWatchlistButtons();
        }
    }

    public boolean isInWatchlist(int movieId) {
        return storageRepository.getWatchlist().stream()
                .anyMatch(movie -> movie.getId() == movieId);
    }

    public boolean isInWatchHistory(int movieId) {
        return storageRepository.getWatchHistory().stream()
                .anyMatch(movie -> movie.getId() == movieId);
    }

    public void addToWatchHistory(Movie movie) {
        boolean success = addToWatchHistoryUseCase.execute(movie);
        if (success) {
            view.updateWatchlistButtons();
        }
    }

    public void searchAgain() {
        view.showSearchScreen();
        view.clearSearchQuery();
        this.currentMovies = List.of();
        this.filteredMovies = List.of();
    }

    public void showProfile() {
        view.showProfile();
    }

    public void dispose() {
        executor.shutdown();
    }
}