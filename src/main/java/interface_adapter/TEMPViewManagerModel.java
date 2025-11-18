package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class TEMPViewManagerModel {

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String state = "";

    public void setState(String newState) {
        this.state = newState;
        support.firePropertyChange("state", null, newState);
    }

    public String getState() {
        return state;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    // This is the method LoginPresenter needs
    public void firePropertyChange() {
        support.firePropertyChange("state", null, this.state);
    }
}