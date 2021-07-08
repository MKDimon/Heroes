package heroes.gamelogic;

import heroes.units.Unit;

import java.util.Arrays;

public class ControlRound {

    private static final int maxRound = 10;

    private static long activeCount(Unit[][] units) { // Количество активных юнитов
        return Arrays.stream(units).mapToLong(x -> Arrays.stream(x).filter(Unit::isActive).count()).sum();
    }

    private static long aliveLineCount(Unit[] units) {
        return Arrays.stream(units).filter(Unit::isAlive).count();
    }

    private static long aliveCountInArmy(Unit[][] units) {
        return Arrays.stream(units).mapToLong(x -> Arrays.stream(x).filter(Unit::isAlive).count()).sum();
    }

    private static void checkAliveLine(Unit[][] army) {
        if (aliveLineCount(army[0]) == 0 && aliveLineCount(army[1]) != 0) {
            Unit[] temp = army[0];
            army[0] = army[1];
            army[1] = temp;
        }
    }

    public static void nextPlayer(Board board) {
        if (board.getCurrentPlayer() == Fields.PLAYER_TWO && activeCount(board.getFieldPlayerOne()) != 0) {
            board.setCurrentPlayer(Fields.PLAYER_ONE);
        } else if (activeCount(board.getFieldPlayerTwo()) != 0) {
            board.setCurrentPlayer(Fields.PLAYER_TWO);
        }
    }

    public static boolean checkStep(Board board) {
        // Количество активных юнитов
        if(!board.getGeneralPlayerOne().isAlive() && board.isArmyOneInspired()){
            Arrays.stream(board.getFieldPlayerOne()).forEach(x -> Arrays.stream(x).forEach(u -> u.deinspire(board.getGeneralPlayerOne().getDeinspiration())));
            board.setArmyOneInspired(false);
        }
        if(!board.getGeneralPlayerTwo().isAlive() && board.isArmyTwoInspired()){
            Arrays.stream(board.getFieldPlayerTwo()).forEach(x -> Arrays.stream(x).forEach(u -> u.deinspire(board.getGeneralPlayerTwo().getDeinspiration())));
            board.setArmyTwoInspired(false);
        }

        checkAliveLine(board.getFieldPlayerOne());
        checkAliveLine(board.getFieldPlayerTwo());

        long active = activeCount(board.getFieldPlayerOne());
        active += activeCount(board.getFieldPlayerTwo());
        ControlRound.nextPlayer(board);
        if (active == 0) {
            return newRound(board);
        }
        return true;
    }

    private static boolean newRound(Board board) {
        if (board.getCurNumRound() == maxRound
                || aliveCountInArmy(board.getFieldPlayerOne()) +
                   aliveCountInArmy(board.getFieldPlayerTwo()) == 0) {
            //TODO: место под логи
            return false;
        }

        // Смена игрока начинающего раунд
        board.setCurrentPlayer( (board.getRoundPlayer() == Fields.PLAYER_ONE)? Fields.PLAYER_TWO: Fields.PLAYER_TWO );
        board.setRoundPlayer( board.getCurrentPlayer() );

        Arrays.stream(board.getFieldPlayerOne()).forEach(x -> Arrays.stream(x).forEach(t -> t.setActive(true)));
        Arrays.stream(board.getFieldPlayerTwo()).forEach(x -> Arrays.stream(x).forEach(t -> t.setActive(true)));
        Arrays.stream(board.getFieldPlayerOne()).forEach(x -> Arrays.stream(x).forEach(t -> t.setBonusArmor(0)));
        Arrays.stream(board.getFieldPlayerTwo()).forEach(x -> Arrays.stream(x).forEach(t -> t.setBonusArmor(0)));
        board.setCurNumRound(board.getCurNumRound() + 1);

        return true;
    }
}
