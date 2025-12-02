package use_case.view_watchlists;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ViewWatchListRequestModelTest {

    @Test
    void constructorWithTwoArgsStoreValues() {
        ViewWatchListsRequestModel request = new ViewWatchListsRequestModel("alice", 3);

        assertEquals("alice", request.getUsername());
        assertEquals(3, request.getSelectedIndex());
    }

    @Test
    void constructorWithOneArgDefaultsIndexToZero() {
        ViewWatchListsRequestModel request =
                new ViewWatchListsRequestModel("bob");

        assertEquals("bob", request.getUsername());
        assertEquals(0, request.getSelectedIndex());
    }
}
