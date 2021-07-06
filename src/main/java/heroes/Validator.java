package heroes;

import heroes.boardexception.BoardException;
import heroes.boardexception.BoardExceptionTypes;
import heroes.mathutils.Position;
import heroes.units.Unit;
import heroes.units.auxiliaryclasses.ActionTypes;

public class Validator {

    private Validator() {}

    public static void checkNullPointer(Object... arr) throws NullPointerException {
        for (Object item: arr) {
            if (item == null) {
                throw new NullPointerException(item.getClass().getName() + ". Exception was executed in actionValidate.");
            }
        }
    }

    public static void checkIndexOutOfBounds(Position pair) throws BoardException {
        int x = pair.X(),
            y = pair.Y();
        if (x > 1 || x < 0 || y > 2 || y < 0) {
            throw new BoardException(BoardExceptionTypes.INDEX_OUT_OF_BOUNDS); //TODO: BoardException
        }
    }

    public static void checkCorrectAction(Unit unit, ActionTypes actionType) throws BoardException {
        if (unit.getActionType() != actionType && actionType != ActionTypes.DEFENSE) {
            throw new BoardException(BoardExceptionTypes.ACTION_INCORRECT);
        }
    }

    public static void checkTargetAction(Position attacker, Position defender, ActionTypes actionType, int countAlive) throws BoardException {
        if (actionType == ActionTypes.HEALING && attacker.F() != defender.F()) {
            throw new BoardException(BoardExceptionTypes.INCORRECT_TARGET);
        }
        if (actionType == ActionTypes.CLOSE_COMBAT &&
            !((attacker.F() != defender.F() && attacker.X() == 0 && defender.X() == 0) &&
            (Math.abs(attacker.Y() - defender.Y()) < 2 || countAlive == 1))) {

            throw new BoardException(BoardExceptionTypes.INCORRECT_TARGET);
        }
        if (actionType == ActionTypes.AREA_DAMAGE || actionType == ActionTypes.RANGE_COMBAT) {
            if (attacker.F() == defender.F()) {
                throw new BoardException(BoardExceptionTypes.INCORRECT_TARGET);
            }
        }


    }

}
