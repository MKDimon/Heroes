package heroes.gamelogic;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.Unit;

public class GameLogic {

    private Board board;
    private boolean gameBegun;

    public GameLogic() {
        gameBegun = false;
    }

    public GameLogic(Board board) throws UnitException { // TODO: конструктор копирования доски и Validate на доску
        this.board = board;
        gameBegun = true;
    }

    public void gameStart(Unit[][] fieldPlayerOne, Unit[][] fieldPlayerTwo,
                          General generalPlayerOne, General generalPlayerTwo) { //TODO: VALIDATOR
        try {
            Validator.checkNullPointer(fieldPlayerOne, fieldPlayerTwo, generalPlayerOne, generalPlayerTwo);
            board = new Board(fieldPlayerOne, fieldPlayerTwo, generalPlayerOne, generalPlayerTwo);
            gameBegun = true;
        } catch (NullPointerException exception) {
            //TODO: место под логи
        }
    }

    private boolean actionValidate(Position attacker, Position defender, ActionTypes act) {
        if (!gameBegun) {
            return false;
        }
        try {
            // TODO
            Validator.checkNullPointer(attacker, defender, act);
            Validator.checkNullPointer(attacker.X(), attacker.Y(), defender.Y(), defender.X());
            Validator.checkIndexOutOfBounds(attacker);
            Validator.checkIndexOutOfBounds(defender);

            Validator.checkCorrectPlayer(board, attacker);
            Validator.checkCorrectAction(board.getUnitByCoordinate(attacker), act);
            Validator.checkCorrectUnit(board.getUnitByCoordinate(attacker));
            Validator.checkCorrectUnit(board.getUnitByCoordinate(defender));

            // TODO: упростить код
            int countAlive = 0, x = attacker.X();
            Unit[][] units = board.getArmy(attacker.F());
            for (int i = 0; i < 3; i++) {
                if (units[x][i].isAlive()) {
                    countAlive++;
                }
            }
            Validator.checkTargetAction(attacker, defender, act, countAlive);
        } catch (NullPointerException | BoardException exception) {
            // TODO: место под логер

            return false;
        }

        return true;
    }

    public boolean action(Position attacker, Position defender, ActionTypes act) {
        if (actionValidate(attacker, defender, act)) {
            board.doAction(attacker, defender, act);
            gameBegun = ControlRound.checkRound(board);
            return true;
        }
        return false;
    }

    public Board getBoard() {
        return board;
    }
}
