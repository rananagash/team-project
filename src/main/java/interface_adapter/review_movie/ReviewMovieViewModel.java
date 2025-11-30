package interface_adapter.review_movie;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Map;
import use_case.review_movie.ReviewMovieResponseModel;
import java.util.HashMap;

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

    private final Map<String, Map<String, ReviewMovieResponseModel>> reviewCache = new HashMap<>();

    public ReviewMovieResponseModel getExistingReview(String userId, String movieId) {
        if (!reviewCache.containsKey(userId)) return null;
        return reviewCache.get(userId).get(movieId);
    }

    public void storeReview(String userId, String movieId, ReviewMovieResponseModel review) {
        reviewCache.computeIfAbsent(userId, k -> new HashMap<>()).put(movieId, review);
    }



}