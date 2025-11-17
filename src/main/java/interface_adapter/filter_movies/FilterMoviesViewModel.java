package interface_adapter.filter_movies;

import entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class FilterMoviesViewModel {

    private List<Movie> filteredMovies = new ArrayList<>();
    private List<String> selectedGenreNames = new ArrayList<>();
    private String errorMessage = null;
    private boolean hasError = false;

    public List<Movie> getFilteredMovies() {
        return filteredMovies;
    }

    public void setFilteredMovies(List<Movie> filteredMovies) {
        this.filteredMovies = filteredMovies != null ? new ArrayList<>(filteredMovies) : new ArrayList<>();
    }

    public List<String> getSelectedGenreNames() {
        return selectedGenreNames;
    }

    public void setSelectedGenreNames(List<String> selectedGenreNames) {
        this.selectedGenreNames = selectedGenreNames != null ? new ArrayList<>(selectedGenreNames) : new ArrayList<>();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.hasError = errorMessage != null && !errorMessage.isEmpty();
    }

    public boolean hasError() {
        return hasError;
    }

    public void clearError() {
        this.errorMessage = null;
        this.hasError = false;
    }

    public int getMovieCount() {
        return filteredMovies.size();
    }
}

