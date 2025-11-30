# MovieNight

### Software Design Project - CSC207 Fall 2025

## Project Summary
**MovieNight** is a Java Swing desktop application that allows users to discover movies, organize watchlists, 
track their viewing history, and rate and review movies.
It integrates the [TMDb API](https://developer.themoviedb.org/) to retrieve movie information including posters, genres, 
and summaries.

The app is structured using Clean Architecture and SOLID design principles. Core logic is seperated from the UI, allowing for easy extension and maintaining. Movie data is retrieved through a dedicated data access layer that communicates with TMDb using authenticated HTTP requests.

Users can search for movies by title, create watchlists, store their viewing history, and rate or review movies. The Swing interface presents search results, watchlist tools, and movie information in a simple and interactive layout.

---

## Current User Stories

| # | User Story                     | Description                                                                                                                                      | Lead Developer    |
|---|--------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|-------------------|
| 1 | **Create a WatchList**         | As a user, I wish to create a watchlist of movies so that I can keep track of movies to watch later.                                             | Alana Watson      |
| 2 | **Compare Watchlists**         | As a user, I wish to examine the watchlist of other users so that I can find common movies and plan group movie nights.                          | Rana Nagash       |
| 3 | **Store Watch History**        | As a user, I wish to store the viewing history of watched movies so that I can look back at what I've seen and rate or review them.              | Jiaqi Zhao        |
| 4 | **Search for Movies by Title** | As a user, I wish to search for movies by title so I can find specific movies or discover new ones.                                              | Chester Zhao      |
| 5 | **Filter Movies by Genre**     | As a user, I wish to be able to filter movies by genre, so that I can easily find movies that I'm interested in.                                 | Inba Thiyagarajan |
| 6 | **Rate and Review Movies**     | As a user, I wish to be able to rate and review movies I've watched so that others can get recommendations (and vice versa). | Oliver Bisbee     |

---

## API Information

### The Movie Database (TMDb) API
- Documentation: [https://developer.themoviedb.org/](https://developer.themoviedb.org/)
- Usage in MovieNight:
  - Search for movies by title
- Authentication:
### End Points Used
Search movies by title
GET https://api.themoviedb.org/3/search/movie
- Used in the search feature
- Returns movie summaries, poster paths, ratings, dates, and genre IDs

Get full movie details
GET https://api.themoviedb.org/3/movie/{id}
- Used to fetch complete data for a selected movie
- Returns plot, genres, popularity, rating, release date, poster path, and more

### Authentication
- The application loads an API token from a .env file using the dotenv library.
- The file must contain:
APITOKENKEY=your_token_here

### Interaction
Data Access Layer
- All TMDb interaction is handled by TMDbMovieDataAccessObject.
- It uses Java HttpClient to send requests and performs lightweight JSON parsing to extract movie fields, lists of genres, and pagination info.
---

## Screenshots

TO ADD ONCE PROJECT IS FINISHED (MVP)


## Future Enhancements
# A list of Use Cases that we did not implement, but would have liked to given more time. By adhering to CA and SOLID principles, these could be implemented with minimal work. 
- Movie detailed view (click movie poster or title from search results view to get a dedicated view of just that one movie, with more detail)
- Create new Watchlists
- Delete from Watchlists
- Create and name multiple Review lists (i.e favorites, best new gen, etc.)
- Delete movies from Review Lists
- Sorting and filtering search results
- Friend profiles and shared watchlist generation
- UI theming and accessibility improvements


