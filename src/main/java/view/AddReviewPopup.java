package view;

import interface_adapter.review_movie.ReviewMovieController;
import interface_adapter.review_movie.ReviewMovieViewModel;

import javax.swing.*;
import java.awt.*;

public class AddReviewPopup extends JDialog {

    private final ReviewMovieController controller;
    private final ReviewMovieViewModel viewModel;

    public AddReviewPopup(JFrame parent,
                          ReviewMovieController controller,
                          ReviewMovieViewModel viewModel,
                          String userId,
                          String movieId,
                          String movieTitle) {

        super(parent, "Review " + movieTitle, true);

        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout());
        setSize(350, 300);
        setLocationRelativeTo(parent);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1));

        JLabel ratingLabel = new JLabel("Rating:");
        JComboBox<Integer> ratingBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});

        JLabel commentLabel = new JLabel("Comment:");
        JTextArea commentArea = new JTextArea(5, 20);
        JScrollPane commentScroll = new JScrollPane(commentArea);

        formPanel.add(ratingLabel);
        formPanel.add(ratingBox);
        formPanel.add(commentLabel);
        formPanel.add(commentScroll);

        JButton submitButton = new JButton("Submit");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(submitButton);
        buttonPanel.add(cancelButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            int rating = (int) ratingBox.getSelectedItem();
            String comment = commentArea.getText();

            controller.submitReview(userId, movieId, rating, comment);

            dispose();
        });

        cancelButton.addActionListener(e -> dispose());

        viewModel.addPropertyChangeListener(evt -> {
            JOptionPane.showMessageDialog(this, viewModel.getMessage());
        });
    }
}