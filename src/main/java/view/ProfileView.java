package view;

import interface_adapter.view_profile.ViewProfileController;
import interface_adapter.view_profile.ViewProfileState;
import interface_adapter.view_profile.ViewProfileViewModel;
import interface_adapter.view_watchlists.ViewWatchListsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for displaying user profiles.
 */
public class ProfileView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "view profile";
    private final ViewProfileViewModel viewProfileViewModel;
    private ViewProfileController viewProfileController;
    private ViewWatchListsController viewWatchListsController;

    private final JLabel titleLabel = new JLabel(ViewProfileViewModel.TITLE_LABEL);
    private final JLabel usernameLabel = new JLabel();
    private final JLabel watchlistCountLabel = new JLabel();
    private final JLabel reviewCountLabel = new JLabel();
    private final JLabel watchedMoviesLabel = new JLabel();
    private final JLabel errorLabel = new JLabel();

    private JButton viewWatchListsButton;
    private String currentUsername;

    public ProfileView(ViewProfileViewModel viewProfileViewModel) {
        this.viewProfileViewModel = viewProfileViewModel;
        viewProfileViewModel.addPropertyChangeListener(this);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Button to navigate to Watch Lists page
        viewWatchListsButton = new JButton("View Watchlists");
        viewWatchListsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewWatchListsButton.addActionListener(this);

        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(usernameLabel);
        add(watchlistCountLabel);
        add(reviewCountLabel);
        add(watchedMoviesLabel);

        add(Box.createRigidArea(new Dimension(0, 20)));
        add(viewWatchListsButton);

        add(Box.createRigidArea(new Dimension(0, 10)));
        add(errorLabel);
    }

    public void setViewProfileController(ViewProfileController controller) {
        this.viewProfileController = controller;
    }

    public void setViewWatchListsController(ViewWatchListsController controller) {
        this.viewWatchListsController = controller;
    }

    /**
     * Loads profile for the specified username.
     */
    public void loadProfile(String username) {
        if (viewProfileController != null) {
            viewProfileController.execute(username);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ViewProfileState state = (ViewProfileState) evt.getNewValue();

        currentUsername = state.getUser().getUserName();

        if (state.getError() != null) {
            errorLabel.setText(state.getError());
        } else if (state.getUser() != null && state.getProfileStats() != null) {
            usernameLabel.setText("Username: " + currentUsername);
            watchlistCountLabel.setText("Watchlists: " + state.getProfileStats().getWatchlistCount());
            reviewCountLabel.setText("Reviews: " + state.getProfileStats().getReviewCount());
            watchedMoviesLabel.setText("Watched Movies: " + state.getProfileStats().getWatchedMoviesCount());
            errorLabel.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewWatchListsButton) {
            if (viewWatchListsController != null && currentUsername != null) {
                viewWatchListsController.execute(currentUsername);
            }
        }
    }

    public String getViewName() {
        return viewName;
    }
}