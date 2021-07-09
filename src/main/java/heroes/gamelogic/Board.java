package heroes.gamelogic;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.boardexception.BoardExceptionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.boardfactory.CommandFactory;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.Unit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    /*
     *          ---> X
     *          |
     *          |
     *          V Y
     */

    private int curNumRound;
    private Fields currentPlayer;

    private Fields roundPlayer;

    private final Army fieldPlayerOne;
    private final Army fieldPlayerTwo;

    private final Map<Fields, Unit[][]> getUnits;

    private boolean isArmyOneInspired;
    private boolean isArmyTwoInspired;

    public Board(final Army fieldPlayerOne, final Army fieldPlayerTwo) { //TODO: Validation
        curNumRound = 1;
        currentPlayer = Fields.PLAYER_ONE;
        roundPlayer = Fields.PLAYER_ONE;
        this.fieldPlayerOne = fieldPlayerOne;
        this.fieldPlayerTwo = fieldPlayerTwo;

        getUnits = new HashMap<>();

        getUnits.put(Fields.PLAYER_ONE, fieldPlayerOne.getPlayerUnits());
        getUnits.put(Fields.PLAYER_TWO, fieldPlayerTwo.getPlayerUnits());

        inspireArmy(fieldPlayerOne.getPlayerUnits(), fieldPlayerOne.getGeneral());
        isArmyOneInspired = true;
        inspireArmy(fieldPlayerTwo.getPlayerUnits(), fieldPlayerTwo.getGeneral());
        isArmyTwoInspired = true;
    }

    public Board(final Board board) throws BoardException, UnitException {
        if (board == null) {
            throw new BoardException(BoardExceptionTypes.NULL_POINTER);
        }
        currentPlayer = board.currentPlayer;
        roundPlayer = board.roundPlayer;
        curNumRound = board.curNumRound;
        fieldPlayerOne = new Army(board.fieldPlayerOne);
        fieldPlayerTwo = new Army(board.fieldPlayerTwo);
        getUnits = new HashMap<>();
        getUnits.put(Fields.PLAYER_ONE, fieldPlayerOne.getPlayerUnits());
        getUnits.put(Fields.PLAYER_TWO, fieldPlayerTwo.getPlayerUnits());
        isArmyOneInspired = board.isArmyOneInspired;
        isArmyTwoInspired = board.isArmyTwoInspired;
    }

    public void inspireArmy(final Unit[][] army, final General general) {
        Arrays.stream(army).forEach(x -> Arrays.stream(x).forEach(u -> u.inspire(general.getInspiration())));
    }

    public void doAction(final Unit attacker, final List<Unit> defender, final ActionTypes act) {
        CommandFactory cf = new CommandFactory();
        cf.getCommand(attacker, defender, act).execute();
        attacker.setActive(false);
    }

    public Unit[][] getArmy(final Fields fields) {
        return getUnits.get(fields).clone();
    }

    public Unit getUnitByCoordinate(final Position pair) {
        try {
            Validator.checkNullPointer(pair);
            return getUnits.get(pair.F())[pair.X()][pair.Y()];
        } catch (NullPointerException exception) {
            return null;
        }
    }

    public static long activeCount(final Unit[][] units) { // Количество активных юнитов
        return Arrays.stream(units).mapToLong(x -> Arrays.stream(x).filter(Unit::isActive).count()).sum();
    }

    public static long aliveLineCount(final Unit[] units) {
        return Arrays.stream(units).filter(Unit::isAlive).count();
    }

    public static long aliveCountInArmy(final Unit[][] units) {
        return Arrays.stream(units).mapToLong(x -> Arrays.stream(x).filter(Unit::isAlive).count()).sum();
    }

    public static void checkAliveLine(final Unit[][] army) {
        if (aliveLineCount(army[0]) == 0 && aliveLineCount(army[1]) != 0) {
            Unit[] temp = army[0];
            army[0] = army[1];
            army[1] = temp;
        }
    }

    public void setCurNumRound(final int curNumRound) {
        this.curNumRound = curNumRound;
    }

    public int getCurNumRound() {
        return curNumRound;
    }

    public Unit[][] getFieldPlayerOne() {
        return fieldPlayerOne.getPlayerUnits().clone();
    }

    public Unit[][] getFieldPlayerTwo() {
        return fieldPlayerTwo.getPlayerUnits().clone();
    }

    public General getGeneralPlayerOne() {
        return fieldPlayerOne.getGeneral();
    }

    public General getGeneralPlayerTwo() {
        return fieldPlayerTwo.getGeneral();
    }

    public void setCurrentPlayer(final Fields currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Fields getCurrentPlayer() {
        return currentPlayer;
    }

    public Fields getRoundPlayer() {
        return roundPlayer;
    }

    public void setRoundPlayer(final Fields roundPlayer) {
        this.roundPlayer = roundPlayer;
    }

    public boolean isArmyOneInspired() {
        return isArmyOneInspired;
    }

    public void setArmyOneInspired(final boolean armyOneInspired) {
        isArmyOneInspired = armyOneInspired;
    }

    public boolean isArmyTwoInspired() {
        return isArmyTwoInspired;
    }

    public void setArmyTwoInspired(final boolean armyTwoInspired) {
        isArmyTwoInspired = armyTwoInspired;
    }
}
