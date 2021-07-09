package heroes.gamelogic;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.GameLogicExceptionType;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.mathutils.Position;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameLogic {
    Logger logger = LoggerFactory.getLogger(GameLogic.class);

    private Board board;
    private boolean gameBegun;

    public GameLogic() {
        gameBegun = false;
    }

    public GameLogic(Board board) throws UnitException, BoardException {
        this.board = new Board(board);
        gameBegun = true;
    }

    public boolean gameStart(Army fieldPlayerOne, Army fieldPlayerTwo) {
        try {
            if (fieldPlayerOne == fieldPlayerTwo
            || fieldPlayerOne.getPlayerUnits() == fieldPlayerTwo.getPlayerUnits()
            || fieldPlayerOne.getGeneral() == fieldPlayerTwo.getGeneral()) {
                throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
            }

            Validator.checkNullPointer(fieldPlayerOne, fieldPlayerTwo,
                    fieldPlayerOne.getGeneral(), fieldPlayerTwo.getGeneral());

            Validator.checkNullPointerInArmy(fieldPlayerOne.getPlayerUnits());
            Validator.checkNullPointerInArmy(fieldPlayerTwo.getPlayerUnits());

            board = new Board(fieldPlayerOne, fieldPlayerTwo);
            gameBegun = true;
            Arrays.stream(fieldPlayerOne.getPlayerUnits()).forEach
                    (x -> Arrays.stream(x).forEach(u -> u.inspire
                            (fieldPlayerOne.getGeneral().getInspiration())));
            Arrays.stream(fieldPlayerTwo.getPlayerUnits()).forEach
                    (x -> Arrays.stream(x).forEach(u -> u.inspire
                            (fieldPlayerTwo.getGeneral().getInspiration())));
            return true;
        } catch (NullPointerException | GameLogicException exception) {
            logger.error(" Game Start failed ",exception);
            return false;
        }
    }

    private boolean actionValidate(Position attacker, Position defender, ActionTypes act) {
        if (!gameBegun) {
            return false;
        }
        try {
            Validator.checkNullPointer(attacker, defender, act);
            Validator.checkNullPointer(attacker.X(), attacker.Y(), defender.Y(), defender.X());
            Validator.checkIndexOutOfBounds(attacker);
            Validator.checkIndexOutOfBounds(defender);

            Validator.checkCorrectPlayer(board, attacker);
            Validator.checkCorrectAction(board.getUnitByCoordinate(attacker), act);
            Validator.checkCorrectAttacker(board.getUnitByCoordinate(attacker));
            Validator.checkCorrectDefender(board.getUnitByCoordinate(defender));

            // можно через лямбду
            int countAliveAtc = 0, x = attacker.X(),
                    countAliveDef = 0, xD = defender.X();
            Unit[][] units = board.getArmy(attacker.F());
            Unit[][] unitsD = board.getArmy(defender.F());
            for (int i = 0; i < 3; i++) {
                if (units[x][i].isAlive()) {
                    countAliveAtc++;
                }
                if (unitsD[x][i].isAlive()) {
                    countAliveDef++;
                }
            }
            Validator.checkTargetAction(attacker, defender, act, countAliveAtc, countAliveDef);
        } catch (NullPointerException | BoardException exception) {
            logger.warn(exception.getMessage());
            return false;
        } catch (UnitException exception) {
            act = ActionTypes.DEFENSE;
            return true;
        }

        return true;
    }

    private List<Unit> actionGetList(final Position def, final ActionTypes act) {
        List<Unit> result = new ArrayList<>();
        if (act.isMassEffect()) {
            result.addAll(Arrays.asList((board.getArmy(def.F())[0])));
            result.addAll(Arrays.asList((board.getArmy(def.F())[1])));
        } else {
            result.add(board.getUnitByCoordinate(def));
        }
        return result;
    }

    public boolean action(final Position attacker, final Position defender, final ActionTypes act) {
        if (actionValidate(attacker, defender, act)) {
            board.doAction(board.getUnitByCoordinate(attacker), actionGetList(defender, act), act);
            gameBegun = ControlRound.checkStep(board);
            return true;

        }
        return false;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isGameBegun() {
        return gameBegun;
    }
}
