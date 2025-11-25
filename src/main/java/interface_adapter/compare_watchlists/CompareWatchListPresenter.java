package interface_adapter.compare_watchlists;

import use_case.compare_watchlists.CompareWatchListOutputBoundary;
import use_case.compare_watchlists.CompareWatchListResponseModel;

public class CompareWatchListPresenter implements CompareWatchListOutputBoundary {

    private final CompareWatchListViewModel compareWatchListViewModel;

    public CompareWatchListPresenter(CompareWatchListViewModel compareWatchListViewModel) {
        this.compareWatchListViewModel = compareWatchListViewModel;
    }

    @Override
    public void prepareSuccessView(CompareWatchListResponseModel responseModel) {
        CompareWatchListState state = compareWatchListViewModel.getState();
        state.setCommonMovies(responseModel.getCommonMovies());
        state.setBaseOnlyMovies(responseModel.getBaseOnlyMovies());
        state.setTargetOnlyMovies(responseModel.getTargetOnlyMovies());
        state.setError(null);
        compareWatchListViewModel.setState(state);
        compareWatchListViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        CompareWatchListState state = compareWatchListViewModel.getState();
        state.setError(errorMessage);
        compareWatchListViewModel.setState(state);
        compareWatchListViewModel.firePropertyChange();
    }
}
