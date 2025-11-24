package view;

import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.view_watchhistory.ViewWatchHistoryController;
import interface_adapter.search_movie.SearchMovieController;
import interface_adapter.view_profile.ViewProfileController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class LoggedInView extends JPanel implements PropertyChangeListener {

    private final String viewName = "logged in";
    private SearchMovieController searchController;
    private LoggedInViewModel loggedInViewModel;
    private ChangePasswordController changePasswordController;
    private LogoutController logoutController;
    private ViewWatchHistoryController viewWatchHistoryController;
    private interface_adapter.review_movie.ReviewMovieController reviewMovieController;
    private interface_adapter.review_movie.ReviewMovieViewModel reviewMovieViewModel;

    private JTextField queryField;
    private JButton searchButton;
    private JButton prevPageButton;
    private JButton nextPageButton;
    private JLabel pageLabel;
    private JLabel statusLabel;
    private JList<String> resultsList;
    private DefaultListModel<String> listModel;
    private JLabel username;
    private JTextField passwordInputField;
    private JLabel passwordErrorField;

    private int currentPage = 1;
    private int totalPages = 1;
    private String lastQuery = "";

    private interface_adapter.view_profile.ViewProfileController profileController;


    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(8, 8));

        // ===== Top bar with user info and account buttons =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel usernameLabel = new JLabel("Logged in as: ");
        username = new JLabel();
        topPanel.add(usernameLabel);
        topPanel.add(username);

        topPanel.add(new JSeparator(SwingConstants.VERTICAL));

        JButton logOutBtn = new JButton("Log Out");
        logOutBtn.addActionListener(e -> {
            if (logoutController != null) {
                logoutController.execute();
            }
        });
        topPanel.add(logOutBtn);

        JButton profileBtn = new JButton("Profile");
        profileBtn.addActionListener(e -> {
            if (profileController != null) {
                final LoggedInState currentState = loggedInViewModel.getState();
                profileController.switchToProfile(currentState.getUsername());
            }
        });
        topPanel.add(profileBtn);

        JButton changePasswordBtn = new JButton("Change Password");
        changePasswordBtn.addActionListener(e -> {
            if (changePasswordController != null) {
                final LoggedInState currentState = loggedInViewModel.getState();
                changePasswordController.execute(
                        currentState.getUsername(),
                        currentState.getPassword()
                );
            }
        });
        topPanel.add(changePasswordBtn);

        JButton viewHistoryBtn = new JButton("View Watch History");
        viewHistoryBtn.addActionListener(e -> {
            if (viewWatchHistoryController != null) {
                final LoggedInState currentState = loggedInViewModel.getState();
                viewWatchHistoryController.loadHistory(currentState.getUsername());
            }
        });
        topPanel.add(viewHistoryBtn);

        JButton reviewBtn = new JButton("Review a movie");
        reviewBtn.addActionListener(e -> {
            if (reviewMovieController != null && reviewMovieViewModel != null) {
                final LoggedInState state = loggedInViewModel.getState();
                String username = state.getUsername();

                JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                AddReviewPopup popup = new AddReviewPopup(
                        parent,
                        reviewMovieController,
                        reviewMovieViewModel,
                        username,
                        "299534",
                        "Avengers Endgame"
                );

                popup.setVisible(true);
            }
        });
        topPanel.add(reviewBtn);

        this.add(topPanel, BorderLayout.NORTH);

        // ===== Search bar =====
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

        // ===== Password field for change password =====
        passwordInputField = new JTextField(15);
        passwordInputField.getDocument().addDocumentListener(new DocumentListener() {
            private void documentListenerHelper() {
                final LoggedInState currentState = loggedInViewModel.getState();
                currentState.setPassword(passwordInputField.getText());
                loggedInViewModel.setState(currentState);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                documentListenerHelper();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                documentListenerHelper();
            }
        });

        passwordErrorField = new JLabel();

        // ===== Event Bindings =====
        searchButton.addActionListener(e -> {
            if (searchController == null) return;

            String query = queryField.getText().trim();
            if (query.isEmpty()) {
                showError("Search query cannot be empty.");
                return;
            }

            lastQuery = query;
            currentPage = 1;
            setStatus("Searching...");
            searchController.search(lastQuery, currentPage);
        });

        prevPageButton.addActionListener(e -> {
            if (searchController == null || lastQuery.isEmpty()) return;

            if (currentPage > 1) {
                currentPage--;
                setStatus("Loading page " + currentPage + "...");
                searchController.search(lastQuery, currentPage);
            }
        });

        nextPageButton.addActionListener(e -> {
            if (searchController == null || lastQuery.isEmpty()) return;

            if (currentPage < totalPages) {
                currentPage++;
                setStatus("Loading page " + currentPage + "...");
                searchController.search(lastQuery, currentPage);
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            username.setText(state.getUsername());
        } else if (evt.getPropertyName().equals("password")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            if (state.getPasswordError() == null) {
                JOptionPane.showMessageDialog(this, "password updated for " + state.getUsername());
                passwordInputField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, state.getPasswordError());
            }
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setController(SearchMovieController controller) {
        this.searchController = controller;
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
    }

    public void setProfileController(interface_adapter.view_profile.ViewProfileController controller) {
        this.profileController = controller;
    }

    public void setViewWatchHistoryController(ViewWatchHistoryController viewWatchHistoryController) {
        this.viewWatchHistoryController = viewWatchHistoryController;
    }

    public void setReviewMovieController(interface_adapter.review_movie.ReviewMovieController controller) {
        this.reviewMovieController = controller;
    }

    public void setReviewMovieViewModel(interface_adapter.review_movie.ReviewMovieViewModel viewModel) {
        this.reviewMovieViewModel = viewModel;
    }

    public void setViewProfileController(ViewProfileController viewProfileController) {
    }
}