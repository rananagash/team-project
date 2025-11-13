package view;

import interface_adapter.search_movie.SearchMovieController;

import javax.swing.JPanel;

public class SearchResultsView extends JPanel {

    private SearchMovieController controller;

    public void setController(SearchMovieController controller) {
        this.controller = controller;
    }

    /*
     * TODO(Chester Zhao): Build the Swing components here and bind controller.search(query).
     */
}
