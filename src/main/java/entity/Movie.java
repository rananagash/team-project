package entity;

import java.util.List;

public class Movie {

    private String movieID;
    private String title;
    private String plot;
    private List<Integer> genres;
    private String releaseDate;
    private double rating;
    private String posterURL;

    public Movie(String movieID, String title, String plot, List<Integer> genres, String releaseDate, double rating, String posterURL) {
        this.movieID = movieID;
        this.title = title;
        this.plot = plot;
        this.genres = genres;
        this.rating = rating;
        this.releaseDate = releaseDate;
        this.posterURL = posterURL;
    }

    public String getMovieIDId() {
        return movieID;
    }
    public String getTitle() {
        return title;
    }
    public String getPlot() {
        return plot;
    }
    public String getGenre() {
        return genres;
    }
    public double getRating() {
        return rating;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public String getPosterURL() {
        return posterURL;
    }
}
