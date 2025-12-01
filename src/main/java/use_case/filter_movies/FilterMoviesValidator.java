package use_case.filter_movies;

import java.util.List;

/**
 * Validator for Filter Movies use case requests.
 * <p>
 * Responsible for validating request models according to business rules.
 * This class follows the Single Responsibility Principle by isolating
 * validation logic from business logic.
 */
public class FilterMoviesValidator {

    /**
     * Validates the filter movies request model.
     *
     * @param requestModel the request model to validate
     * @return validation result containing error message if invalid, null if valid
     */
    public String validate(FilterMoviesRequestModel requestModel) {
        if (requestModel == null) {
            return "Request cannot be null.";
        }

        List<Integer> genreIds = requestModel.getGenreIds();
        if (genreIds == null || genreIds.isEmpty()) {
            return "Select at least one genre.";
        }

        List<?> moviesToFilter = requestModel.getMoviesToFilter();
        if (moviesToFilter == null || moviesToFilter.isEmpty()) {
            return "No movies to filter.";
        }

        return null; // Validation passed
    }
}

