package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.*;

import interface_adapter.MovieViewData;
import interface_adapter.view_watchlists.ViewWatchListsController;
import interface_adapter.view_watchlists.ViewWatchListsState;
import interface_adapter.view_watchlists.ViewWatchListsViewModel;
import view.components.MovieCard;

/**
 * The view responsible for displaying a user's watchlists and the movies inside the selected watchlist.
 * This class listens to changes in the {@link ViewWatchListsViewModel} and updates the UI accordingly.
 */
public class ViewWatchListsView extends JPanel implements PropertyChangeListener {

    private final String viewName = "view watchlists";
    private final ViewWatchListsViewModel viewModel;

    private ViewWatchListsController controller;

    private JComboBox<String> watchlistDropdown;
    private JButton createButton;
    private JButton backButton;
    private JButton logoutButton;

    private JPanel moviePanel;
    private JScrollPane scrollPane;

    private JLabel errorLabel;

    /**
     * Constructs the ViewWatchListsView and registers it as a listener on the ViewModel.
     *
     * @param viewModel the view model for this view
     */
    public ViewWatchListsView(ViewWatchListsViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);
        initializeui();
    }

    /**
     * Initializes and lays out all UI components for this view.
     */
    private void initializeui() {
        setLayout(new BorderLayout());

        // ---------- NAV BAR ----------
        final JPanel navBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButton = new JButton("← Back");
        logoutButton = new JButton("Logout");

        navBar.add(backButton);
//        navBar.add(logoutButton);
        add(navBar, BorderLayout.NORTH);

        backButton.addActionListener(e -> {
            viewModel.getViewManagerModel().setState("view profile");
            viewModel.getViewManagerModel().firePropertyChange();
        });
        logoutButton.addActionListener(e -> {
            viewModel.getViewManagerModel().setState("log in");
            viewModel.getViewManagerModel().firePropertyChange();
        });

        // ---------- CENTER PANEL ----------
        final JPanel center = new JPanel(new BorderLayout());

        // ---------- TOP CONTROLS ----------
        final JPanel topControls = new JPanel();
        topControls.setLayout(new BoxLayout(topControls, BoxLayout.X_AXIS));

        watchlistDropdown = new JComboBox<>();
        createButton = new JButton("Create New Watchlist");
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);

        topControls.add(watchlistDropdown);
        topControls.add(createButton);
        topControls.add(errorLabel);

        center.add(topControls, BorderLayout.NORTH);

        // Dropdown listener
        watchlistDropdown.addActionListener(e -> {
            if (controller != null && watchlistDropdown.getItemCount() > 0) {
                final int index = watchlistDropdown.getSelectedIndex();
                controller.execute(viewModel.getUsername(), index);
            }
        });

        createButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "Create WatchList is not yet implemented.",
                    "Not Implemented",
                    JOptionPane.WARNING_MESSAGE
            );
        });

        // ---------- MOVIE LIST ----------
        moviePanel = new JPanel();
        moviePanel.setLayout(new BoxLayout(moviePanel, BoxLayout.Y_AXIS));

        scrollPane = new JScrollPane(moviePanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        center.add(scrollPane, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    /**
     * Sets the controller for this view.
     *
     * @param controller the ViewWatchListController used to request watchlist data
     */
    public void setController(ViewWatchListsController controller) {
        this.controller = controller;
    }

    /**
     * Responds to changes in the {@link ViewWatchListsViewModel} state.
     * Re-renders all components including the dropdown list and movie cards.
     *
     * @param evt the property change event containing the updated ViewWatchListsState
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ViewWatchListsState state = (ViewWatchListsState) evt.getNewValue();

        if (state.getError() != null) {
            errorLabel.setText(state.getError());
            return;
        }

        // Username stored for dropdown refresh
        viewModel.setUsername(state.getUsername());

        // Update dropdown
        watchlistDropdown.removeAllItems();
        for (var info : state.getWatchlists()) {
            watchlistDropdown.addItem(info.getName());
        }
        watchlistDropdown.setSelectedIndex(viewModel.getSelectedIndex());

        // Update movie cards
        moviePanel.removeAll();
        for (var movie : state.getMovies()) {
            final MovieViewData movieView = new MovieViewData(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getPlot(),
                    movie.getGenreIds(),
                    movie.getReleaseDate(),
                    movie.getRating(),
                    movie.getPosterUrl()
            );

            moviePanel.add(new MovieCard(movieView, createMovieButtons(movieView)));
            moviePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        moviePanel.revalidate();
        moviePanel.repaint();

        // scroll to top
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
    }

    /**
     * Returns the unique view name used by the ViewManagerModel to switch views.
     *
     * @return the internal name of this view
     */
    public String getViewName() {
        return viewName;
    }

    public List<JButton> createMovieButtons(MovieViewData movie) {

        final JButton watchedButton = new JButton("✓ Mark as Watched");
        final JButton reviewButton = new JButton("Review");

        watchedButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "\"Mark as Watched\" is not yet implemented in this view.",
                    "Not Implemented",
                    JOptionPane.WARNING_MESSAGE
            );
        });

        reviewButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                    this,
                    "\"Review\" is not yet implemented in this view.",
                    "Not Implemented",
                    JOptionPane.WARNING_MESSAGE
            );
        });
        return List.of(watchedButton, reviewButton);
    }
}
