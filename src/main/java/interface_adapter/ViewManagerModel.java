package interface_adapter;
//import interface_adapter.ViewModel;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 */
public class ViewManagerModel extends ViewModel<String> {

    public ViewManagerModel() {
        super("view manager");
        this.setState("");
    }

//    public void setState(String newState) {
//        super.setState(newState);
//    }
//
//    public String getState() {
//        return super.getState();
//    }
//
//    /** Notify listeners explicitly (used by presenters) */
//    public void firePropertyChange() {
//        super.firePropertyChange();
//    }


}
