package use_case.compare_watchlists;

public class CompareWatchListRequestModel {

    private final String baseUserName;
    private final String targetUserName;

    public CompareWatchListRequestModel(String baseUserName, String targetUserName) {
        this.baseUserName = baseUserName;
        this.targetUserName = targetUserName;
    }

    public String getBaseUserName() {
        return baseUserName;
    }

    public String getTargetUserName() {
        return targetUserName;
    }
}

