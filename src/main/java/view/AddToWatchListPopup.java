package view;

import entity.Movie;
import entity.User;
import entity.WatchList;
import interface_adapter.add_to_watchlist.AddWatchListController;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddToWatchListPopup extends JDialog {

    private JLabel messageLabel;
    private JPanel buttonPanel;
    private JComboBox<WatchList> dropdown;

    private final User user;
    private final Movie movie;
    private AddWatchListController controller; // if setController method not necessary, this might be okay to be final

    public AddToWatchListPopup(JFrame parent,
                               User user,
                               Movie movie,
                               AddWatchListController controller) {
        super(parent, "Add to Watch List", true);
        this.user = user;
        this.movie = movie;
        this.controller = controller;

        initialState();

        //TODO(Alana): some appearance finessing, centering content in the popup, etc
        setPreferredSize(new Dimension(400, 200));
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initialState() {
        List<WatchList> watchLists = user.getWatchLists();

        messageLabel = new JLabel();
        buttonPanel = new JPanel();

        if (watchLists.size() == 1) {
            messageLabel = new JLabel("Add \"" + movie.getTitle() + "\" to \""
                    + watchLists.get(0).getName() + "\"?");
        } else {
            messageLabel = new JLabel("Add \"" + movie.getTitle() + "\" to:");
            dropdown = new JComboBox<>(watchLists.toArray(new WatchList[0]));
        }

        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        addButton.addActionListener(e -> handleAdd());
        cancelButton.addActionListener(e -> dispose());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(messageLabel);
        if (dropdown != null) {
            panel.add(dropdown);
        }
        panel.add(buttonPanel);

        getContentPane().add(panel);
    }

    private void handleAdd() {
        WatchList selected = (dropdown == null) ? user.getWatchLists().get(0)
                : (WatchList) dropdown.getSelectedItem();
        controller.addMovieToWatchList(user, movie, selected);
        // don't close popup; presenter will call showResult to update
    }

    // called by presenter to refresh with outcome message and close button
    public void showResult(String message) {
        messageLabel.setText(message);

        buttonPanel.removeAll();

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        buttonPanel.revalidate();
        buttonPanel.repaint();
        pack();
    }

    // this method is to enable a test I wrote, to get around the loop of dependencies.
    // Might not be necessary in the final version
    public void setController(AddWatchListController controller) {
        this.controller = controller;
    }
}
