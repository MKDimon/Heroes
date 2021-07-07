package heroes.gamelogic;

import heroes.units.Unit;

import java.util.Arrays;

public class ControlRound {

    private static final int maxRound = 10;

    private static long activeCount(Unit[][] units) { // Количество активных юнитов
        return Arrays.stream(units).mapToLong(x -> Arrays.stream(x).filter(Unit::isActive).count()).sum();
    }

    private static long aliveCount(Unit[] units) {
        return Arrays.stream(units).filter(Unit::isAlive).count();
    }

    private static void checkAliveLine(Unit[][] army) {
        if (aliveCount(army[0]) == 0 && aliveCount(army[1]) != 0) {
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
        if(!board.getGeneralPlayerOne().isAlive()){
            Arrays.stream(board.getFieldPlayerOne()).forEach(x -> Arrays.stream(x).forEach(u -> u.deinspire(board.getGeneralPlayerOne().getDeinspiration())));
        }
        if(!board.getGeneralPlayerTwo().isAlive()){
            Arrays.stream(board.getFieldPlayerTwo()).forEach(x -> Arrays.stream(x).forEach(u -> u.deinspire(board.getGeneralPlayerTwo().getDeinspiration())));
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
        if (board.getCurNumRound() == maxRound) {
            return false;
        }
        Arrays.stream(board.getFieldPlayerOne()).forEach(x -> Arrays.stream(x).forEach(t -> t.setActive(true)));
        Arrays.stream(board.getFieldPlayerTwo()).forEach(x -> Arrays.stream(x).forEach(t -> t.setActive(true)));
        Arrays.stream(board.getFieldPlayerOne()).forEach(x -> Arrays.stream(x).forEach(t -> t.setBonusArmor(0)));
        Arrays.stream(board.getFieldPlayerTwo()).forEach(x -> Arrays.stream(x).forEach(t -> t.setBonusArmor(0)));
        board.setCurNumRound(board.getCurNumRound() + 1);
        // TODO: флаги активности

        return true;
    }
}