package view;

import entity.WatchedMovie;
import interface_adapter.view_watchhistory.ViewWatchHistoryView;

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
public class ViewWatchHistoryPopup extends JDialog implements ViewWatchHistoryView {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private JLabel titleLabel;
    private JLabel countLabel;
    private JPanel moviesPanel;
    private JScrollPane scrollPane;
    private JLabel errorLabel;

    /**
     * Constructs the popup UI.
     *
     * @param parent the parent Swing frame
     */
    public ViewWatchHistoryPopup(JFrame parent) {
        super(parent, "Watch History", true);
        initializeUI();
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

        // Rating (if available)
        JLabel ratingLabel = null;
        if (movie.getRating() > 0) {
            ratingLabel = new JLabel("Rating: " + String.format("%.1f", movie.getRating()) + " ⭐");
            ratingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            ratingLabel.setForeground(new Color(255, 140, 0));
            ratingLabel.setBorder(new EmptyBorder(3, 0, 0, 0));
        }

        // Release date (if available)
        JLabel releaseLabel = null;
        if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
            releaseLabel = new JLabel("Released: " + movie.getReleaseDate());
            releaseLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            releaseLabel.setForeground(new Color(120, 120, 120));
            releaseLabel.setBorder(new EmptyBorder(3, 0, 0, 0));
        }

        infoPanel.add(titleLabel);
        infoPanel.add(dateLabel);
        if (ratingLabel != null) {
            infoPanel.add(ratingLabel);
        }
        if (releaseLabel != null) {
            infoPanel.add(releaseLabel);
        }
        infoPanel.add(Box.createVerticalGlue());

        // Right side: Movie ID (small text)
        JLabel idLabel = new JLabel("ID: " + movie.getMovieId());
        idLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        idLabel.setForeground(new Color(180, 180, 180));
        idLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(idLabel, BorderLayout.EAST);

        return card;
    }

    @Override
    public void showError(String errorMessage) {
        errorLabel.setText("Error: " + errorMessage);
        moviesPanel.removeAll();
        moviesPanel.revalidate();
        moviesPanel.repaint();
        setVisible(true);
    }

    /**
     * Shows the popup centered on the parent frame.
     */
    public void display() {
        setLocationRelativeTo(getParent());
        setVisible(true);
    }
}

