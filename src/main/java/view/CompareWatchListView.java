package view;

import interface_adapter.compare_watchlists.CompareWatchListViewModel;
import interface_adapter.compare_watchlists.CompareWatchListController;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;

/**
 * Compare WatchList View
 */
public class CompareWatchListView extends JPanel {

    private final CompareWatchListViewModel viewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;

    private CompareWatchListController controller;

    public CompareWatchListView(CompareWatchListViewModel viewModel,
                                LoggedInViewModel loggedInViewModel,
                                ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.loggedInViewModel = loggedInViewModel;
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout());
        JLabel label = new JLabel("Compare Watchlists View");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        add(label, BorderLayout.CENTER);
    }

    public void setController(CompareWatchListController controller) {
        this.controller = controller;
    }

    public String getViewName() {
        return viewModel.getViewName();
    }
}
