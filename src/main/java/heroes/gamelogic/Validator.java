package heroes.gamelogic;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.boardexception.BoardExceptionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.auxiliaryclasses.unitexception.UnitExceptionTypes;
import heroes.mathutils.Position;
import heroes.units.Unit;

import java.util.Arrays;
import java.util.Objects;

public class Validator {

    private Validator() {
    }

    public static void checkNullPointer(final Object... arr) throws NullPointerException {
        for (Object item : arr) {
            if (item == null) {
                throw new NullPointerException(BoardExceptionTypes.NULL_POINTER.getErrorType());
            }
        }
    }

    public static void checkNullPointerInArmy(final Object[][] obj) {
        if (Arrays.stream(obj).anyMatch(x -> Arrays.stream(x).anyMatch(Objects::isNull))) {
            throw new NullPointerException(BoardExceptionTypes.NULL_UNIT_IN_ARMY.getErrorType());
        }
    }

    public static void checkIndexOutOfBounds(final Position pair) throws BoardException {
        int x = pair.X(),
                y = pair.Y();
        if (x > 1 || x < 0 || y > 2 || y < 0) {
            throw new BoardException(BoardExceptionTypes.INDEX_OUT_OF_BOUNDS); //TODO: BoardException
        }
    }

    public static void checkCorrectAction(final Unit unit, final ActionTypes actionType) throws BoardException {
        if (unit.getActionType() != actionType && actionType != ActionTypes.DEFENSE) {
            throw new BoardException(BoardExceptionTypes.ACTION_INCORRECT);
        }
    }

    public static void checkCorrectDefender(final Unit unit) throws BoardException {
        if (!unit.isAlive()) {
            throw new BoardException(BoardExceptionTypes.UNIT_IS_DEAD);
        }
    }

    public static void checkCorrectAttacker(final Unit unit) throws BoardException {
        if (!unit.isActive() || !unit.isAlive()) {
            throw new BoardException(BoardExceptionTypes.UNIT_IS_NOT_ACTIVE);
        }
    }

    public static void checkCorrectPlayer(final Board board, final Position attacker) throws BoardException {
        if (attacker.F() != board.getCurrentPlayer()) {
            throw new BoardException(BoardExceptionTypes.INCORRECT_PLAYER);
        }
    }

    /**
     * Проверяет на корректность выбора цели
     * Если юнит не может ходить - возвращает ошибку UNIT_CANT_STEP
     *
     * @param attacker
     * @param defender
     * @param actionType
     * @param countAlive - со стороны атакующего
     * @param countAliveDef - со стороны цели
     * @throws BoardException
     * @throws UnitException
     */
    public static void checkTargetAction(final Position attacker, final Position defender, final ActionTypes actionType,
                                         int countAlive, int countAliveDef) throws BoardException, UnitException {
        if (actionType == ActionTypes.HEALING && attacker.F() != defender.F()) {
            throw new BoardException(BoardExceptionTypes.INCORRECT_TARGET);
        }

        if (actionType == ActionTypes.CLOSE_COMBAT) {
            if (attacker.X() == 1) {
                throw new UnitException(UnitExceptionTypes.UNIT_CANT_STEP);
            }
            if (!((attacker.F() != defender.F() && attacker.X() == 0 && defender.X() == 0) &&
                    (Math.abs(attacker.Y() - defender.Y()) < 2 || countAlive == 1))) {
                if (countAliveDef > 1) {
                    throw new BoardException(BoardExceptionTypes.INCORRECT_TARGET);
                }
                else {
                    throw new UnitException(UnitExceptionTypes.UNIT_CANT_STEP);
                }
            }
        }

        if (actionType == ActionTypes.AREA_DAMAGE || actionType == ActionTypes.RANGE_COMBAT) {
            if (attacker.F() == defender.F()) {
                throw new BoardException(BoardExceptionTypes.INCORRECT_TARGET);
            }
        }
    }

}
