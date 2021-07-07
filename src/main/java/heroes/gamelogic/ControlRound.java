package heroes.gamelogic;

import heroes.units.Unit;

import java.util.Arrays;

public class ControlRound {

    private static final int maxRound = 10;

    private static long activeCount(Unit[][] units) { // Количество активных юнитов
        return Arrays.stream(units).mapToLong(x -> Arrays.stream(x).filter(Unit::isActive).count()).sum();
    }

    public static void nextPlayer(Board board) {
        if (board.getCurrentPlayer() == Fields.PLAYER_TWO && activeCount(board.getFieldPlayerOne()) != 0) {
            board.setCurrentPlayer(Fields.PLAYER_ONE);
        }
        else if (activeCount(board.getFieldPlayerTwo()) != 0) {
            board.setCurrentPlayer(Fields.PLAYER_TWO);
        }
    }

    public static boolean checkRound(Board board) {
        // Количество активных юнитов
        long active = activeCount(board.getFieldPlayerOne());
        active += activeCount(board.getFieldPlayerTwo());
        ControlRound.nextPlayer(board);
        if (active == 0) {
            return newRound(board);
        }
        return true;
    }

    private static boolean newRound(Board board) {
        if (board.getCurNumRound() == maxRound) { return false; }
        Arrays.stream(board.getFieldPlayerOne()).forEach(x -> Arrays.stream(x).forEach(t -> t.setActive(true)));
        Arrays.stream(board.getFieldPlayerTwo()).forEach(x -> Arrays.stream(x).forEach(t -> t.setActive(true)));
        board.setCurNumRound(board.getCurNumRound() + 1);
        // TODO: флаги активности

        return true;
    }
}
