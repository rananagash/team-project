package view;

import interface_adapter.search_movie.SearchMovieController;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;

import java.util.List;

public class SearchResultsView extends JPanel {

    private SearchMovieController controller;

    private JTextField queryField;
    private JButton searchButton;
    private JButton prevPageButton;
    private JButton nextPageButton;
    private JLabel pageLabel;
    private JLabel statusLabel;
    private JList<String> resultsList;
    private DefaultListModel<String> listModel;

    private int currentPage = 1;
    private int totalPages = 1;
    private String lastQuery = "";

    public SearchResultsView() {
        initComponents();
    }

    public void setController(SearchMovieController controller) {
        this.controller = controller;
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(8, 8));

        // ===== Top search bar =====
        JPanel searchPanel = new JPanel(new BorderLayout(4, 4));
        queryField = new JTextField();
        searchButton = new JButton("Search");
        searchPanel.add(queryField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        this.add(searchPanel, BorderLayout.NORTH);

        // ===== Result list =====
        listModel = new DefaultListModel<>();
        resultsList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(resultsList);
        this.add(scrollPane, BorderLayout.CENTER);

        // ===== Pagination + status =====
        JPanel bottomPanel = new JPanel(new BorderLayout(4, 4));

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        prevPageButton = new JButton("Prev");
        nextPageButton = new JButton("Next");
        pageLabel = new JLabel("Page 1 / 1");

        paginationPanel.add(prevPageButton);
        paginationPanel.add(nextPageButton);
        paginationPanel.add(pageLabel);

        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.DARK_GRAY);

        bottomPanel.add(paginationPanel, BorderLayout.WEST);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        this.add(bottomPanel, BorderLayout.SOUTH);

        // ===== Event Bindings =====
        searchButton.addActionListener(e -> {
            if (controller == null) return;

            String query = queryField.getText().trim();
            if (query.isEmpty()) {
                showError("Search query cannot be empty.");
                return;
            }

            lastQuery = query;
            currentPage = 1;
            setStatus("Searching...");
            controller.search(lastQuery, currentPage);
        });

        prevPageButton.addActionListener(e -> {
            if (controller == null || lastQuery.isEmpty()) return;

            if (currentPage > 1) {
                currentPage--;
                setStatus("Loading page " + currentPage + "...");
                controller.search(lastQuery, currentPage);
            }
        });

        nextPageButton.addActionListener(e -> {
            if (controller == null || lastQuery.isEmpty()) return;

            if (currentPage < totalPages) {
                currentPage++;
                setStatus("Loading page " + currentPage + "...");
                controller.search(lastQuery, currentPage);
            }
        });

        updatePaginationButtons();
    }

    public void showResults(List<String> movieLines, int currentPage, int totalPages) {
        this.currentPage = currentPage;
        this.totalPages = Math.max(totalPages, 1);

        listModel.clear();
        for (String line : movieLines) {
            listModel.addElement(line);
        }

        updatePaginationButtons();
        setStatus("Found " + movieLines.size() + " results (page " + currentPage + ").");
    }

    public void showError(String message) {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText(message);
    }

    public void setStatus(String message) {
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setText(message);
    }

    private void updatePaginationButtons() {
        prevPageButton.setEnabled(currentPage > 1);
        nextPageButton.setEnabled(currentPage < totalPages);
        pageLabel.setText("Page " + currentPage + " / " + totalPages);
    }
}

