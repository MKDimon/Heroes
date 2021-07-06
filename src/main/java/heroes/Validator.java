package heroes;

import heroes.boardexception.BoardException;
import heroes.boardexception.BoardExceptionTypes;
import heroes.mathutils.Pair;
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

    public static void checkTargetAction() {

    }

}
