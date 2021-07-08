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

    private Unit[][] fieldPlayerOne;
    private Unit[][] fieldPlayerTwo;

    private Map<Fields,Unit[][]> getUnits;

    private General generalPlayerOne;
    private General generalPlayerTwo;

    private boolean isArmyOneInspired;
    private boolean isArmyTwoInspired;
    public Board(Unit[][] fieldPlayerOne, Unit[][] fieldPlayerTwo,
                            General generalPlayerOne, General generalPlayerTwo) { //TODO: Validation
        curNumRound = 1;
        currentPlayer = Fields.PLAYER_ONE;
        roundPlayer = Fields.PLAYER_ONE;
        this.fieldPlayerOne = fieldPlayerOne;
        this.fieldPlayerTwo = fieldPlayerTwo;

        getUnits = new HashMap<>();

        getUnits.put(Fields.PLAYER_ONE, fieldPlayerOne);
        getUnits.put(Fields.PLAYER_TWO, fieldPlayerTwo);

        this.generalPlayerOne = generalPlayerOne;
        this.generalPlayerTwo = generalPlayerTwo;
        inspireArmy(fieldPlayerOne, generalPlayerOne);
        isArmyOneInspired = true;
        inspireArmy(fieldPlayerTwo, generalPlayerTwo);
        isArmyTwoInspired = true;
    }

    public Board(Board board) throws BoardException, UnitException {
        if (board == null) {
            throw new BoardException(BoardExceptionTypes.NULL_POINTER);
        }
        currentPlayer = board.currentPlayer;
        roundPlayer = board.roundPlayer;
        curNumRound = board.curNumRound;
        fieldPlayerOne = copyArmy(board.fieldPlayerOne, board.generalPlayerOne);
        fieldPlayerTwo = copyArmy(board.fieldPlayerTwo, board.generalPlayerTwo);
        generalPlayerOne = new General(board.generalPlayerOne);
        generalPlayerTwo = new General(board.generalPlayerTwo);
        getUnits = new HashMap<>();
        getUnits.put(Fields.PLAYER_ONE, fieldPlayerOne);
        getUnits.put(Fields.PLAYER_TWO, fieldPlayerTwo);
        isArmyOneInspired = board.isArmyOneInspired;
        isArmyTwoInspired = board.isArmyTwoInspired;
    }

    public void inspireArmy(Unit[][] army, General general){
        Arrays.stream(army).forEach(x -> Arrays.stream(x).forEach(u -> u.inspire(general.getInspiration())));
    }

    private Unit[][] copyArmy(Unit[][] army, General general) throws BoardException, UnitException {
        Unit[][] result = new Unit[2][3];
        for (int i = 0; i < 2; i++) {
            if (army == null) {
                throw new BoardException(BoardExceptionTypes.NULL_POINTER);
            }
            for (int j = 0; j < 3; j++) {
                if (army[i][j] == null) {
                    throw new BoardException(BoardExceptionTypes.NULL_POINTER);
                }
                if(army[i][j] == general){
                    result[i][j] = new General(general);
                } else {
                    result[i][j] = new Unit(army[i][j]);
                }
            }
        }
        return result;
    }

    public void doAction(Unit attacker, List<Unit> defender, ActionTypes act) {
        CommandFactory cf = new CommandFactory();
        cf.getCommand(attacker, defender, act).execute();
    }

    public Unit[][] getArmy(Fields fields) {
        return getUnits.get(fields);
    }

    public Unit getUnitByCoordinate(Position pair) {
        try {
            Validator.checkNullPointer(pair);
            return getUnits.get(pair.F())[pair.X()][pair.Y()];
        }
        catch (NullPointerException exception) {
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
        return fieldPlayerOne;
    }

    public Unit[][] getFieldPlayerTwo() {
        return fieldPlayerTwo;
    }

    public General getGeneralPlayerOne() {
        return generalPlayerOne;
    }

    public General getGeneralPlayerTwo() {
        return generalPlayerTwo;
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
