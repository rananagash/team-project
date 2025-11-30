package view;

import common.GenreUtils;
import entity.Movie;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Popup dialog for filtering movies by genre.
 * Allows users to select one or more genres and filter movies accordingly.
 */
public class FilterMoviesPopup extends JDialog {

    private final LoggedInView loggedInView;
    private final List<JCheckBox> genreCheckBoxes = new ArrayList<>();
    private final Map<Integer, String> allGenres;

    public FilterMoviesPopup(JFrame parent, LoggedInView loggedInView) {
        super(parent, "Filter Movies by Genre", true);
        this.loggedInView = loggedInView;
        this.allGenres = GenreUtils.getAllGenres();

        setLayout(new BorderLayout());
        setSize(400, 500);
        setLocationRelativeTo(parent);

        // Create genre selection panel with checkboxes
        JPanel genrePanel = new JPanel();
        genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.Y_AXIS));
        genrePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Select genres to filter:");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        genrePanel.add(titleLabel);
        genrePanel.add(Box.createVerticalStrut(10));

        // Add checkboxes for each genre
        for (Map.Entry<Integer, String> entry : allGenres.entrySet()) {
            JCheckBox checkBox = new JCheckBox(entry.getValue());
            genreCheckBoxes.add(checkBox);
            genrePanel.add(checkBox);
        }

        // Scroll pane for genre list
        JScrollPane scrollPane = new JScrollPane(genrePanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton filterButton = new JButton("Filter");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(filterButton);
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

            loggedInView.filterCurrentMovies(selectedGenreIds);
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
}

