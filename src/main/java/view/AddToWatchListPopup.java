package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import entity.Movie;
import entity.User;
import entity.WatchList;
import interface_adapter.add_to_watchlist.AddWatchListController;
import interface_adapter.add_to_watchlist.AddWatchListPresenter;
import interface_adapter.add_to_watchlist.AddWatchListView;

/**
 * Swing-based popup dialog for adding a movie to a user's watch list.
 *
 * <p>Implements {@link AddWatchListView}, enabling the presenter to update the UI
 *
 * <p>The popup:
 * <ul>
 *     <li>Confirms if the user wants to add the selected movie</li>
 *     <li>Allows selection of a target watch list (or auto-selects if the user has only one)</li>
 *     <li>Delegates add operations to the controller</li>
 *     <li>Updates its display when the presenter calls {@link #showResult(String)}</li>
 * </ul>
 */
public class AddToWatchListPopup extends JDialog implements AddWatchListView {

    private JLabel messageLabel;
    private JPanel buttonPanel;
    private JComboBox<WatchList> dropdown;

    private final User user;
    private final Movie movie;
    private AddWatchListController controller;
    private AddWatchListPresenter presenter;

    /**
     * Constructs the popup UI and displays it immediately.
     *
     * @param parent the parent Swing frame
     * @param user the user who is adding the movie
     * @param movie the movie to be added
     * @param controller the controller handling the add operations
     * @param presenter the presenter
     */
    public AddToWatchListPopup(JFrame parent,
                               User user,
                               Movie movie,
                               AddWatchListController controller,
                               AddWatchListPresenter presenter) {
        super(parent, "Add to Watch List", true);
        this.user = user;
        this.movie = movie;
        this.controller = controller;
        this.presenter = presenter;

        if (this.presenter != null) {
            this.presenter.setView(this);
        }

        initialState();

        setPreferredSize(new Dimension(400, 150));
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initialState() {
        final List<WatchList> watchLists = user.getWatchLists();

        messageLabel = new JLabel();
        buttonPanel = new JPanel();

        if (watchLists.size() == 1) {
            messageLabel = new JLabel("Add " + movie.getTitle() + " to "
                    + watchLists.get(0).getName() + "?");
        }
        else {
            messageLabel = new JLabel("Add " + movie.getTitle() + " to:");
            dropdown = new JComboBox<>(watchLists.toArray(new WatchList[0]));
        }
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton addButton = new JButton("Add");
        final JButton cancelButton = new JButton("Cancel");
        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);

        addButton.addActionListener(e -> handleAdd());
        cancelButton.addActionListener(e -> dispose());

        final JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalGlue());
        panel.add(messageLabel);
        if (dropdown != null) {
            panel.add(dropdown);
        }
        panel.add(Box.createVerticalGlue());

        getContentPane().add(panel);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleAdd() {
        final WatchList selected;
        if (dropdown == null) {
            selected = user.getWatchLists().get(0);
        }
        else {
            selected = (WatchList) dropdown.getSelectedItem();
        }
        controller.addMovieToWatchList(user.getUserName(), movie.getMovieId(), selected.getWatchListId());
        // don't close popup; presenter will call showResult to update
    }

    /**
     * Updates the popup with the result message from the presenter, replaces
     * the action buttons with a Close button, and repacks the dialog.
     *
     * @param message the message to display to the user
     */
    @Override
    public void showResult(String message) {
        final Container parent = messageLabel.getParent();
        messageLabel.setText(message);

        // remove dropdown if it exists
        if (dropdown != null) {
            parent.remove(dropdown);
            dropdown = null;
        }

        // replace button panel contents
        buttonPanel.removeAll();
        final JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        // refresh UI
        messageLabel.revalidate();
        messageLabel.repaint();
        buttonPanel.revalidate();
        buttonPanel.repaint();
        parent.revalidate();
        parent.repaint();
        pack();
    }

    /**
     * Setter used only for testing to inject a mock controller.
     *
     * @param controller controller to replace the initial null
     */
    public void setController(AddWatchListController controller) {
        this.controller = controller;
    }
}
