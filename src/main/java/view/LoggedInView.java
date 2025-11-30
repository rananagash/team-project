package view;

import entity.Movie;
import entity.User;
import interface_adapter.add_to_watchlist.AddWatchListController;
import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;
import interface_adapter.record_watchhistory.RecordWatchHistoryController;
import interface_adapter.record_watchhistory.RecordWatchHistoryPresenter;
import interface_adapter.view_watchhistory.ViewWatchHistoryController;
import interface_adapter.search_movie.SearchMovieController;
import interface_adapter.view_profile.ViewProfileController;
import interface_adapter.view_watchlists.ViewWatchListsController;
import interface_adapter.filter_movies.FilterMoviesController;
import interface_adapter.filter_movies.FilterMoviesViewModel;
import use_case.record_watchhistory.RecordWatchHistoryInteractor;
import use_case.common.MovieGateway;
import use_case.common.PagedMovieResult;
import use_case.common.MovieDataAccessException;
import interface_adapter.MovieViewData;
import view.components.MovieCard;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;

/**
 * The View for when the user is logged into the program.
 * Displays search results, pagination, and account management buttons.
 */
public class LoggedInView extends JPanel implements PropertyChangeListener {

    private final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;

    // Controllers
    private SearchMovieController searchController;
    private ChangePasswordController changePasswordController;
    private LogoutController logoutController;
    private ViewWatchHistoryController viewWatchHistoryController;
    private interface_adapter.review_movie.ReviewMovieController reviewMovieController;
    private ViewProfileController viewProfileController;
    private AddWatchListController addWatchListController;
    private RecordWatchHistoryController recordWatchHistoryController;
    private ViewWatchListsController viewWatchListsController;
    private FilterMoviesController filterMoviesController;

    // ViewModels
    private interface_adapter.review_movie.ReviewMovieViewModel reviewMovieViewModel;
    private FilterMoviesViewModel filterMoviesViewModel;

    // UI Components - Top panel
    private JLabel username;
    private JButton logOutBtn;
    private JButton changePasswordBtn;
    private JButton viewHistoryBtn;
    private JButton profileBtn;
    private JButton reviewBtn;

    // Middle Panel (testing only)
    //TODO: remove before final version
    private JPanel middlePanel;
    private JButton addToWatchListBtn;
    private JButton addToHistoryBtn;
    private JButton rateReviewBtn;

    // UI Components - Search panel
    private JTextField queryField;
    private JButton searchButton;

    // UI Components - Results panel (using MovieCard components)
    private JPanel resultsPanel;
    private JScrollPane resultsScrollPane;

    // UI Components - Pagination
    private JButton prevPageButton;
    private JButton nextPageButton;
    private JLabel pageLabel;
    private JLabel statusLabel;

    // UI Components - Password (for change password)
    private JTextField passwordInputField;

    // State
    private int currentPage = 1;
    private int totalPages = 1;
    private String lastQuery = "";

    // Movie gateway for loading popular movies
    private MovieGateway movieGateway;

    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(8, 8));

        // ===== Top panel with account buttons =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel usernameLabel = new JLabel("Logged in as: ");
        username = new JLabel();
        topPanel.add(usernameLabel);
        topPanel.add(username);

        topPanel.add(new JSeparator(SwingConstants.VERTICAL));

        logOutBtn = new JButton("Log Out");
        logOutBtn.addActionListener(e -> {
            if (logoutController != null) {
                logoutController.execute();
            }
        });
        topPanel.add(logOutBtn);

        changePasswordBtn = new JButton("Change Password");
        changePasswordBtn.addActionListener(e -> {
            if (changePasswordController != null) {
                final LoggedInState currentState = loggedInViewModel.getState();
                if (currentState != null) {
                    changePasswordController.execute(
                            currentState.getUsername(),
                            currentState.getPassword()
                    );
                }
            }
        });
        topPanel.add(changePasswordBtn);

        viewHistoryBtn = new JButton("View Watch History");
        viewHistoryBtn.addActionListener(e -> {
            if (viewWatchHistoryController != null) {
                final LoggedInState currentState = loggedInViewModel.getState();
                if (currentState != null) {
                    viewWatchHistoryController.loadHistory(currentState.getUsername());
                }
            }
        });
        topPanel.add(viewHistoryBtn);

        profileBtn = new JButton("Profile");
        profileBtn.addActionListener(e -> {
            if (viewProfileController != null) {
                final LoggedInState currentState = loggedInViewModel.getState();
                if (currentState != null) {
                    viewProfileController.switchToProfile(currentState.getUsername());
                }
            }
        });
        topPanel.add(profileBtn);

        reviewBtn = new JButton("Review a movie");
        reviewBtn.addActionListener(e -> {
            if (reviewMovieController != null && reviewMovieViewModel != null) {
                final LoggedInState state = loggedInViewModel.getState();
                if (state != null) {
                    String reviewUsername = state.getUsername();
                    JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
                    AddReviewPopup popup = new AddReviewPopup(
                            parent,
                            reviewMovieController,
                            reviewMovieViewModel,
                            reviewUsername,
                            "299534",  // TODO: Make this configurable instead of hardcoded
                            "Avengers Endgame"
                    );
                    popup.setVisible(true);
                }
            }
        });
        topPanel.add(reviewBtn);

        // Middle panel buttons for testing only
        //TODO: remove before final version
        middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        addToWatchListBtn = new JButton("Add to Watchlist (TEST)");
        addToHistoryBtn = new JButton("Add to History (TEST)");
        rateReviewBtn = new JButton("Rate & Review (TEST)");

        middlePanel.add(addToWatchListBtn);
        middlePanel.add(addToHistoryBtn);
        middlePanel.add(rateReviewBtn);

        addToWatchListBtn.addActionListener(e -> {
            final LoggedInState currentState = loggedInViewModel.getState();
            if (currentState == null) return;

            //Dummy user - real button should get current logged in user
            User user = new User("dummy-user", "password");

            //Dummy movie
            Movie movie = new Movie("m1",
                    "Test Movie",
                    "A test movie plot happens",
                    List.of(1, 2),
                    "2025-01-01",
                    7.5,
                    0.0,
                    "poster-url");

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            // Create popup
            new AddToWatchListPopup(
                    parent,
                    user,
                    movie,
                    addWatchListController
            );
        });

        addToHistoryBtn.addActionListener(e -> {
            final LoggedInState currentState = loggedInViewModel.getState();
            if (currentState == null) return;

            //Dummy user - real button should get current logged in user
            User user = new User("dummy-user", "password");

            //Dummy movie
            Movie movie = new Movie("m1",
                    "Test Movie",
                    "A test movie plot happens",
                    List.of(1, 2),
                    "2025-01-01",
                    7.5,
                    0.0,
                    "poster-url");

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            // Create popup
            RecordWatchHistoryPopup popup = new RecordWatchHistoryPopup(parent);

            //TODO:Jiaqi I couldn't quite get your implementation to work
        });

        rateReviewBtn.addActionListener(e -> {
            final LoggedInState currentState = loggedInViewModel.getState();
            if (currentState == null) return;

            //Dummy user - real button should get current logged in user
            User user = new User("dummy-user", "password");

            //Dummy movie
            Movie movie = new Movie("m1",
                    "Test Movie",
                    "A test movie plot happens",
                    List.of(1, 2),
                    "2025-01-01",
                    7.5,
                    0.0,
                    "poster-url");

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            // Create popup
            AddReviewPopup popup = new AddReviewPopup(
                    parent,
                    reviewMovieController,
                    reviewMovieViewModel,
                    user.getUserName(),
                    movie.getMovieId(),
                    movie.getTitle());

            popup.setVisible(true);
        });

//        this.add(topPanel, BorderLayout.NORTH);


        // ===== Search bar =====
        JPanel searchPanel = new JPanel(new BorderLayout(4, 4));
        queryField = new JTextField();

        // Add Enter key support for search
        queryField.addActionListener(e -> performSearch());

        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> performSearch());

        searchPanel.add(queryField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
//        this.add(searchPanel, BorderLayout.NORTH);

        // Wrap two top panels so they can both be in the "NORTH" section of BorderLayout
        JPanel northWrapper = new JPanel();
        northWrapper.setLayout(new BorderLayout());
        northWrapper.add(topPanel, BorderLayout.NORTH);
        northWrapper.add(middlePanel, BorderLayout.CENTER); //TODO: just for testing
        northWrapper.add(searchPanel, BorderLayout.SOUTH);
        this.add(northWrapper, BorderLayout.NORTH);

        // ===== Results panel with MovieCard components =====
        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(new Color(245, 245, 245));
        resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        resultsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        resultsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        this.add(resultsScrollPane, BorderLayout.CENTER);

        // ===== Pagination + status =====
        JPanel bottomPanel = new JPanel(new BorderLayout(4, 4));

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        prevPageButton = new JButton("Prev");
        prevPageButton.addActionListener(e -> {
            if (searchController == null || lastQuery.isEmpty()) return;

            if (currentPage > 1) {
                currentPage--;
                setStatus("Loading page " + currentPage + "...");
                searchController.search(lastQuery, currentPage);
            }
        });

        nextPageButton = new JButton("Next");
        nextPageButton.addActionListener(e -> {
            if (searchController == null || lastQuery.isEmpty()) return;

            if (currentPage < totalPages) {
                currentPage++;
                setStatus("Loading page " + currentPage + "...");
                searchController.search(lastQuery, currentPage);
            }
        });

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
                if (currentState != null) {
                    currentState.setPassword(passwordInputField.getText());
                    loggedInViewModel.setState(currentState);
                }
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

        updatePaginationButtons();
    }

    /**
     * Perform a movie search.
     */
    private void performSearch() {
        if (searchController == null) {
            System.err.println("SearchController is null when performSearch() called");
            showError("Search functionality not initialized. Please try again.");
            return;
        }

        String query = queryField.getText().trim();
        if (query.isEmpty()) {
            showError("Search query cannot be empty.");
            return;
        }

        System.out.println("Performing search for: " + query);
        lastQuery = query;
        currentPage = 1;
        setStatus("Searching...");

        // Run search in background thread to avoid blocking UI
        new Thread(() -> {
            try {
                searchController.search(lastQuery, currentPage);
            } catch (Exception e) {
                e.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    showError("Error performing search: " + e.getMessage());
                    setStatus("Search failed.");
                });
            }
        }).start();
    }

    /**
     * Display search results with pagination using MovieCard components.
     */
    public void showResults(List<Movie> movies, int currentPage, int totalPages) {
        this.currentPage = currentPage;
        this.totalPages = Math.max(totalPages, 1);

        // Clear previous results
        resultsPanel.removeAll();

        // Add MovieCard for each movie
        for (Movie movie : movies) {
            MovieViewData movieViewData = new MovieViewData(
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getPlot(),
                    movie.getGenreIds(),
                    movie.getReleaseDate(),
                    movie.getRating(),
                    movie.getPosterUrl()
            );

            MovieCard movieCard = new MovieCard(movieViewData);
            movieCard.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            resultsPanel.add(movieCard);
            resultsPanel.add(Box.createVerticalStrut(10));
        }

        // Add glue to push content to top
        resultsPanel.add(Box.createVerticalGlue());

        // Refresh the panel
        resultsPanel.revalidate();
        resultsPanel.repaint();

        // Scroll to top
        SwingUtilities.invokeLater(() -> {
            resultsScrollPane.getVerticalScrollBar().setValue(0);
        });

        updatePaginationButtons();
        setStatus("Found " + movies.size() + " results (page " + currentPage + ").");
    }

    /**
     * Display an error message.
     */
    public void showError(String message) {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText(message);
    }

    /**
     * Display a status message.
     */
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
            if (state != null) {
                username.setText(state.getUsername());
            }
        } else if (evt.getPropertyName().equals("password")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();
            if (state != null) {
                if (state.getPasswordError() == null) {
                    JOptionPane.showMessageDialog(this, "password updated for " + state.getUsername());
                    passwordInputField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, state.getPasswordError());
                }
            }
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setController(SearchMovieController controller) {
        this.searchController = controller;
        if (controller == null) {
            System.err.println("Warning: SearchMovieController is null!");
        } else {
            System.out.println("SearchMovieController set successfully");
        }
    }

    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
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

    public void setViewProfileController(ViewProfileController controller) {
        this.viewProfileController = controller;
    }

    public void setAddWatchListController(AddWatchListController controller) {
        this.addWatchListController = controller;
    }

    public void setRecordWatchHistoryController(RecordWatchHistoryController controller) {
        this.recordWatchHistoryController = controller;
    }

    public void setViewWatchListsController(ViewWatchListsController controller) {
        this.viewWatchListsController = controller;
    }

    public void setFilterMoviesController(FilterMoviesController controller) {
        this.filterMoviesController = controller;
    }

    public void setFilterMoviesViewModel(FilterMoviesViewModel viewModel) {
        this.filterMoviesViewModel = viewModel;
    }

    public void setMovieGateway(MovieGateway movieGateway) {
        this.movieGateway = movieGateway;
        // If we're already on the logged in view and no movies are displayed, load popular movies
        if (movieGateway != null && resultsPanel != null && resultsPanel.getComponentCount() == 0) {
            loadPopularMovies();
        }
    }

    /**
     * Load popular movies from TMDB and display them.
     */
    public void loadPopularMovies() {
        if (movieGateway == null) {
            showError("Movie gateway not initialized. Cannot load popular movies.");
            return;
        }

        if (resultsPanel == null) {
            showError("Results panel not initialized. Cannot display movies.");
            return;
        }

        setStatus("Loading popular movies...");
        new Thread(() -> {
            try {
                PagedMovieResult result = movieGateway.getPopularMovies(1);
                List<Movie> movies = result.getMovies();

                if (movies == null || movies.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        showError("No popular movies found.");
                        setStatus("No movies to display.");
                    });
                    return;
                }

                SwingUtilities.invokeLater(() -> {
                    showResults(movies, result.getPage(), result.getTotalPages());
                    lastQuery = ""; // Clear last query since we're showing popular movies
                    setStatus("Loaded " + movies.size() + " popular movies.");
                });
            } catch (MovieDataAccessException e) {
                e.printStackTrace(); // Print stack trace for debugging
                SwingUtilities.invokeLater(() -> {
                    showError("Failed to load popular movies: " + e.getMessage());
                    setStatus("Error loading movies.");
                });
            } catch (Exception e) {
                e.printStackTrace(); // Print stack trace for debugging
                SwingUtilities.invokeLater(() -> {
                    showError("Unexpected error loading popular movies: " + e.getMessage());
                    setStatus("Error loading movies.");
                });
            }
        }).start();
    }
}