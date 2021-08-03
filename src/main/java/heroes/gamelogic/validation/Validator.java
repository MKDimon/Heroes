package heroes.gamelogic.validation;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.boardexception.BoardExceptionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.validation.AreaDamageChecker;
import heroes.gamelogic.validation.Checker;
import heroes.gamelogic.validation.CheckerFactory;
import heroes.mathutils.Position;
import heroes.units.Unit;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        final int x = pair.X();
        final int y = pair.Y();
        if (x > 1 || x < 0 || y > 2 || y < 0) {
            throw new BoardException(BoardExceptionTypes.INDEX_OUT_OF_BOUNDS);
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
     * @param attacker - инициализатор
     * @param defender - цель
     * @param actionType - тип действия
     * @param countAlive - со стороны атакующего
     * @throws BoardException - ошибка в доске
     */
    public static void checkTargetAction(final Position attacker, final Position defender,
                                         final ActionTypes actionType, final int countAlive)
                                         throws BoardException {
        CheckerFactory checkerFactory = new CheckerFactory();
        if (!checkerFactory.getChecker(attacker, defender, actionType, countAlive).check()) {
            throw new BoardException(BoardExceptionTypes.INCORRECT_TARGET);
        }
    }

}
