package heroes.gamelogic;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.boardexception.BoardExceptionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.boardfactory.CommandFactory;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.units.UnitTypes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.List;

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

    private Map<Fields, Unit[][]> getUnits;

    private boolean isArmyOneInspired;
    private boolean isArmyTwoInspired;

    public Board(Army fieldPlayerOne, Army fieldPlayerTwo) { //TODO: Validation
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

    public Board(Board board) throws BoardException, UnitException {
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

    public void inspireArmy(Unit[][] army, General general) {
        Arrays.stream(army).forEach(x -> Arrays.stream(x).forEach(u -> u.inspire(general.getInspiration())));
    }



    public void doAction(Unit attacker, List<Unit> defender, ActionTypes act) {
        CommandFactory cf = new CommandFactory();
        cf.getCommand(attacker, defender, act).execute();
        attacker.setActive(false);
    }

    public Unit[][] getArmy(Fields fields) {
        return getUnits.get(fields);
    }

    public Unit getUnitByCoordinate(Position pair) {
        try {
            Validator.checkNullPointer(pair);
            return getUnits.get(pair.F())[pair.X()][pair.Y()];
        } catch (NullPointerException exception) {
            return null;
        }
    }

    public void setCurNumRound(int curNumRound) {
        this.curNumRound = curNumRound;
    }

    public int getCurNumRound() {
        return curNumRound;
    }

    public Unit[][] getFieldPlayerOne() {
        return fieldPlayerOne.getPlayerUnits();
    }

    public Unit[][] getFieldPlayerTwo() {
        return fieldPlayerTwo.getPlayerUnits();
    }

    public General getGeneralPlayerOne() {
        return fieldPlayerOne.getGeneral();
    }

    public General getGeneralPlayerTwo() {
        return fieldPlayerTwo.getGeneral();
    }

    public void setCurrentPlayer(Fields currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Fields getCurrentPlayer() {
        return currentPlayer;
    }

    public Fields getRoundPlayer() {
        return roundPlayer;
    }

    public void setRoundPlayer(Fields roundPlayer) {
        this.roundPlayer = roundPlayer;
    }

    public boolean isArmyOneInspired() {
        return isArmyOneInspired;
    }

    public void setArmyOneInspired(boolean armyOneInspired) {
        isArmyOneInspired = armyOneInspired;
    }

    public boolean isArmyTwoInspired() {
        return isArmyTwoInspired;
    }

    public void setArmyTwoInspired(boolean armyTwoInspired) {
        isArmyTwoInspired = armyTwoInspired;
    }
}
