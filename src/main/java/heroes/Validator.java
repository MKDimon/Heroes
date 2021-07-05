package heroes;

import heroes.mathutils.Pair;

public class Validator {

    private Validator() {}

    public static void checkNullPointer(Object... arr) throws NullPointerException {
        for (Object item: arr) {
            if (item == null) {
                throw new NullPointerException(item.getClass().getName() + ". Exception was executed in actionValidate.");
            }
        }
    }

    public static void checkIndexOutOfBounds(Pair<Integer, Integer> pair) throws IllegalArgumentException {
        int x = pair.getX(),
            y = pair.getY();
        if (x > 1 || x < 0 || y > 2 || y < 0) {
            throw new IllegalArgumentException("?????"); //TODO: BoardException
        }
    }

}
