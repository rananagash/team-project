package view;

import common.GenreUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Popup dialog for filtering movies by genre.
 * Allows users to select one or more genres and filter movies accordingly.
 */
public class FilterMoviesPopup extends JDialog {

    private final LoggedInView loggedInView;
    private final List<JCheckBox> genreCheckBoxes = new ArrayList<>();
    private final Map<Integer, String> allGenres;

    public FilterMoviesPopup(JFrame parent, LoggedInView loggedInView) {
        super(parent, "🎬 Filter Movies by Genre", true);
        this.loggedInView = loggedInView;
        this.allGenres = GenreUtils.getAllGenres();

        setLayout(new BorderLayout());
        setSize(520, 580);
        setLocationRelativeTo(parent);

        // Modern dark theme styling
        Color darkBackground = new Color(15, 23, 42);
        Color cardBackground = new Color(30, 41, 59);
        Color textColor = new Color(209, 213, 219);
        Color accentBlue = new Color(59, 130, 246);

        getContentPane().setBackground(darkBackground);

        // Create header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(cardBackground);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 26)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel titleLabel = new JLabel("Select Genres to Filter");
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        // Create genre selection panel with checkboxes
        JPanel genrePanel = new JPanel();
        genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.Y_AXIS));
        genrePanel.setBackground(cardBackground);
        genrePanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Add checkboxes for each genre with modern styling
        for (Map.Entry<Integer, String> entry : allGenres.entrySet()) {
            JCheckBox checkBox = new JCheckBox(entry.getValue());
            checkBox.setFont(new Font("Helvetica", Font.PLAIN, 14));
            checkBox.setForeground(textColor);
            checkBox.setBackground(cardBackground);
            checkBox.setOpaque(false);
            checkBox.setFocusPainted(false);
            checkBox.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            genreCheckBoxes.add(checkBox);
            genrePanel.add(checkBox);
            genrePanel.add(Box.createVerticalStrut(2));
        }

        // Scroll pane for genre list with custom styling
        JScrollPane scrollPane = new JScrollPane(genrePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(cardBackground);
        scrollPane.setBackground(cardBackground);

        // Button panel with modern styling
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        buttonPanel.setBackground(cardBackground);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(255, 255, 255, 26)),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Style filter button - wider to fit text
        JButton filterButton = new JButton("Apply Filter");
        styleButton(filterButton, accentBlue, Color.WHITE, 130);

        // Style clear filter button - wider to fit text
        JButton clearFilterButton = new JButton("Clear Filter");
        styleButton(clearFilterButton, new Color(107, 114, 128), Color.WHITE, 130);

        // Style cancel button
        JButton cancelButton = new JButton("Cancel");
        styleButton(cancelButton, new Color(75, 85, 99), Color.WHITE, 100);

        buttonPanel.add(filterButton);
        buttonPanel.add(clearFilterButton);
        buttonPanel.add(cancelButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Filter button action
        filterButton.addActionListener(e -> {
            List<Integer> selectedGenreIds = getSelectedGenreIds();
            if (selectedGenreIds.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select at least one genre.",
                        "No Genre Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Filter movies and check if any were found
            boolean hasResults = loggedInView.filterCurrentMoviesAndCheck(selectedGenreIds);

            // If no results found, keep dialog open so user can try different genres
            // If results found, close dialog to show results
            if (hasResults) {
                dispose();
            } else {
                // Show message but keep dialog open
                JOptionPane.showMessageDialog(this,
                        "No movies found matching the selected genres. Please try different genres.",
                        "No Results",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Clear Filter button action - resets to show all movies
        clearFilterButton.addActionListener(e -> {
            // Clear all checkboxes
            for (JCheckBox checkBox : genreCheckBoxes) {
                checkBox.setSelected(false);
            }
            // Restore original movies
            loggedInView.clearFilter();
            dispose();
        });

        // Cancel button action
        cancelButton.addActionListener(e -> dispose());
    }

    /**
     * Gets the list of genre IDs for all selected genres.
     *
     * @return list of selected genre IDs
     */
    private List<Integer> getSelectedGenreIds() {
        List<Integer> selectedIds = new ArrayList<>();
        Map<Integer, String> genreMap = GenreUtils.getAllGenres();

        int index = 0;
        for (Map.Entry<Integer, String> entry : genreMap.entrySet()) {
            if (genreCheckBoxes.get(index).isSelected()) {
                selectedIds.add(entry.getKey());
            }
            index++;
        }

        return selectedIds;
    }

    /**
     * Styles a button with modern appearance and hover effects.
     *
     * @param button the button to style
     * @param backgroundColor the background color
     * @param textColor the text color
     * @param width the preferred width of the button
     */
    private void styleButton(JButton button, Color backgroundColor, Color textColor, int width) {
        button.setFont(new Font("Helvetica", Font.BOLD, 13));
        button.setPreferredSize(new Dimension(width, 38));
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        final Color originalBg = backgroundColor;
        final Color hoverBg = new Color(
                Math.max(0, backgroundColor.getRed() - 20),
                Math.max(0, backgroundColor.getGreen() - 20),
                Math.max(0, backgroundColor.getBlue() - 20)
        );
        final Color pressedBg = new Color(
                Math.max(0, backgroundColor.getRed() - 40),
                Math.max(0, backgroundColor.getGreen() - 40),
                Math.max(0, backgroundColor.getBlue() - 40)
        );

        // Add hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBg);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalBg);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(pressedBg);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(originalBg);
            }
        });
    }
}

