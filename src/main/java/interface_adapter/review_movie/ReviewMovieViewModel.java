package interface_adapter.review_movie;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ReviewMovieViewModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private String message = "";
    private String error = "";

    public void setMessage(String message) {
        this.message = message;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() { return message; }
    public String getError() { return error; }

    public void firePropertyChange() {
        support.firePropertyChange(null, null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        support.addPropertyChangeListener(l);
    }
}