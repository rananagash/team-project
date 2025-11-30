package view;

import entity.Movie;
import interface_adapter.record_watchhistory.RecordWatchHistoryController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Popup dialog for displaying movie details and allowing users to mark movies as watched.
 * Similar to AddReviewPopup, but focused on recording watch history.
 */
public class MovieDetailPopup extends JDialog {

    private final RecordWatchHistoryController recordController;
    private final Movie movie;
    private final String currentUsername;

    private JLabel titleLabel;
    private JLabel plotLabel;
    private JLabel ratingLabel;
    private JLabel releaseDateLabel;
    private JLabel movieIdLabel;
    private JButton markAsWatchedButton;
    private JButton cancelButton;

    /**
     * Constructs a movie detail popup.
     *
     * @param parent the parent Swing frame
     * @param recordController the controller for recording watch history
     * @param movie the movie to display
     * @param currentUsername the currently logged-in username
     */
    public MovieDetailPopup(JFrame parent,
                           RecordWatchHistoryController recordController,
                           Movie movie,
                           String currentUsername) {
        super(parent, "Movie Details: " + movie.getTitle(), true);

        this.recordController = recordController;
        this.movie = movie;
        this.currentUsername = currentUsername;

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 400));
        setResizable(false);

        // ===== Header Panel =====
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        headerPanel.setBackground(new Color(245, 245, 250));

        titleLabel = new JLabel(movie.getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(10));

        // ===== Content Panel =====
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPanel.setBackground(Color.WHITE);

        // Movie ID
        movieIdLabel = new JLabel("Movie ID: " + movie.getMovieId());
        movieIdLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        movieIdLabel.setForeground(new Color(120, 120, 120));
        movieIdLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Rating
        ratingLabel = new JLabel("Rating: " + String.format("%.1f", movie.getRating()) + " ⭐");
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        ratingLabel.setForeground(new Color(255, 140, 0));
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        ratingLabel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Release Date
        String releaseDateText = movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()
                ? "Release Date: " + movie.getReleaseDate()
                : "Release Date: N/A";
        releaseDateLabel = new JLabel(releaseDateText);
        releaseDateLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        releaseDateLabel.setForeground(new Color(100, 100, 100));
        releaseDateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        releaseDateLabel.setBorder(new EmptyBorder(5, 0, 0, 0));

        // Plot
        String plotText = movie.getPlot() != null && !movie.getPlot().isEmpty()
                ? movie.getPlot()
                : "No plot summary available.";
        plotLabel = new JLabel("<html><div style='width: 400px;'>" + plotText + "</div></html>");
        plotLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        plotLabel.setForeground(new Color(50, 50, 50));
        plotLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        plotLabel.setBorder(new EmptyBorder(15, 0, 0, 0));

        contentPanel.add(movieIdLabel);
        contentPanel.add(ratingLabel);
        contentPanel.add(releaseDateLabel);
        contentPanel.add(plotLabel);
        contentPanel.add(Box.createVerticalGlue());

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(15, 20, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 250));

        markAsWatchedButton = new JButton("Mark as Watched");
        markAsWatchedButton.setFont(new Font("Arial", Font.BOLD, 14));
        markAsWatchedButton.setPreferredSize(new Dimension(180, 40));
        markAsWatchedButton.setBackground(new Color(46, 125, 50));
        markAsWatchedButton.setForeground(Color.WHITE);
        markAsWatchedButton.setFocusPainted(false);

        cancelButton = new JButton("Close");
        cancelButton.setFont(new Font("Arial", Font.PLAIN, 14));
        cancelButton.setPreferredSize(new Dimension(100, 40));

        // Button actions
        markAsWatchedButton.addActionListener(e -> {
            if (recordController != null) {
                // Record with current time
                recordController.recordMovie(currentUsername, movie.getMovieId());
                // The popup will be shown by RecordWatchHistoryPopup
                dispose();
            }
        });

        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(markAsWatchedButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);

        // ===== Add components =====
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    /**
     * Shows the popup centered on the parent frame.
     */
    public void display() {
        setLocationRelativeTo(getParent());
        setVisible(true);
    }
}

