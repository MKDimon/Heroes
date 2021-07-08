package heroes.gamelogic;

import heroes.SelfPlay;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.GameLogicExceptionType;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.mathutils.Position;
import heroes.units.General;
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
            Arrays.stream(fieldPlayerOne).forEach(x -> Arrays.stream(x).forEach(u -> u.inspire(generalPlayerOne.getInspiration())));
            Arrays.stream(fieldPlayerTwo).forEach(x -> Arrays.stream(x).forEach(u -> u.inspire(generalPlayerTwo.getInspiration())));
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

    private List<Unit> actionGetList(Position def, ActionTypes act) {
        List<Unit> result = new ArrayList<>();
        Unit[][] army = board.getArmy(def.F());
        if (act.isMassEffect()) {
            result.addAll(Arrays.asList((board.getArmy(def.F())[0])));
            result.addAll(Arrays.asList((board.getArmy(def.F())[1])));
        } else {
            result.add(board.getUnitByCoordinate(def));
        }
        return result;
    }

    public boolean action(Position attacker, Position defender, ActionTypes act) {
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
