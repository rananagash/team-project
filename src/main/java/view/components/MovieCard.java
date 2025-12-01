package view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.net.MalformedURLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import common.GenreUtils;
import interface_adapter.MovieViewData;

/**
 * A reusable Swing UI component for displaying movie information in a card layout.
 *
 * <p>This component is part of the view layer and depends only on {@link MovieViewData}
 * and optional action buttons provided by the View that calls it.
 *
 * <p>Each MovieCard visually represents:
 * <ul>
 *     <li>The movie poster (if available)</li>
 *     <li>The title</li>
 *     <li>The release date and TMDb rating</li>
 *     <li>The genres (as raw IDs for now)</li>
 *     <li>The plot summary</li>
 *     <li>Optional contextual action buttons, (eg. "Remove", "Add to Watchlist", etc.)</li>
 * </ul>
 *
 * <p>MovieCard is designed to be used in multiple views across different use cases without modification.
 */
public class MovieCard extends JPanel {

    private static final int POSTER_WIDTH = 120;
    private static final int POSTER_HEIGHT = 180;

    /**
     * Creates a MovieCard displaying the given movie's data with no action buttons.
     *
     * @param movie the movie information to display
     */
    public MovieCard(MovieViewData movie) {
        this(movie, List.of());
    }

    /**
     * Creates a MovieCard displaying the given movie's data with optional action buttons.
     *
     * <p>The buttons are passed in by the calling view, allowing each use case to attach its own actions
     * without changing the component.
     *
     * @param movie the movie information to display
     * @param actionButtons a list of buttons to display
     */
    public MovieCard(MovieViewData movie, List<JButton> actionButtons) {
        setLayout(new BorderLayout(15, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        add(createPoster(movie.getPosterUrl()), BorderLayout.WEST);
        add(createContent(movie, actionButtons), BorderLayout.CENTER);
    }

    /**
     * Creates ard returns a JLabel containing a scaled movie poster image.
     * If no valid URL is provided, a placeholder text is shown instead.
     *
     * @param posterUrl the URL string of the movie poster
     * @return a JLabel containing either the poster image or fallback text
     */
    private JLabel createPoster(String posterUrl) {
        final JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(POSTER_WIDTH, POSTER_HEIGHT));

        if (posterUrl != null && !posterUrl.isBlank()) {
            try {
                final ImageIcon raw = new ImageIcon(new java.net.URL(posterUrl));
                final Image scaled = raw.getImage().getScaledInstance(
                        POSTER_WIDTH, POSTER_HEIGHT, Image.SCALE_SMOOTH);
                label.setIcon(new ImageIcon(scaled));
            }
            catch (MalformedURLException e) {
                label.setText("No Image");
            }
        }
        return label;
    }

    /**
     * Creates the text-content portion of the movie card: title, rating, genres, plot summary, and any buttons.
     *
     * @param movie the movie data to render
     * @param buttons optional action buttons
     * @return a JPanel containing the movie's text information
     */
    private JPanel createContent(MovieViewData movie, List<JButton> buttons) {
        final JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);

        final JLabel title = new JLabel(movie.getTitle());
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel dateRating = new JLabel("Released: " + movie.getReleaseDate()
                + "     |     Rating: " + movie.getRating());
        dateRating.setAlignmentX(Component.CENTER_ALIGNMENT);

        final String genresText = GenreUtils.getGenreNamesAsString(movie.getGenreIds());
        final JLabel genres = new JLabel(genresText);
        genres.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JTextArea plot = new JTextArea(movie.getPlot());
        plot.setLineWrap(true);
        plot.setWrapStyleWord(true);
        plot.setEditable(false);
        plot.setOpaque(false);

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(dateRating);
        card.add(Box.createVerticalStrut(8));
        card.add(genres);
        card.add(Box.createVerticalStrut(8));
        card.add(plot);

        if (!buttons.isEmpty()) {
            final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setOpaque(false);
            for (JButton b: buttons) {
                buttonPanel.add(b);
            }
            card.add(Box.createVerticalStrut(4));
            card.add(buttonPanel);
        }

        return card;
    }
}
