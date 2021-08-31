package logparser.dto;

public class Result {
    private final Integer endRoundNumber;
    private final String gameResult;

    public Result(final Integer endRoundNumber, final String gameResult) {
        this.endRoundNumber = endRoundNumber;
        this.gameResult = gameResult;
    }

    public Integer getEndRoundNumber() {
        return endRoundNumber;
    }

    public String getGameResult() {
        return gameResult;
    }
}
