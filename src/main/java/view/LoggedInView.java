package view;

import common.GenreUtils;
import entity.Movie;
import entity.User;
import interface_adapter.add_to_watchlist.AddWatchListController;
import interface_adapter.add_to_watchlist.AddWatchListPresenter;
import interface_adapter.filter_movies.FilterMoviesController;
import interface_adapter.filter_movies.FilterMoviesViewModel;
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
import interface_adapter.review_movie.ReviewMovieController;
import interface_adapter.review_movie.ReviewMovieViewModel;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    private AddWatchListPresenter addWatchListPresenter;
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
    private JButton filterMoviesBtn;

    // Middle Panel (testing only)
    //TODO: remove before final version
    private JPanel middlePanel;
    private JButton addToWatchListBtn;
    private JButton addToHistoryBtn;

    // UI Components - Search panel
    private JTextField queryField;
    private JButton searchButton;
    //    private JList<String> resultsList;
//    private DefaultListModel<String> listModel;
    private JPanel movieResultsPanel; // will replace JList
    private JScrollPane movieScrollPane;

    // UI Components - Results panel (using MovieCard components)
    // Note: movieResultsPanel is used for displaying movie results

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

    // Store original movies before filtering (to restore when clearing filter)
    private List<Movie> originalMovies = new ArrayList<>();


    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(8, 8));

        // ===== Top panel with account buttons =====
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        // Make top panel scrollable if needed
        topPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 50));

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

        topPanel.add(new JSeparator(SwingConstants.VERTICAL));

        filterMoviesBtn = new JButton("Filter Movies");
        filterMoviesBtn.setVisible(true);
        filterMoviesBtn.setPreferredSize(new Dimension(120, 30)); // Make button more visible

        filterMoviesBtn.addActionListener(e -> {
            // Check if we have movies to filter
            if (currentMovies == null || currentMovies.isEmpty()) {
                showError("No movies to filter. Please search for movies or load popular movies first.");
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

        middlePanel.add(addToWatchListBtn);
        middlePanel.add(addToHistoryBtn);

        addToWatchListBtn.addActionListener(e -> {
            final LoggedInState currentState = loggedInViewModel.getState();
            if (currentState == null) return;

            //Dummy user - real button should get current logged in user
            User user = new User("TestUser", "pw");

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
                    addWatchListController,
                    addWatchListPresenter
            );
        });

        addToHistoryBtn.addActionListener(e -> {
            final LoggedInState currentState = loggedInViewModel.getState();
            if (currentState == null) return;

            if (recordWatchHistoryController != null) {
                // Use current logged in user's username
                String username = currentState.getUsername();

                // Test movie ID (using a real TMDb movie ID for testing)
                String testMovieId = "299534"; // Avengers: Endgame

                // Record the movie to watch history
                // watchedAt is null, so it will use current time
                recordWatchHistoryController.recordMovie(username, testMovieId);
            }
        });

//        this.add(topPanel, BorderLayout.NORTH);


        // ===== Search bar =====
        JPanel searchPanel = new JPanel(new BorderLayout(4, 4));
        queryField = new JTextField();
        searchButton = new JButton("Search");
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

        // ===== Result list =====
        movieResultsPanel = new JPanel();
        movieResultsPanel.setLayout(new BoxLayout(movieResultsPanel, BoxLayout.Y_AXIS));
        movieResultsPanel.setBackground(new Color(15, 23, 42));

        movieScrollPane = new JScrollPane(movieResultsPanel);
        movieScrollPane.setBorder(BorderFactory.createEmptyBorder());
        movieScrollPane.getViewport().setBackground(new Color(15, 23, 42));
        movieScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        this.add(movieScrollPane, BorderLayout.CENTER);

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
     * Display search results with pagination.
     */
    public void showResults(List<entity.Movie> movies, int currentPage, int totalPages) {
        this.currentPage = currentPage;
        this.totalPages = Math.max(totalPages, 1);

        // Store the current movies for filtering - always update this when showing results
        if (movies != null && !movies.isEmpty()) {
            this.currentMovies = new ArrayList<>(movies);
            // Also store as original movies (before any filtering)
            this.originalMovies = new ArrayList<>(movies);
        } else {
            this.currentMovies = new ArrayList<>();
            this.originalMovies = new ArrayList<>();
        }

        // clear result
        movieResultsPanel.removeAll();

        if (movies.isEmpty()) {
            // nothing found
            JLabel noResultsLabel = new JLabel("No movies found for your search", SwingConstants.CENTER);
            noResultsLabel.setForeground(new Color(209, 213, 219));
            noResultsLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            movieResultsPanel.add(noResultsLabel);
        } else {
            // cards display
            for (entity.Movie movie : movies) {
                JPanel movieCard = createMovieCard(movie);
                movieCard.setAlignmentX(Component.LEFT_ALIGNMENT);
                movieResultsPanel.add(movieCard);
                movieResultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        // UI
        movieResultsPanel.add(Box.createVerticalGlue());

        // refresh
        movieResultsPanel.revalidate();
        movieResultsPanel.repaint();

        // page button
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

                if (state.getSearchResults() != null && !state.getSearchResults().isEmpty()) {
                    showResults(state.getSearchResults(), state.getCurrentPage(), state.getTotalPages());
                } else if (state.getSearchError() != null) {
                    showError(state.getSearchError());
                }
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

    public void setAddWatchListPresenter(AddWatchListPresenter addWatchListPresenter) {
        this.addWatchListPresenter = addWatchListPresenter;
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
     * @return true if movies were found, false otherwise
     *
     * @param genreIds the list of genre IDs to filter by
     */
    public boolean filterCurrentMoviesAndCheck(List<Integer> genreIds) {
        return filterCurrentMovies(genreIds);
    }

    /**
     * Filters the currently displayed movies by the selected genre IDs.
     *
     * @param genreIds the list of genre IDs to filter by
     * @return true if movies were found, false otherwise
     */
    public boolean filterCurrentMovies(List<Integer> genreIds) {

        // Filter current movies by selected genres
        List<Movie> filteredMovies = currentMovies.stream()
                .filter(movie -> {
                    List<Integer> movieGenres = movie.getGenreIds();
                    if (movieGenres == null || movieGenres.isEmpty()) {
                        return false;
                    }
                    // Check if movie has at least one of the selected genres
                    return movieGenres.stream().anyMatch(genreIds::contains);
                })
                .collect(Collectors.toList());

        // Display filtered results
        if (filteredMovies.isEmpty()) {
            // Clear the results panel and show "no movies found" message
            movieResultsPanel.removeAll();
            List<String> genreNames = GenreUtils.getGenreNames(genreIds);
            String genreText = String.join(", ", genreNames);
            JLabel noResultsLabel = new JLabel("No movies found matching genres: " + genreText, SwingConstants.CENTER);
            noResultsLabel.setForeground(new Color(209, 213, 219));
            noResultsLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            movieResultsPanel.add(noResultsLabel);
            movieResultsPanel.add(Box.createVerticalGlue());
            movieResultsPanel.revalidate();
            movieResultsPanel.repaint();
            setStatus("No movies found matching genres: " + genreText);
        } else {
            showResults(filteredMovies, 1, 1);
            List<String> genreNames = GenreUtils.getGenreNames(genreIds);
            String genreText = String.join(", ", genreNames);
            setStatus("Showing " + filteredMovies.size() + " of " + originalMovies.size() + " movies filtered by: " + genreText);
            return true; // Results found
        }
        return false;
    }

    /**
     * Clears the current filter and restores the original movies.
     */
    public void clearFilter() {
        if (originalMovies.isEmpty()) {
            showError("No original movies to restore.");
            return;
        }
        showResults(originalMovies, currentPage, totalPages);
        setStatus("Filter cleared. Showing all " + originalMovies.size() + " movies.");
    }
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
            // No movies found - clear the results panel and show message
            movieResultsPanel.removeAll();
            JLabel noResultsLabel = new JLabel("No movies found for selected genres.", SwingConstants.CENTER);
            noResultsLabel.setForeground(new Color(209, 213, 219));
            noResultsLabel.setFont(new Font("Helvetica", Font.PLAIN, 16));
            noResultsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            movieResultsPanel.add(noResultsLabel);
            movieResultsPanel.add(Box.createVerticalGlue());
            movieResultsPanel.revalidate();
            movieResultsPanel.repaint();
            setStatus("No movies found for selected genres.");
        }
    }

    private JPanel createMovieCard(Movie movie) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(new Color(255, 255, 255, 13));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 26)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(900, 150));

        // Poster
        JLabel posterLabel = new JLabel("Loading...", SwingConstants.CENTER);
        posterLabel.setPreferredSize(new Dimension(80, 120));
        posterLabel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 38)));
        posterLabel.setForeground(Color.WHITE);
        posterLabel.setFont(new Font("Helvetica", Font.PLAIN, 8));
        posterLabel.setOpaque(true);
        posterLabel.setBackground(new Color(30, 41, 59));

        // load Poster
        loadPosterImage(movie.getPosterUrl(), posterLabel);

        // info
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(movie.getTitle());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 18));

        // detail
        Integer year = movie.getReleaseYear() != null ? movie.getReleaseYear() : 0000;
        String rating = String.format("%.1f/10", movie.getRating());
        String detailsText = "<html>Year: " + year + "<br>Rating: " + rating + "</html>";

        JLabel detailsLabel = new JLabel(detailsText);
        detailsLabel.setForeground(new Color(209, 213, 219));
        detailsLabel.setFont(new Font("Helvetica", Font.PLAIN, 14));

        infoPanel.add(titleLabel);
        infoPanel.add(detailsLabel);

        // 2 buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(260, 40));

        // list
        JButton addToWatchlistBtn = new JButton("Add to Watchlist");
        addToWatchlistBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        addToWatchlistBtn.setBackground(new Color(37, 99, 235));
        addToWatchlistBtn.setForeground(Color.BLACK);
        addToWatchlistBtn.setFocusPainted(false);

        // history
        JButton addToHistoryBtn = new JButton("Add to History");
        addToHistoryBtn.setFont(new Font("Helvetica", Font.BOLD, 12));
        addToHistoryBtn.setBackground(new Color(147, 51, 234));
        addToHistoryBtn.setForeground(Color.BLACK);
        addToHistoryBtn.setFocusPainted(false);

        JButton reviewButton = new JButton("Review");
        reviewButton.setFont(new Font("Helvetica", Font.BOLD, 12));
        reviewButton.setBackground(new Color(59, 130, 246)); // same blue
        reviewButton.setForeground(Color.BLACK);
        reviewButton.setFocusPainted(false);

        buttonPanel.add(addToWatchlistBtn);
        buttonPanel.add(addToHistoryBtn);

        // layout
        card.add(posterLabel, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);

        // listners
        addToWatchlistBtn.addActionListener(e -> {
            if (addWatchListController != null) {
                // may need AddWatchListController
                // only update the look for now
                addToWatchlistBtn.setText("In Watchlist");
                addToWatchlistBtn.setBackground(new Color(22, 163, 74));
                addToWatchlistBtn.setEnabled(false);

                // TODO: need to call watch list Controller
            }
        });

        // review listener

        reviewButton.addActionListener(e -> {

            LoggedInState currentState = loggedInViewModel.getState();
            if (currentState == null) return;

            String userId = currentState.getUsername();
            String movieId = movie.getMovieId() + "";
            String movieTitle = movie.getTitle();

            // Check if user already reviewed the movie
            if (reviewMovieViewModel.getExistingReview(userId, movieId) != null) {
                int choice = JOptionPane.showConfirmDialog(
                        null,
                        "You already reviewed this. Edit review?",
                        "Review exists",
                        JOptionPane.YES_NO_OPTION
                );

                if (choice != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            AddReviewPopup popup = new AddReviewPopup(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    reviewMovieController,
                    reviewMovieViewModel,
                    userId,
                    movieId,
                    movieTitle
            );
            popup.setVisible(true);
        });

        buttonPanel.add(reviewButton);


        addToHistoryBtn.addActionListener(e -> {
            if (recordWatchHistoryController != null) {
                final LoggedInState currentState = loggedInViewModel.getState();
                if (currentState != null) {
                    String username = currentState.getUsername();
                    String movieId = movie.getMovieId();

                    // Record the movie to watch history
                    recordWatchHistoryController.recordMovie(username, movieId);

                    // Update button appearance
                    addToHistoryBtn.setText("Watched");
                    addToHistoryBtn.setBackground(new Color(22, 163, 74));
                    addToHistoryBtn.setEnabled(false);
                }
            }
        });

        return card;
    }

    private void loadPosterImage(String posterUrl, JLabel posterLabel) {
        if (posterUrl == null || posterUrl.isEmpty() || posterUrl.equals("null")) {
            showNoImagePlaceholder(posterLabel);
            return;
        }

        try {
            SwingWorker<ImageIcon, Void> worker = new SwingWorker<ImageIcon, Void>() {
                @Override
                protected ImageIcon doInBackground() throws Exception {
                    URL imageUrl = new URL(posterUrl);
                    return new ImageIcon(imageUrl);
                }

                @Override
                protected void done() {
                    try {
                        ImageIcon imageIcon = get();
                        Image image = imageIcon.getImage().getScaledInstance(80, 120, Image.SCALE_SMOOTH);
                        posterLabel.setIcon(new ImageIcon(image));
                        posterLabel.setText("");
                    } catch (Exception e) {
                        showNoImagePlaceholder(posterLabel);
                    }
                }
            };
            worker.execute();

        } catch (Exception e) {
            showNoImagePlaceholder(posterLabel);
        }
    }

    private void showNoImagePlaceholder(JLabel posterLabel) {
        posterLabel.setIcon(null);
        posterLabel.setText("No Image");
        posterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        posterLabel.setVerticalAlignment(SwingConstants.CENTER);
        posterLabel.setForeground(new Color(156, 163, 175));
        posterLabel.setFont(new Font("Helvetica", Font.PLAIN, 10));
    }
}
