package heroes.gamelogic;

import heroes.units.Unit;

import java.util.Arrays;

public class ControlRound {

    public void checkRound(Board board) {
        // Количество активных юнитов
        long active = Arrays.stream(board.getFieldPlayerOne()).mapToLong(x -> Arrays.stream(x).filter(Unit::isActive).count()).sum();

    }

    private void newRound(Board board) {

    }

    private boolean endRounds(Board board) {
        return false;
    }
}
