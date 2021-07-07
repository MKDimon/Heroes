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

import java.util.HashMap;
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

    //TODO (@MKDimon) : int -> UNITS, String -> Action
    private Unit[][] fieldPlayerOne;
    private Unit[][] fieldPlayerTwo;

    private Map<Fields,Unit[][]> getUnits;

    private General generalPlayerOne;
    private General generalPlayerTwo;

    public Board(Unit[][] fieldPlayerOne, Unit[][] fieldPlayerTwo,
                            General generalPlayerOne, General generalPlayerTwo) { //TODO: Validation
        curNumRound = 1;
        currentPlayer = Fields.PLAYER_ONE;

        this.fieldPlayerOne = fieldPlayerOne;
        this.fieldPlayerTwo = fieldPlayerTwo;

        getUnits = new HashMap<>();

        getUnits.put(Fields.PLAYER_ONE, fieldPlayerOne);
        getUnits.put(Fields.PLAYER_TWO, fieldPlayerTwo);

        this.generalPlayerOne = generalPlayerOne;
        this.generalPlayerTwo = generalPlayerTwo;
    }

    public Board(Board board) throws BoardException, UnitException {
        if (board == null) {
            throw new BoardException(BoardExceptionTypes.NULL_POINTER);
        }
        currentPlayer = board.currentPlayer;
        curNumRound = board.curNumRound;
        fieldPlayerOne = copyArmy(board.fieldPlayerOne);
        fieldPlayerTwo = copyArmy(board.fieldPlayerTwo);
        generalPlayerOne = new General(board.generalPlayerOne);
        generalPlayerTwo = new General(board.generalPlayerTwo);
    }

    private Unit[][] copyArmy(Unit[][] army) throws BoardException, UnitException {
        Unit[][] result = new Unit[2][3];
        for (int i = 0; i < 2; i++) {
            if (army == null) {
                throw new BoardException(BoardExceptionTypes.NULL_POINTER);
            }
            for (int j = 0; j < 3; j++) {
                if (army[i][j] == null) {
                    throw new BoardException(BoardExceptionTypes.NULL_POINTER);
                }
                result[i][j] = new Unit(army[i][j]);
            }
        }
        return result;
    }

    public void doAction(Position attacker, Position defender, ActionTypes act) { //TODO: exception?
        Unit att = getUnitByCoordinate(attacker);
        Unit def = getUnitByCoordinate(defender);
        CommandFactory cf = new CommandFactory();
        cf.getCommand(att, def, act).execute();
    }

    public Unit[][] getArmy(Fields fields) {
        return getUnits.get(fields);
    }

    public Unit getUnitByCoordinate(Position pair) { //TODO: Validation
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

}
