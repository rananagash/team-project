package view;

import interface_adapter.record_watchhistory.RecordWatchHistoryView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Swing-based popup dialog for displaying the result of recording watch history.
 *
 * <p>Implements {@link RecordWatchHistoryView}, enabling the presenter to update the UI
 * with success or error messages.
 */
public class RecordWatchHistoryPopup extends JDialog implements RecordWatchHistoryView {

    private JLabel titleLabel;
    private JLabel messageLabel;
    private JLabel errorLabel;
    private JButton closeButton;
    private JPanel contentPanel;

    /**
     * Constructs the popup UI.
     *
     * @param parent the parent Swing frame
     */
    public RecordWatchHistoryPopup(JFrame parent) {
        super(parent, "Record Watch History", true);
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(450, 250));
        setResizable(false);

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(20, 20, 15, 20));
        headerPanel.setBackground(new Color(245, 245, 250));

        titleLabel = new JLabel("Record Watch History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);

        // Content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        contentPanel.setBackground(Color.WHITE);

        messageLabel = new JLabel();
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        errorLabel = new JLabel();
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);

        contentPanel.add(Box.createVerticalGlue());
        contentPanel.add(messageLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(errorLabel);
        contentPanel.add(Box.createVerticalGlue());

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(15, 20, 20, 20));
        buttonPanel.setBackground(new Color(245, 245, 250));

        closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.PLAIN, 14));
        closeButton.setPreferredSize(new Dimension(100, 35));
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        // Add components
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    @Override
    public void showSuccess(String userName, String movieTitle, String movieId, String watchedAt) {
        // Clear error
        errorLabel.setText("");
        errorLabel.setVisible(false);

        // Show success message
        String successMessage = String.format(
            "<html><div style='text-align: center;'>" +
            "<p style='font-size: 16px; color: #2E7D32; font-weight: bold;'>✓ Success!</p>" +
            "<p style='margin-top: 10px;'>Successfully recorded watch history:</p>" +
            "<p style='margin-top: 8px;'><b>User:</b> %s</p>" +
            "<p><b>Movie:</b> %s</p>" +
            "<p><b>Movie ID:</b> %s</p>" +
            "<p><b>Watched at:</b> %s</p>" +
            "</div></html>",
            userName, movieTitle, movieId, watchedAt
        );

        messageLabel.setText(successMessage);
        messageLabel.setForeground(new Color(46, 125, 50));
        messageLabel.setVisible(true);

        // Ensure popup is visible and centered
        pack();
        // Try to get the actual application frame as parent
        Window parentWindow = SwingUtilities.getWindowAncestor(getParent());
        if (parentWindow instanceof JFrame) {
            setLocationRelativeTo(parentWindow);
        } else {
            setLocationRelativeTo(null);
        }
        setVisible(true);
        toFront();
        requestFocus();
    }

    @Override
    public void showError(String errorMessage) {
        // Clear success message
        messageLabel.setText("");
        messageLabel.setVisible(false);

        // Show error message
        String errorText = String.format(
            "<html><div style='text-align: center;'>" +
            "<p style='font-size: 16px; color: #C62828; font-weight: bold;'>✗ Error</p>" +
            "<p style='margin-top: 10px;'>%s</p>" +
            "</div></html>",
            errorMessage
        );

        errorLabel.setText(errorText);
        errorLabel.setVisible(true);

        // Ensure popup is visible and centered
        pack();
        // Try to get the actual application frame as parent
        Window parentWindow = SwingUtilities.getWindowAncestor(getParent());
        if (parentWindow instanceof JFrame) {
            setLocationRelativeTo(parentWindow);
        } else {
            setLocationRelativeTo(null);
        }
        setVisible(true);
        toFront();
        requestFocus();
    }

    /**
     * Shows the popup centered on the parent frame.
     */
    public void display() {
        setLocationRelativeTo(getParent());
        setVisible(true);
    }
}

