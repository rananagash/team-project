package view;

public class ProfileView {
}

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        add(usernameLabel);
        add(watchlistCountLabel);
        add(reviewCountLabel);
        add(watchedMoviesLabel);
        add(errorLabel);
    }

    public void setViewProfileController(ViewProfileController controller) {
        this.viewProfileController = controller;
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

        if (state.getError() != null) {
            errorLabel.setText(state.getError());
        } else if (state.getUser() != null && state.getProfileStats() != null) {
            usernameLabel.setText("Username: " + state.getUser().getUserName());
            watchlistCountLabel.setText("Watchlists: " + state.getProfileStats().getWatchlistCount());
            reviewCountLabel.setText("Reviews: " + state.getProfileStats().getReviewCount());
            watchedMoviesLabel.setText("Watched Movies: " + state.getProfileStats().getWatchedMoviesCount());
            errorLabel.setText("");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle button actions if needed
    }
}