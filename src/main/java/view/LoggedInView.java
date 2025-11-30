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
import view.FilterMoviesPopup;
import common.GenreUtils;

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
import java.util.stream.Collectors;
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
    private JButton filterMoviesBtn;

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

    // Store current movies being displayed (for filtering)
    private List<Movie> currentMovies = new ArrayList<>();

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

        topPanel.add(new JSeparator(SwingConstants.VERTICAL));

        filterMoviesBtn = new JButton("Filter Movies");
        filterMoviesBtn.setVisible(true);
        filterMoviesBtn.addActionListener(e -> {
            if (currentMovies.isEmpty()) {
                showError("No movies to filter. Please search for movies first.");
                return;
            }
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            FilterMoviesPopup popup = new FilterMoviesPopup(parent, this);
            popup.setVisible(true);
        });
        topPanel.add(filterMoviesBtn);
        topPanel.revalidate();
        topPanel.repaint();

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
            if (currentPage <= 1) return;

            currentPage--;
            setStatus("Loading page " + currentPage + "...");

            // Handle pagination for search results
            if (searchController != null && !lastQuery.isEmpty()) {
                searchController.search(lastQuery, currentPage);
            }
            // Handle pagination for popular movies
            else if (movieGateway != null) {
                new Thread(() -> {
                    try {
                        PagedMovieResult result = movieGateway.getPopularMovies(currentPage);
                        List<Movie> movies = result.getMovies();

                        if (movies == null || movies.isEmpty()) {
                            SwingUtilities.invokeLater(() -> {
                                showError("No movies found on page " + currentPage + ".");
                                setStatus("No movies to display.");
                            });
                            return;
                        }

                        SwingUtilities.invokeLater(() -> {
                            showResults(movies, result.getPage(), result.getTotalPages());
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() -> {
                            showError("Error loading page " + currentPage + ": " + ex.getMessage());
                            setStatus("Failed to load page.");
                        });
                    }
                }).start();
            }
        });

        nextPageButton = new JButton("Next");
        nextPageButton.addActionListener(e -> {
            if (currentPage >= totalPages) return;

            currentPage++;
            setStatus("Loading page " + currentPage + "...");

            // Handle pagination for search results
            if (searchController != null && !lastQuery.isEmpty()) {
                searchController.search(lastQuery, currentPage);
            }
            // Handle pagination for popular movies
            else if (movieGateway != null) {
                new Thread(() -> {
                    try {
                        PagedMovieResult result = movieGateway.getPopularMovies(currentPage);
                        List<Movie> movies = result.getMovies();

                        if (movies == null || movies.isEmpty()) {
                            SwingUtilities.invokeLater(() -> {
                                showError("No movies found on page " + currentPage + ".");
                                setStatus("No movies to display.");
                            });
                            return;
                        }

                        SwingUtilities.invokeLater(() -> {
                            showResults(movies, result.getPage(), result.getTotalPages());
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        SwingUtilities.invokeLater(() -> {
                            showError("Error loading page " + currentPage + ": " + ex.getMessage());
                            setStatus("Failed to load page.");
                        });
                    }
                }).start();
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

    // Maximum number of movies to display at once to prevent UI lag
    private static final int MAX_MOVIES_TO_DISPLAY = 10;

    /**
     * Display search results with pagination using MovieCard components.
     */
    public void showResults(List<Movie> movies, int currentPage, int totalPages) {
        this.currentPage = currentPage;
        this.totalPages = Math.max(totalPages, 1);

        // Store the current movies for filtering
        this.currentMovies = new ArrayList<>(movies);

        // Clear previous results
        resultsPanel.removeAll();

        // Limit the number of movies displayed to prevent UI lag
        List<Movie> moviesToDisplay = movies.size() > MAX_MOVIES_TO_DISPLAY
                ? movies.subList(0, MAX_MOVIES_TO_DISPLAY)
                : movies;

        // Add MovieCard for each movie
        for (Movie movie : moviesToDisplay) {
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
        if (movies.size() > MAX_MOVIES_TO_DISPLAY) {
            setStatus("Showing " + MAX_MOVIES_TO_DISPLAY + " of " + movies.size() + " results (page " + currentPage + "). Use pagination to see more.");
        } else {
            setStatus("Found " + movies.size() + " results (page " + currentPage + ").");
        }
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
        // Listen to view model changes to update the display when movies are filtered
        if (viewModel != null) {
            viewModel.addPropertyChangeListener(evt -> {
                if (evt.getPropertyName().equals("filteredMovies") ||
                        evt.getPropertyName().equals("errorMessage")) {
                    updateFilteredMoviesDisplay();
                }
            });
        }
    }

    /**
     * Filters the currently displayed movies by the selected genre IDs.
     *
     * @param genreIds the list of genre IDs to filter by
     */
    public void filterCurrentMovies(List<Integer> genreIds) {
        if (currentMovies.isEmpty()) {
            showError("No movies to filter.");
            return;
        }

        System.out.println("Filtering " + currentMovies.size() + " movies by genres: " + genreIds);

        // Filter current movies by selected genres
        List<Movie> filteredMovies = currentMovies.stream()
                .filter(movie -> {
                    List<Integer> movieGenres = movie.getGenreIds();
                    if (movieGenres == null || movieGenres.isEmpty()) {
                        System.out.println("Movie " + movie.getTitle() + " has no genres");
                        return false;
                    }
                    // Check if movie has at least one of the selected genres
                    boolean matches = movieGenres.stream().anyMatch(genreIds::contains);
                    if (matches) {
                        System.out.println("Movie " + movie.getTitle() + " matches with genres: " + movieGenres);
                    }
                    return matches;
                })
                .collect(Collectors.toList());

        System.out.println("Filtered to " + filteredMovies.size() + " movies");

        // Display filtered results
        if (filteredMovies.isEmpty()) {
            resultsPanel.removeAll();
            resultsPanel.revalidate();
            resultsPanel.repaint();
            List<String> genreNames = GenreUtils.getGenreNames(genreIds);
            String genreText = String.join(", ", genreNames);
            setStatus("No movies found matching genres: " + genreText);
        } else {
            showResults(filteredMovies, 1, 1);
            List<String> genreNames = GenreUtils.getGenreNames(genreIds);
            String genreText = String.join(", ", genreNames);
            setStatus("Showing " + filteredMovies.size() + " of " + currentMovies.size() + " movies filtered by: " + genreText);
        }
    }

    /**
     * Updates the display with filtered movies from the FilterMoviesViewModel.
     */
    private void updateFilteredMoviesDisplay() {
        if (filterMoviesViewModel == null) {
            return;
        }

        if (filterMoviesViewModel.hasError()) {
            showError(filterMoviesViewModel.getErrorMessage());
            return;
        }

        List<Movie> filteredMovies = filterMoviesViewModel.getFilteredMovies();
        if (filteredMovies != null && !filteredMovies.isEmpty()) {
            // Display filtered movies
            showResults(filteredMovies, 1, 1);
            List<String> genreNames = filterMoviesViewModel.getSelectedGenreNames();
            String genreText = String.join(", ", genreNames);
            setStatus("Showing " + filteredMovies.size() + " movies filtered by: " + genreText);
        } else {
            // No movies found
            resultsPanel.removeAll();
            resultsPanel.revalidate();
            resultsPanel.repaint();
            setStatus("No movies found for selected genres.");
        }
    }

    public void setMovieGateway(MovieGateway movieGateway) {
        this.movieGateway = movieGateway;
        // Don't auto-load movies here - let it be triggered explicitly after login
        // This prevents lag during app initialization
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

        // Update status immediately on EDT to show loading state
        SwingUtilities.invokeLater(() -> {
            setStatus("Loading popular movies...");
        });

        // Run API call in background thread with a short delay
        // This ensures the view transition is complete and UI is responsive
        new Thread(() -> {
            try {
                // Small delay to let view transition complete smoothly
                // This prevents perceived lag during login
                Thread.sleep(200);

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
                    // Status message is already set by showResults(), no need to override it
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
        }, "MovieLoader").start();
    }
}