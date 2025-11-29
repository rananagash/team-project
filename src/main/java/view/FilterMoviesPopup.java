package view;

import interface_adapter.filter_movies.FilterMoviesController;
import interface_adapter.filter_movies.FilterMoviesViewModel;
import use_case.filter_movies.FilterMoviesInteractor;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Swing-based popup dialog for filtering movies by genre.
 *
 * <p>Allows users to select one or more genres and filter movies accordingly.
 * Displays the filtered results in the parent LoggedInView.
 */
public class FilterMoviesPopup extends JDialog implements PropertyChangeListener {

    private final FilterMoviesController controller;
    private final FilterMoviesViewModel viewModel;
    private final Map<Integer, String> genres;
    private JPanel genrePanel;
    private JList<String> resultsList;
    private DefaultListModel<String> listModel;
    private JLabel statusLabel;
    private JButton filterButton;
    private JButton closeButton;
    private List<JCheckBox> genreCheckBoxes;

    /**
     * Constructs the popup UI for filtering movies by genre.
     *
     * @param parent the parent Swing frame
     * @param controller the controller handling filter operations
     * @param viewModel the view model for filter movies
     */
    public FilterMoviesPopup(JFrame parent,
                             FilterMoviesController controller,
                             FilterMoviesViewModel viewModel) {
        super(parent, "Filter Movies by Genre", true);
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);
        this.genres = FilterMoviesInteractor.getAllGenres();

        initComponents();

        setPreferredSize(new Dimension(600, 500));
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // ===== Genre selection panel =====
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        JLabel titleLabel = new JLabel("Select genres to filter:");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        genrePanel = new JPanel();
        genrePanel.setLayout(new BoxLayout(genrePanel, BoxLayout.Y_AXIS));
        genreCheckBoxes = new ArrayList<>();

        // Create checkboxes for each genre
        List<Map.Entry<Integer, String>> sortedGenres = genres.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .toList();

        for (Map.Entry<Integer, String> entry : sortedGenres) {
            JCheckBox checkBox = new JCheckBox(entry.getValue());
            checkBox.setActionCommand(String.valueOf(entry.getKey()));
            genreCheckBoxes.add(checkBox);
            genrePanel.add(checkBox);
        }

        JScrollPane genreScrollPane = new JScrollPane(genrePanel);
        genreScrollPane.setPreferredSize(new Dimension(200, 200));
        topPanel.add(genreScrollPane, BorderLayout.CENTER);

        // ===== Filter button =====
        filterButton = new JButton("Filter Movies");
        filterButton.addActionListener(e -> handleFilter());
        topPanel.add(filterButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.WEST);

        // ===== Results panel =====
        JPanel resultsPanel = new JPanel(new BorderLayout(5, 5));
        JLabel resultsLabel = new JLabel("Filtered Results:");
        resultsLabel.setFont(resultsLabel.getFont().deriveFont(Font.BOLD));
        resultsPanel.add(resultsLabel, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        resultsList = new JList<>(listModel);
        JScrollPane resultsScrollPane = new JScrollPane(resultsList);
        resultsScrollPane.setPreferredSize(new Dimension(350, 300));
        resultsPanel.add(resultsScrollPane, BorderLayout.CENTER);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.DARK_GRAY);
        resultsPanel.add(statusLabel, BorderLayout.SOUTH);

        add(resultsPanel, BorderLayout.CENTER);

        // ===== Close button =====
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleFilter() {
        List<Integer> selectedGenreIds = new ArrayList<>();

        for (JCheckBox checkBox : genreCheckBoxes) {
            if (checkBox.isSelected()) {
                selectedGenreIds.add(Integer.parseInt(checkBox.getActionCommand()));
            }
        }

        if (selectedGenreIds.isEmpty()) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Please select at least one genre.");
            return;
        }

        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setText("Filtering movies...");
        controller.filterByGenres(selectedGenreIds);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("filteredMovies") ||
                evt.getPropertyName().equals("errorMessage")) {
            updateResults();
        }
    }

    private void updateResults() {
        listModel.clear();

        if (viewModel.hasError()) {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText(viewModel.getErrorMessage());
            return;
        }

        List<entity.Movie> movies = viewModel.getFilteredMovies();

        if (movies.isEmpty()) {
            statusLabel.setForeground(Color.DARK_GRAY);
            statusLabel.setText("No movies found for selected genres.");
        } else {
            statusLabel.setForeground(Color.DARK_GRAY);
            statusLabel.setText("Found " + movies.size() + " movies");

            for (entity.Movie movie : movies) {
                String display = String.format("%s (%.1f) - %s",
                        movie.getTitle(),
                        movie.getRating(),
                        movie.getPlot().length() > 50
                                ? movie.getPlot().substring(0, 50) + "..."
                                : movie.getPlot());
                listModel.addElement(display);
            }
        }
    }
}

