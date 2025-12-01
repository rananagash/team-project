package view;

import entity.Movie;
import entity.Review;
import entity.WatchedMovie;
import interface_adapter.delete_watchedmovie.DeleteWatchedMovieController;
import interface_adapter.delete_watchedmovie.DeleteWatchedMovieView;
import interface_adapter.edit_watchedmovie.EditWatchedMovieController;
import interface_adapter.edit_watchedmovie.EditWatchedMoviePresenter;
import interface_adapter.edit_watchedmovie.EditWatchedMovieView;
import use_case.delete_watchedmovie.DeleteWatchedMovieResponseModel;
import use_case.edit_watchedmovie.EditWatchedMovieResponseModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Popup dialog for editing movie details in watch history.
 * Displays movie information and allows editing watched date, rating, and review.
 */
public class MovieDetailEditPopup extends JDialog implements EditWatchedMovieView, DeleteWatchedMovieView {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final String userName;
    private final WatchedMovie watchedMovie;
    private final Movie movie;
    private final Review existingReview;
    private final EditWatchedMovieController editController;
    private final DeleteWatchedMovieController deleteController;
    private final Runnable onCloseCallback;
    private final EditWatchedMoviePresenter editPresenter;

    // UI Components
    private JTextField watchedDateField;
    private JTextField watchedTimeField;
    private JComboBox<Integer> ratingComboBox;
    private JTextArea reviewTextArea;
    private JLabel errorLabel;

    public MovieDetailEditPopup(JFrame parent,
                                 String userName,
                                 WatchedMovie watchedMovie,
                                 Movie movie,
                                 Review existingReview,
                                 EditWatchedMovieController editController,
                                 DeleteWatchedMovieController deleteController,
                                 EditWatchedMoviePresenter editPresenter,
                                 Runnable onCloseCallback) {
        super(parent, "Edit Movie Details", true);
        this.userName = userName;
        this.watchedMovie = watchedMovie;
        this.movie = movie;
        this.existingReview = existingReview;
        this.editController = editController;
        this.deleteController = deleteController;
        this.editPresenter = editPresenter != null ? editPresenter : new EditWatchedMoviePresenter(this);
        this.onCloseCallback = onCloseCallback;
        
        // Update presenter to use this view
        if (editPresenter != null) {
            // Note: Presenter doesn't have a setter for view, so we create a new one
            // This is a limitation of the current architecture
        }

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 700));
        setResizable(true);

        // Main content panel with scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // === Non-editable section ===
        JPanel nonEditablePanel = createNonEditableSection();
        mainPanel.add(nonEditablePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // === Editable section ===
        JPanel editablePanel = createEditableSection();
        mainPanel.add(editablePanel);
        mainPanel.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // === Error label ===
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBorder(new EmptyBorder(5, 20, 5, 20));

        // === Button panel ===
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setBorder(new EmptyBorder(10, 20, 20, 20));

        add(scrollPane, BorderLayout.CENTER);
        add(errorLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    private JPanel createNonEditableSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Movie Information (Read-only)"));

        // Poster and title row
        JPanel topRow = new JPanel(new BorderLayout(15, 0));
        
        // Poster
        JLabel posterLabel = new JLabel();
        if (movie.getPosterUrl() != null && !movie.getPosterUrl().isEmpty()) {
            try {
                java.net.URL imageUrl = new java.net.URL(movie.getPosterUrl());
                ImageIcon icon = new ImageIcon(imageUrl);
                Image scaled = icon.getImage().getScaledInstance(120, 180, Image.SCALE_SMOOTH);
                posterLabel.setIcon(new ImageIcon(scaled));
            } catch (Exception e) {
                posterLabel.setText("No Image");
                posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }
        } else {
            posterLabel.setText("No Image");
            posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        posterLabel.setPreferredSize(new Dimension(120, 180));
        posterLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Title and basic info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        JLabel titleLabel = new JLabel("<html><b>" + movie.getTitle() + "</b></html>");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        infoPanel.add(titleLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Genre IDs
        List<Integer> genreIds = movie.getGenreIds();
        if (!genreIds.isEmpty()) {
            String genreText = "Genres: " + genreIds.toString();
            JLabel genreLabel = new JLabel(genreText);
            genreLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            infoPanel.add(genreLabel);
        }

        // Release date
        if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
            JLabel releaseLabel = new JLabel("Release Date: " + movie.getReleaseDate());
            releaseLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            infoPanel.add(releaseLabel);
        }

        // Rating
        JLabel ratingLabel = new JLabel("TMDb Rating: " + String.format("%.1f", movie.getRating()) + "/10");
        ratingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        infoPanel.add(ratingLabel);

        // Plot
        if (movie.getPlot() != null && !movie.getPlot().isEmpty()) {
            infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            JTextArea plotArea = new JTextArea(movie.getPlot());
            plotArea.setEditable(false);
            plotArea.setLineWrap(true);
            plotArea.setWrapStyleWord(true);
            plotArea.setBackground(getBackground());
            plotArea.setFont(new Font("Arial", Font.PLAIN, 12));
            JScrollPane plotScroll = new JScrollPane(plotArea);
            plotScroll.setPreferredSize(new Dimension(400, 80));
            infoPanel.add(plotScroll);
        }

        topRow.add(posterLabel, BorderLayout.WEST);
        topRow.add(infoPanel, BorderLayout.CENTER);

        panel.add(topRow);
        return panel;
    }

    private JPanel createEditableSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new TitledBorder("Editable Information"));

        // Watched Date
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Date Watched:"));
        watchedDateField = new JTextField(10);
        LocalDateTime watchedDate = watchedMovie.getWatchedDate();
        watchedDateField.setText(watchedDate.format(DATE_FORMATTER));
        datePanel.add(watchedDateField);
        datePanel.add(new JLabel("Time (HH:mm):"));
        watchedTimeField = new JTextField(5);
        watchedTimeField.setText(watchedDate.format(DateTimeFormatter.ofPattern("HH:mm")));
        datePanel.add(watchedTimeField);
        datePanel.add(new JLabel("(Format: YYYY-MM-DD HH:mm)"));
        panel.add(datePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Rating
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratingPanel.add(new JLabel("Your Rating (1-5):"));
        ratingComboBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        if (existingReview != null) {
            ratingComboBox.setSelectedItem(existingReview.getRating());
        } else {
            ratingComboBox.setSelectedItem(3);
        }
        ratingPanel.add(ratingComboBox);
        panel.add(ratingPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Review
        JPanel reviewPanel = new JPanel(new BorderLayout());
        reviewPanel.add(new JLabel("Your Review:"), BorderLayout.NORTH);
        reviewTextArea = new JTextArea(5, 40);
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setWrapStyleWord(true);
        if (existingReview != null && existingReview.getComment() != null) {
            reviewTextArea.setText(existingReview.getComment());
        }
        JScrollPane reviewScroll = new JScrollPane(reviewTextArea);
        reviewPanel.add(reviewScroll, BorderLayout.CENTER);
        panel.add(reviewPanel);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(100, 35));
        saveButton.setBackground(new Color(40, 167, 69)); // Green
        saveButton.setForeground(Color.WHITE);
        saveButton.setOpaque(true);
        saveButton.setContentAreaFilled(true);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> saveChanges());

        JButton deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(100, 35));
        deleteButton.setBackground(new Color(220, 53, 69)); // Red
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setOpaque(true);
        deleteButton.setContentAreaFilled(true);
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(e -> deleteMovie());

        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            dispose();
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
        });

        panel.add(saveButton);
        panel.add(deleteButton);
        panel.add(backButton);

        return panel;
    }

    private void saveChanges() {
        errorLabel.setText("");

        try {
            // Parse watched date and time
            String dateStr = watchedDateField.getText().trim();
            String timeStr = watchedTimeField.getText().trim();
            LocalDateTime watchedDate;
            
            if (dateStr.isEmpty()) {
                errorLabel.setText("Date watched cannot be empty.");
                return;
            }

            try {
                if (timeStr.isEmpty()) {
                    watchedDate = LocalDateTime.parse(dateStr + " 00:00", DATE_TIME_FORMATTER);
                } else {
                    watchedDate = LocalDateTime.parse(dateStr + " " + timeStr, DATE_TIME_FORMATTER);
                }
            } catch (Exception e) {
                errorLabel.setText("Invalid date/time format. Use YYYY-MM-DD HH:mm");
                return;
            }

            // Get rating and review
            Integer rating = (Integer) ratingComboBox.getSelectedItem();
            String review = reviewTextArea.getText().trim();

            // Call controller
            editController.editMovie(userName, movie.getMovieId(), watchedDate, rating, review);
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    private void deleteMovie() {
        DeleteConfirmationPopup confirmDialog = new DeleteConfirmationPopup(
                (JFrame) getParent(), movie.getTitle()
        );
        confirmDialog.setVisible(true);

        if (confirmDialog.isConfirmed()) {
            deleteController.deleteMovie(userName, movie.getMovieId());
        }
    }

    @Override
    public void onEditSuccess(EditWatchedMovieResponseModel responseModel) {
        JOptionPane.showMessageDialog(this,
                "Movie updated successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
    }

    @Override
    public void onEditError(String errorMessage) {
        errorLabel.setText("Error: " + errorMessage);
    }

    @Override
    public void onDeleteSuccess(DeleteWatchedMovieResponseModel responseModel) {
        JOptionPane.showMessageDialog(this,
                "Movie deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        dispose();
        if (onCloseCallback != null) {
            onCloseCallback.run();
        }
    }

    @Override
    public void onDeleteError(String errorMessage) {
        errorLabel.setText("Error: " + errorMessage);
    }
}

