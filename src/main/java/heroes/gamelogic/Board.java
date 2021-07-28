package heroes.gamelogic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.boardexception.BoardExceptionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.boardfactory.CommandFactory;
import heroes.gamelogic.validation.Validator;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.Unit;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Board {
    /*
     *          ---> X
     *          |
     *          |
     *          V Y
     */
    @JsonProperty
    private int curNumRound;
    @JsonProperty
    private Fields currentPlayer;
    @JsonProperty
    private Fields roundPlayer;
    @JsonProperty
    private final Army fieldPlayerOne;
    @JsonProperty
    private final Army fieldPlayerTwo;
    @JsonIgnore
    private final Map<Fields, Unit[][]> getUnits;
    @JsonProperty
    private boolean isArmyOneInspired;
    @JsonProperty
    private boolean isArmyTwoInspired;
    @JsonProperty
    private GameStatus status = GameStatus.GAME_PROCESS;

    @JsonCreator
    public Board(@JsonProperty("curNumRound") final int curNumRound,
                 @JsonProperty("currentPlayer") final Fields currentPlayer,
                 @JsonProperty("roundPlayer") final Fields roundPlayer,
                 @JsonProperty("fieldPlayerOne")final Army fieldPlayerOne,
                 @JsonProperty("fieldPlayerTwo")final Army fieldPlayerTwo,
                 @JsonProperty("status") final  GameStatus status,
                 @JsonProperty("isArmyOneInspired") final boolean isArmyOneInspired,
                 @JsonProperty("isArmyTwoInspired") final boolean isArmyTwoInspired) {
        this.curNumRound = curNumRound;
        this.currentPlayer = currentPlayer;
        this.roundPlayer = roundPlayer;
        this.fieldPlayerOne = fieldPlayerOne;
        this.fieldPlayerTwo = fieldPlayerTwo;
        this.isArmyOneInspired = isArmyOneInspired;
        this.isArmyTwoInspired = isArmyTwoInspired;
        this.status = status;
        getUnits = new HashMap<>();
        if (fieldPlayerOne != null) {
            getUnits.put(Fields.PLAYER_ONE, fieldPlayerOne.getPlayerUnits());
        }
        if (fieldPlayerTwo != null) {
            getUnits.put(Fields.PLAYER_TWO, fieldPlayerTwo.getPlayerUnits());
        }
    }

    public Board(final Army fieldPlayerOne, final Army fieldPlayerTwo) throws UnitException { //TODO: Validation
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
        fieldPlayerOne = (board.fieldPlayerOne != null)? new Army(board.fieldPlayerOne): null;
        fieldPlayerTwo = (board.fieldPlayerTwo != null)? new Army(board.fieldPlayerTwo): null;
        getUnits = new HashMap<>();
        status = board.status;
        getUnits.put(Fields.PLAYER_ONE, (fieldPlayerOne != null)? fieldPlayerOne.getPlayerUnits(): null);
        getUnits.put(Fields.PLAYER_TWO, (fieldPlayerTwo != null)? fieldPlayerTwo.getPlayerUnits(): null);
        isArmyOneInspired = board.isArmyOneInspired;
        isArmyTwoInspired = board.isArmyTwoInspired;
    }

    /**
     * Вспомогательный конструктор для промежуточной отрисовки и просмотра чужой армии
     */
    public Board(final Army army, final Fields field) throws UnitException, BoardException {
        if (army == null) {
            throw new BoardException(BoardExceptionTypes.NULL_POINTER);
        }
        curNumRound = 0;
        currentPlayer = Fields.PLAYER_TWO;
        getUnits = new HashMap<>();
        if (field == Fields.PLAYER_ONE) {
            fieldPlayerOne = new Army(army);
            fieldPlayerTwo = null;
            getUnits.put(Fields.PLAYER_ONE, fieldPlayerOne.getPlayerUnits());
            getUnits.put(Fields.PLAYER_TWO, null);
        }
        else {
            fieldPlayerOne = null;
            fieldPlayerTwo = new Army(army);
            getUnits.put(Fields.PLAYER_ONE, null);
            getUnits.put(Fields.PLAYER_TWO, fieldPlayerTwo.getPlayerUnits());
        }
    }

    /**
     * Воодушевление на армию
     *
     * @param army - армия содержащая генерала
     * @param general - генерал армии, содержащий воодушевление
     */
    private void inspireArmy(final Unit[][] army, final General general) {
        Arrays.stream(army).forEach(x -> Arrays.stream(x).forEach(u -> u.inspire(general.inspirationArmorBonus,
                general.inspirationDamageBonus, general.inspirationAccuracyBonus)));
    }

    public void deinspireArmy(final Unit[][] army){
        Arrays.stream(army).forEach(x -> Arrays.stream(x).forEach(Unit::deinspire));
    }

    /**
     * Выполняется действие через фабрику
     *
     * @param attacker - инициатор
     * @param defender - цель
     * @param act      - действие
     */
    public void doAction(final Unit attacker, final List<Unit> defender, final ActionTypes act) {
        final CommandFactory cf = new CommandFactory();
        cf.getCommand(attacker, defender, act).execute();
        attacker.setActive(false);
    }

    public Unit[][] getArmy(final Fields fields) {
        return (getUnits.get(fields) != null)? getUnits.get(fields).clone(): null;
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

    public void checkAliveLine(final Fields fields) {
        final Unit[][] army = getUnits.get(fields);
        if (aliveLineCount(army[0]) == 0 && aliveLineCount(army[1]) != 0) {
            final Unit[] temp = army[0];
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

    @JsonIgnore
    public Unit[][] getFieldPlayerOne() {
        return new Unit[][]{fieldPlayerOne.getPlayerUnits()[0].clone(),
                fieldPlayerOne.getPlayerUnits()[1].clone() };
    }

    @JsonIgnore
    public Unit[][] getFieldPlayerTwo() {
        return new Unit[][]{fieldPlayerTwo.getPlayerUnits()[0].clone(),
                fieldPlayerTwo.getPlayerUnits()[1].clone() };
    }

    @JsonIgnore
    public General getGeneralPlayerOne() throws UnitException {
        return fieldPlayerOne.getGeneral();
    }

    @JsonIgnore
    public General getGeneralPlayerTwo() throws UnitException {
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

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(final GameStatus status) {
        this.status = status;
    }

    public List<Position> getActiveUnitsPositions(final Fields fields){
        final Unit[][] army = getArmy(fields);
        final List<Position> result = new ArrayList<>(6);
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 3; j++){
                if(army[i][j].isActive()){
                    result.add(new Position(i, j, fields));
                }
            }
        }
        return result;
    }

    public List<Position> getAliveUnitsPositions(final Fields fields){
        final Unit[][] army = getArmy(fields);
        final List<Position> result = new ArrayList<>(6);
        for (int i = 0; i < 2; i++){
            for (int j = 0; j < 3; j++){
                if(army[i][j].isAlive()){
                    result.add(new Position(i, j, fields));
                }
            }
        }
        return result;
    }
}
