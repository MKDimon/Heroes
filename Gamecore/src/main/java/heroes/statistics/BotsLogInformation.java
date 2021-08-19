package heroes.statistics;

public class BotsLogInformation {
    private final String botOne;
    private final String botTwo;
    private final String winner;


    public BotsLogInformation(final String botOne, final String botTwo, final String winner) {
        this.botOne = botOne;
        this.botTwo = botTwo;
        this.winner = winner;
    }

    public String getBotOne() {
        return botOne;
    }

    public String getBotTwo() {
        return botTwo;
    }

    public String getWinner() {
        return winner;
    }
}
