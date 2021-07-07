package heroes.gamelogic;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.GameLogicExceptionType;
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

    public GameLogic(Board board) throws UnitException, BoardException { // TODO: конструктор копирования доски и Validate на доску
        this.board = new Board(board);
        gameBegun = true;
    }

    public boolean gameStart(Unit[][] fieldPlayerOne, Unit[][] fieldPlayerTwo,
                     General generalPlayerOne, General generalPlayerTwo) {
        try {
            if (fieldPlayerOne == fieldPlayerTwo ||
                    generalPlayerOne == generalPlayerTwo) {
                throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
            }

            Validator.checkNullPointer(fieldPlayerOne, fieldPlayerTwo, generalPlayerOne, generalPlayerTwo);

            Validator.checkNullPointerInArmy(fieldPlayerOne);
            Validator.checkNullPointerInArmy(fieldPlayerTwo);

            board = new Board(fieldPlayerOne, fieldPlayerTwo, generalPlayerOne, generalPlayerTwo);
            gameBegun = true;
            return true;
        }
        catch (NullPointerException | GameLogicException exception) {
            //TODO: место под логи
            System.out.println(exception.getMessage()); // TODO: в логер
            return false;
        }
    }

    private boolean actionValidate(Position attacker, Position defender, ActionTypes act) {
        if (!gameBegun) { return false; }
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
            // можно через лямбду
            int countAlive = 0, x = attacker.X();
            Unit[][] units = board.getArmy(attacker.F());
            for (int i = 0; i < 3; i++) {
                if (units[x][i].isAlive()) { countAlive++; }
            }
            Validator.checkTargetAction(attacker, defender, act, countAlive);
        }
        catch (NullPointerException | BoardException exception) {
            // TODO: место под логер
            System.out.println(exception.getMessage()); // TODO: в логер
            return false;
        }

        return true;
    }

    public boolean action(Position attacker, Position defender, ActionTypes act) {
        if (actionValidate(attacker, defender, act)) {
            board.doAction(attacker, defender, act);
            gameBegun = ControlRound.checkStep(board);
            return true;
        }
        return false;
    }

    public Board getBoard() {
        return board;
    }
}
