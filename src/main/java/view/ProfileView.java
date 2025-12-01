package view;

import interface_adapter.view_profile.ViewProfileController;
import interface_adapter.view_profile.ViewProfileState;
import interface_adapter.view_profile.ViewProfileViewModel;
import interface_adapter.view_watchlists.ViewWatchListsController;
import interface_adapter.view_watchhistory.ViewWatchHistoryController;
import interface_adapter.ViewManagerModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for displaying user profiles with enhanced UI including circular avatar.
 */
public class ProfileView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "view profile";
    private final ViewProfileViewModel viewProfileViewModel;
    private ViewProfileController viewProfileController;
    private ViewWatchListsController viewWatchListsController;
    private ViewWatchHistoryController viewWatchHistoryController;
    private ViewManagerModel viewManagerModel;

    private final JLabel usernameLabel = new JLabel();
    private final JLabel watchlistCountLabel = new JLabel();
    private final JLabel reviewCountLabel = new JLabel();
    private final JLabel watchedMoviesLabel = new JLabel();
    private final JLabel errorLabel = new JLabel();
    private final JPanel avatarPanel = new JPanel();

    private JButton viewWatchListsButton;
    private JButton viewWatchHistoryButton;
    private JButton backButton;
    private String currentUsername;

    public ProfileView(ViewProfileViewModel viewProfileViewModel) {
        this.viewProfileViewModel = viewProfileViewModel;
        viewProfileViewModel.addPropertyChangeListener(this);

        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(new Color(245, 245, 245));

        // Top panel with back button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 245, 245));
        backButton = new JButton("← Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backButton.addActionListener(this);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        // Center panel with profile content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Avatar circle
        setupAvatarPanel();
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(avatarPanel);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Username
        usernameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        usernameLabel.setForeground(new Color(50, 50, 50));
        centerPanel.add(usernameLabel);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Stats section
        JPanel statsPanel = createStatsPanel();
        statsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(statsPanel);

        centerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));
        buttonsPanel.setBackground(new Color(245, 245, 245));
        buttonsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        viewWatchListsButton = new JButton("View Watchlists");
        viewWatchListsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewWatchListsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewWatchListsButton.setPreferredSize(new Dimension(200, 40));
        viewWatchListsButton.setMaximumSize(new Dimension(200, 40));
        viewWatchListsButton.addActionListener(this);
        styleButton(viewWatchListsButton);

        viewWatchHistoryButton = new JButton("View Watch History");
        viewWatchHistoryButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        viewWatchHistoryButton.setFont(new Font("Arial", Font.PLAIN, 14));
        viewWatchHistoryButton.setPreferredSize(new Dimension(200, 40));
        viewWatchHistoryButton.setMaximumSize(new Dimension(200, 40));
        viewWatchHistoryButton.addActionListener(e -> {
            if (viewWatchHistoryController != null && currentUsername != null) {
                viewWatchHistoryController.loadHistory(currentUsername);
            }
        });
        styleButton(viewWatchHistoryButton);

        buttonsPanel.add(viewWatchListsButton);
        buttonsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        buttonsPanel.add(viewWatchHistoryButton);

        centerPanel.add(buttonsPanel);

        // Error label
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setForeground(new Color(220, 20, 60));
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(errorLabel);

        // Add scrolling capability
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBackground(new Color(245, 245, 245));

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void setupAvatarPanel() {
        avatarPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        avatarPanel.setBackground(new Color(245, 245, 245));
        avatarPanel.setPreferredSize(new Dimension(150, 150));
        avatarPanel.setMaximumSize(new Dimension(150, 150));

        JLabel avatarLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw circular background
                g2d.setColor(new Color(100, 150, 200));
                g2d.fillOval(0, 0, 150, 150);

                // Draw circle border
                g2d.setColor(new Color(50, 100, 150));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(0, 0, 149, 149);

                // Draw initials placeholder
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 50));
                String initial = "U";
                if (currentUsername != null && !currentUsername.isEmpty()) {
                    initial = String.valueOf(currentUsername.charAt(0)).toUpperCase();
                }
                FontMetrics fm = g2d.getFontMetrics();
                int x = (150 - fm.stringWidth(initial)) / 2;
                int y = ((150 - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(initial, x, y);
            }
        };
        avatarLabel.setPreferredSize(new Dimension(150, 150));
        avatarPanel.add(avatarLabel);
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(3, 1, 0, 15));
        statsPanel.setBackground(new Color(245, 245, 245));
        statsPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Watchlist count
        watchlistCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        watchlistCountLabel.setForeground(new Color(80, 80, 80));
        statsPanel.add(watchlistCountLabel);

        // Review count
        reviewCountLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        reviewCountLabel.setForeground(new Color(80, 80, 80));
        statsPanel.add(reviewCountLabel);

        // Watched movies count
        watchedMoviesLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        watchedMoviesLabel.setForeground(new Color(80, 80, 80));
        statsPanel.add(watchedMoviesLabel);

        return statsPanel;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 110, 160));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
    }

    public void setViewProfileController(ViewProfileController controller) {
        this.viewProfileController = controller;
    }

    public void setViewWatchListsController(ViewWatchListsController controller) {
        this.viewWatchListsController = controller;
    }

    public void setViewWatchHistoryController(ViewWatchHistoryController controller) {
        this.viewWatchHistoryController = controller;
    }

    public void setViewManagerModel(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Loads profile for the specified username.
     */
    public void loadProfile(String username) {
        if (viewProfileController != null) {
            viewProfileController.execute(username);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ViewProfileState state = (ViewProfileState) evt.getNewValue();

        currentUsername = state.getUser().getUserName();

        if (state.getError() != null) {
            errorLabel.setText(state.getError());
            usernameLabel.setText("");
            watchlistCountLabel.setText("");
            reviewCountLabel.setText("");
            watchedMoviesLabel.setText("");
        } else if (state.getUser() != null && state.getProfileStats() != null) {
            usernameLabel.setText(currentUsername);
            watchlistCountLabel.setText("📋 Watchlists: " + state.getProfileStats().getWatchlistCount());
            reviewCountLabel.setText("✍️ Reviews: " + state.getProfileStats().getReviewCount());
            watchedMoviesLabel.setText("🎬 Watched Movies: " + state.getProfileStats().getWatchedMoviesCount());
            errorLabel.setText("");
            avatarPanel.repaint();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewWatchListsButton) {
            if (viewWatchListsController != null && currentUsername != null) {
                viewWatchListsController.execute(currentUsername);
            }
        } else if (e.getSource() == backButton) {
            if (viewManagerModel != null) {
                viewManagerModel.setState("logged in");
                viewManagerModel.firePropertyChange();
            }
        }
    }

    public String getViewName() {
        return viewName;
    }
}