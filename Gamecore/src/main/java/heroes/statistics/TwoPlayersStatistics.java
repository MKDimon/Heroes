package heroes.statistics;

public class TwoPlayersStatistics {
    private final String playerOne;
    private final String playerTwo;
    private int playerOneWins;
    private int playerTwoWins;
    private int draws;

    public TwoPlayersStatistics(final String playerOne, final String playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public void incPlayerOneWins() {
        playerOneWins++;
    }

    public void incPlayerTwoWins() {
        playerTwoWins++;
    }

    public void incDraws() {
        draws++;
    }

    public String getPlayerOne() {
        return playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public int getPlayerOneWins() {
        return playerOneWins;
    }

    public int getPlayerTwoWins() {
        return playerTwoWins;
    }

    public int getDraws() {
        return draws;
    }

    public boolean hasPlayer(final String player) {
        return isPlayerOne(player) || isPlayerTwo(player);
    }

    public boolean isPlayerOne(final String player) {
        return player.equals(playerOne);
    }

    public boolean isPlayerTwo(final String player) {
        return player.equals(playerTwo);
    }

    public void changeStatistics(final BotsLogInformation log) {
        if (hasPlayer(log.getBotOne()) && hasPlayer(log.getBotTwo())) {

            if (log.getWinner().equals(playerOne)) {
                playerOneWins++;
            }
            if (log.getWinner().equals(playerTwo)) {
                playerTwoWins++;
            }
            if (log.getWinner().equals("DRAW")) {
                draws++;
            }
        }
    }

}
