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

import java.util.*;

public class Board {
    /*
     *          ---> X
     *          |
     *          |
     *          V Y
     */

    private int curNumRound;
    private Fields currentPlayer;

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
        inspireArmy(fieldPlayerOne, generalPlayerOne);
        inspireArmy(fieldPlayerTwo, generalPlayerTwo);
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
        generalPlayerTwo = new General(board.generalPlayerTwo); //Возможно, нужно добавить баф командира
        inspireArmy(fieldPlayerOne, generalPlayerOne);
        inspireArmy(fieldPlayerTwo, generalPlayerTwo);
        getUnits = new HashMap<>();
        getUnits.put(Fields.PLAYER_ONE, fieldPlayerOne);
        getUnits.put(Fields.PLAYER_TWO, fieldPlayerTwo);
    }

    public void inspireArmy(Unit[][] army, General general){
        Arrays.stream(army).forEach(x -> Arrays.stream(x).forEach(u -> u.inspire(general.getInspiration())));
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

    public void doAction(Unit attacker, List<Unit> defender, ActionTypes act) { //TODO: exception?
        CommandFactory cf = new CommandFactory();
        cf.getCommand(attacker, defender, act).execute();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return curNumRound == board.curNumRound && currentPlayer == board.currentPlayer && Arrays.equals(fieldPlayerOne, board.fieldPlayerOne) && Arrays.equals(fieldPlayerTwo, board.fieldPlayerTwo) && Objects.equals(getUnits, board.getUnits) && Objects.equals(generalPlayerOne, board.generalPlayerOne) && Objects.equals(generalPlayerTwo, board.generalPlayerTwo);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(curNumRound, currentPlayer, getUnits, generalPlayerOne, generalPlayerTwo);
        result = 31 * result + Arrays.hashCode(fieldPlayerOne);
        result = 31 * result + Arrays.hashCode(fieldPlayerTwo);
        return result;
    }
}
