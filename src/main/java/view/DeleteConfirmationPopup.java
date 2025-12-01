package view;

import javax.swing.*;
import java.awt.*;

/**
 * Confirmation dialog for deleting a movie from watch history.
 */
public class DeleteConfirmationPopup extends JDialog {
    private boolean confirmed = false;

    public DeleteConfirmationPopup(JFrame parent, String movieTitle) {
        super(parent, "Confirm Delete", true);
        initializeUI(movieTitle);
    }

    private void initializeUI(String movieTitle) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 150));
        setResizable(false);

        // Message panel
        JPanel messagePanel = new JPanel();
        messagePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));

        JLabel messageLabel = new JLabel("<html>Are you sure you want to delete<br><b>" + movieTitle + "</b><br>from your watch history?</html>");
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messagePanel.add(messageLabel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton confirmButton = new JButton("Delete");
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBackground(new Color(220, 53, 69)); // Red color
        confirmButton.setOpaque(true);
        confirmButton.setContentAreaFilled(true);
        confirmButton.setBorderPainted(false);
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(e -> {
            confirmed = true;
            dispose();
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setOpaque(true);
        cancelButton.setContentAreaFilled(true);
        cancelButton.setBorderPainted(true);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);

        add(messagePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}

