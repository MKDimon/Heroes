package heroes.statistics;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;

import java.util.List;
import java.util.Objects;

/**
 * Класс хрнаит следующую ифнормацю о одной игре.
 * Составы армий игроков
 * Историю ходов
 * Победитель, количетсво раундов
 **/

public class GameLogInformation {
    private final Army playerOneArmy;
    private final Army playerTwoArmy;
    private final List<LogInformation> logList;
    private final String winner;
    private final int countOfRounds;

    public GameLogInformation(final Army playerOneArmy, final Army playerTwoArmy,
                              final List<LogInformation> logList, final String winner, final int countOfRounds) {
        this.playerOneArmy = playerOneArmy;
        this.playerTwoArmy = playerTwoArmy;
        this.logList = logList;
        this.winner = winner;
        this.countOfRounds = countOfRounds;
    }

    public Army getPlayerOneArmy() throws BoardException, UnitException {
        return new Army(playerOneArmy);
    }

    public Army getPlayerTwoArmy() throws BoardException, UnitException {
        return new Army(playerTwoArmy);
    }

    public List<LogInformation> getLogList() {
        return List.copyOf(logList);
    }

    public String getWinner() {
        return winner;
    }

    public int getCountOfRounds() {
        return countOfRounds;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GameLogInformation that = (GameLogInformation) o;
        return countOfRounds == that.countOfRounds && Objects.equals(playerOneArmy, that.playerOneArmy)
                && Objects.equals(playerTwoArmy, that.playerTwoArmy) &&
                Objects.equals(logList, that.logList) && winner.equals(that.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerOneArmy, playerTwoArmy, logList, winner, countOfRounds);
    }
}
