package interface_adapter.filter_movies;

import entity.Movie;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * View model for the Filter Movies use case.
 * <p>
 * Holds the state required by the view, including the filtered movies,
 * selected genre names, and any error messages. Supports error tracking
 * and defensive copying of lists.
 */
public class FilterMoviesViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private List<Movie> filteredMovies = new ArrayList<>();
    private List<String> selectedGenreNames = new ArrayList<>();
    private String errorMessage = null;
    private boolean hasError = false;

    /**
     * Returns a copy of the filtered movies list.
     *
     * @return a list of filtered movies
     */
    public List<Movie> getFilteredMovies() {
        return filteredMovies;
    }

    /**
     * Sets the filtered movies list.
     * <p>
     * Performs a defensive copy to prevent external modification.
     *
     * @param filteredMovies the list of movies to set
     */
    public void setFilteredMovies(List<Movie> filteredMovies) {
        List<Movie> oldValue = this.filteredMovies;
        this.filteredMovies = filteredMovies != null ? new ArrayList<>(filteredMovies) : new ArrayList<>();
        support.firePropertyChange("filteredMovies", oldValue, this.filteredMovies);
    }

    /**
     * Returns a copy of the selected genre names list.
     *
     * @return a list of selected genre names
     */
    public List<String> getSelectedGenreNames() {
        return selectedGenreNames;
    }

    /**
     * Sets the selected genre names.
     * <p>
     * Performs a defensive copy to prevent external modification.
     *
     * @param selectedGenreNames the list of genre names to set
     */
    public void setSelectedGenreNames(List<String> selectedGenreNames) {
        this.selectedGenreNames = selectedGenreNames != null ? new ArrayList<>(selectedGenreNames) : new ArrayList<>();
    }

    /**
     * Returns the current error message, or {@code null} if no error exists.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error message.
     * <p>
     * Updates the {@code hasError} flag automatically.
     *
     * @param errorMessage the error message to set
     */
    public void setErrorMessage(String errorMessage) {
        String oldValue = this.errorMessage;
        this.errorMessage = errorMessage;
        this.hasError = errorMessage != null && !errorMessage.isEmpty();
        support.firePropertyChange("errorMessage", oldValue, this.errorMessage);
    }

    /**
     * Returns whether there is an active error message.
     *
     * @return {@code true} if there is an error, {@code false} otherwise
     */
    public boolean hasError() {
        return hasError;
    }

    /**
     * Clears the current error message and resets the error flag.
     */
    public void clearError() {
        this.errorMessage = null;
        this.hasError = false;
    }

    /**
     * Returns the number of filtered movies currently stored in the view model.
     *
     * @return the number of movies
     */
    public int getMovieCount() {
        return filteredMovies.size();
    }

    /**
     * Adds a PropertyChangeListener to this view model.
     *
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a PropertyChangeListener from this view model.
     *
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
