package view;

import entity.Movie;
import entity.Review;
import entity.User;
import entity.WatchedMovie;
import interface_adapter.delete_watchedmovie.DeleteWatchedMovieController;
import interface_adapter.delete_watchedmovie.DeleteWatchedMovieView;
import interface_adapter.edit_watchedmovie.EditWatchedMovieController;
import interface_adapter.edit_watchedmovie.EditWatchedMoviePresenter;
import interface_adapter.view_watchhistory.ViewWatchHistoryController;
import interface_adapter.view_watchhistory.ViewWatchHistoryView;
import use_case.common.MovieGateway;
import use_case.common.UserDataAccessInterface;
import use_case.delete_watchedmovie.DeleteWatchedMovieResponseModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Swing-based popup dialog for displaying a user's watch history.
 *
 * <p>Implements {@link ViewWatchHistoryView}, enabling the presenter to update the UI
 * with watch history data.
 *
 * <p>The popup displays:
 * <ul>
 *     <li>User's name and total watched count</li>
 *     <li>A scrollable list of watched movies with details</li>
 *     <li>Movie title, watch date, rating, and poster (if available)</li>
 * </ul>
 */
public class ViewWatchHistoryPopup extends JDialog implements ViewWatchHistoryView, DeleteWatchedMovieView {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private JLabel titleLabel;
    private JLabel countLabel;
    private JPanel moviesPanel;
    private JScrollPane scrollPane;
    private JLabel errorLabel;

    // Controllers and dependencies
    private DeleteWatchedMovieController deleteController;
    private EditWatchedMovieController editController;
    private ViewWatchHistoryController viewHistoryController;
    private MovieGateway movieGateway;
    private UserDataAccessInterface userDataAccess;
    private EditWatchedMoviePresenter editPresenter;
    private String currentUserName;

    /**
     * Constructs the popup UI.
     *
     * @param parent the parent Swing frame
     */
    public ViewWatchHistoryPopup(JFrame parent) {
        super(parent, "Watch History", true);
        initializeUI();
    }

    public void setDeleteController(DeleteWatchedMovieController controller) {
        this.deleteController = controller;
    }

    public void setEditController(EditWatchedMovieController controller) {
        this.editController = controller;
    }

    public void setViewHistoryController(ViewWatchHistoryController controller) {
        this.viewHistoryController = controller;
    }

    public void setMovieGateway(MovieGateway movieGateway) {
        this.movieGateway = movieGateway;
    }

    public void setUserDataAccess(UserDataAccessInterface userDataAccess) {
        this.userDataAccess = userDataAccess;
    }

    public void setEditPresenter(EditWatchedMoviePresenter presenter) {
        this.editPresenter = presenter;
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 600));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        headerPanel.setBackground(new Color(245, 245, 250));

        titleLabel = new JLabel("Watch History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        countLabel = new JLabel();
        countLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        countLabel.setForeground(new Color(100, 100, 100));
        countLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        headerPanel.add(countLabel);

        // Movies panel with scroll
        moviesPanel = new JPanel();
        moviesPanel.setLayout(new BoxLayout(moviesPanel, BoxLayout.Y_AXIS));
        moviesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        moviesPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(moviesPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Error label
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // Close button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(15, 20, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        // Add components
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(errorLabel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.PAGE_END);

        pack();
        setLocationRelativeTo(getParent());
    }

    @Override
    public void showWatchHistory(String userName, List<WatchedMovie> watchedMovies) {
        // Clear previous content
        moviesPanel.removeAll();
        errorLabel.setText("");

        // Store current state
        this.currentUserName = userName;

        // Update header
        titleLabel.setText("Watch History - " + userName);
        if (watchedMovies.isEmpty()) {
            countLabel.setText("No movies watched yet");
            JLabel emptyLabel = new JLabel("Start watching movies to build your history!");
            emptyLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            emptyLabel.setForeground(new Color(150, 150, 150));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setBorder(new EmptyBorder(50, 20, 50, 20));
            moviesPanel.add(emptyLabel);
        } else {
            countLabel.setText("Total: " + watchedMovies.size() + " movie(s) watched");

            // Add each movie card
            for (int i = 0; i < watchedMovies.size(); i++) {
                WatchedMovie movie = watchedMovies.get(i);
                JPanel movieCard = createMovieCard(movie, i + 1);
                moviesPanel.add(movieCard);
                moviesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        moviesPanel.revalidate();
        moviesPanel.repaint();
        setVisible(true);
    }

    private JPanel createMovieCard(WatchedMovie movie, int index) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(15, 0));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setBackground(new Color(255, 255, 255));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Left side: Movie number and info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        // Movie title
        JLabel titleLabel = new JLabel(index + ". " + movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(30, 30, 30));

        // Watch date
        JLabel dateLabel = new JLabel("Watched: " + movie.getWatchedDate().format(DATE_FORMATTER));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        dateLabel.setForeground(new Color(100, 100, 100));
        dateLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

        // Get user's review to display rating and review text
        Review userReview = null;
        if (userDataAccess != null && currentUserName != null) {
            User user = userDataAccess.getUser(currentUserName);
            if (user != null) {
                userReview = user.getReviewsByMovieId().get(movie.getMovieId());
            }
        }

        // User's rating (from review, not TMDb rating)
        JLabel ratingLabel = new JLabel();
        if (userReview != null && userReview.getRating() > 0) {
            ratingLabel.setText("Rating: " + userReview.getRating() + "/5 ⭐");
            ratingLabel.setForeground(new Color(255, 140, 0));
        } else {
            ratingLabel.setText("Rating: Not rated yet");
            ratingLabel.setForeground(new Color(150, 150, 150));
        }
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        ratingLabel.setBorder(new EmptyBorder(3, 0, 0, 0));

        // User's review text
        JLabel reviewLabel = new JLabel();
        if (userReview != null && userReview.getComment() != null && !userReview.getComment().trim().isEmpty()) {
            String reviewText = userReview.getComment();
            // Truncate long reviews for display
            if (reviewText.length() > 100) {
                reviewText = reviewText.substring(0, 97) + "...";
            }
            reviewLabel.setText("Review: " + reviewText);
            reviewLabel.setForeground(new Color(120, 120, 120));
        } else {
            reviewLabel.setText("Review: Not reviewed yet");
            reviewLabel.setForeground(new Color(150, 150, 150));
        }
        reviewLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        reviewLabel.setBorder(new EmptyBorder(3, 0, 0, 0));

        infoPanel.add(titleLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(ratingLabel);
        infoPanel.add(reviewLabel);
        infoPanel.add(Box.createVerticalGlue());

        // Right side: Buttons and Movie ID
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonPanel.setOpaque(false);

        JButton detailsButton = new JButton("Details");
        detailsButton.setFont(new Font("Arial", Font.PLAIN, 12));
        detailsButton.setPreferredSize(new Dimension(85, 30));
        detailsButton.setBackground(new Color(0, 123, 255)); // Blue
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFocusPainted(false);
        detailsButton.addActionListener(e -> openEditDialog(movie));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 12));
        deleteButton.setPreferredSize(new Dimension(85, 30));
        deleteButton.setBackground(new Color(220, 53, 69)); // Red
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteMovie(movie));

        buttonPanel.add(detailsButton);
        buttonPanel.add(deleteButton);

        // Movie ID (small text)
        JLabel idLabel = new JLabel("ID: " + movie.getMovieId());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        idLabel.setForeground(new Color(180, 180, 180));
        idLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        idLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        rightPanel.add(buttonPanel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        rightPanel.add(idLabel);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(rightPanel, BorderLayout.EAST);

        return card;
    }

    private void openEditDialog(WatchedMovie watchedMovie) {
        if (editController == null || movieGateway == null || userDataAccess == null) {
            JOptionPane.showMessageDialog(this,
                    "Edit functionality is not available.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get the base Movie entity
        Movie movie = movieGateway.findById(watchedMovie.getMovieId()).orElse(null);
        if (movie == null) {
            JOptionPane.showMessageDialog(this,
                    "Movie details not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Get existing review if any
        User user = userDataAccess.getUser(currentUserName);
        Review existingReview = null;
        if (user != null) {
            existingReview = user.getReviewsByMovieId().get(watchedMovie.getMovieId());
        }

        // Create and show edit dialog
        MovieDetailEditPopup editDialog = new MovieDetailEditPopup(
                (JFrame) getParent(),
                currentUserName,
                watchedMovie,
                movie,
                existingReview,
                editController,
                deleteController,
                editPresenter,
                this::refreshHistory
        );
        editDialog.setVisible(true);
    }

    private void deleteMovie(WatchedMovie watchedMovie) {
        if (deleteController == null) {
            JOptionPane.showMessageDialog(this,
                    "Delete functionality is not available.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        DeleteConfirmationPopup confirmDialog = new DeleteConfirmationPopup(
                (JFrame) getParent(), watchedMovie.getTitle()
        );
        confirmDialog.setVisible(true);

        if (confirmDialog.isConfirmed()) {
            deleteController.deleteMovie(currentUserName, watchedMovie.getMovieId());
        }
    }

    private void refreshHistory() {
        if (viewHistoryController != null && currentUserName != null) {
            viewHistoryController.loadHistory(currentUserName);
            // Ensure the popup is visible and on top after refresh
            setVisible(true);
            toFront();
            requestFocus();
        }
    }

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText("Error: " + errorMessage);
        moviesPanel.removeAll();
        moviesPanel.revalidate();
        moviesPanel.repaint();
        setVisible(true);
    }

    @Override
    public void onDeleteSuccess(DeleteWatchedMovieResponseModel responseModel) {
        // Refresh the history view
        refreshHistory();
    }

    @Override
    public void onDeleteError(String errorMessage) {
        errorLabel.setText("Error: " + errorMessage);
        errorLabel.setVisible(true);
    }

    /**
     * Shows the popup centered on the parent frame.
     */
    public void display() {
        setLocationRelativeTo(getParent());
        setVisible(true);
    }
}

