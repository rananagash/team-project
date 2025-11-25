package interface_adapter.compare_watchlists;

import interface_adapter.ViewModel;

public class CompareWatchListViewModel extends ViewModel<CompareWatchListState> {

    public static final String TITLE_LABEL = "Compare Watchlists";
    public static final String COMPARE_BUTTON_LABEL = "Compare";
    public static final String TARGET_USERNAME_LABEL = "Target Username";

    public CompareWatchListViewModel() {
        super("compare watchlists");
        setState(new CompareWatchListState());
    }
}
