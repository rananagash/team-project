package interface_adapter.add_to_watchlist;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class AddWatchListViewModel {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    private List<WatchListOption> watchListOptions;
    private String resultMessage;

    public void setWatchListOptions(List<WatchListOption> options) {
        this.watchListOptions = options;
        propertyChangeSupport.firePropertyChange("watchListOptions", null, options);
    }

    public List<WatchListOption> getWatchListOptions() {
        return watchListOptions;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
        propertyChangeSupport.firePropertyChange("resultMessage", null, resultMessage);
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
}
