package heroes;

import heroes.mathutils.Pair;

public class Board {
    /*
     *          ---> X
     *          |
     *          |
     *          V Y
     */

    private int curNumRound;

    //TODO (@MKDimon) : int -> UNITS, String -> Action
    private int[][] fieldPlayerOne;
    private int[][] fieldPlayerTwo;

    private int generalPlayerOne;
    private int generalPlayerTwo;

    public Board(int[][] fieldPlayerOne, int[][] fieldPlayerTwo, int generalPlayerOne, int generalPlayerTwo) { //TODO: Validation
        curNumRound = 1;

        this.fieldPlayerOne = fieldPlayerOne;
        this.fieldPlayerTwo = fieldPlayerTwo;
        this.generalPlayerOne = generalPlayerOne;
        this.generalPlayerTwo = generalPlayerTwo;
    }

    private int getUnitByCoordinate(Pair<Integer, Integer> pair, boolean field) { //TODO: Validation
        if (!field) {
            return fieldPlayerOne[pair.getX()][pair.getY()];
        }
        else {
            return fieldPlayerTwo[pair.getX()][pair.getY()];
        }
    }

    private boolean actionValidate(Pair<Integer, Integer> attacker, Pair<Integer, Integer> defender, String act) { //TODO: Validation?

        // TODO: Check Exception Hierarchy
        try {
            // TODO
            Validator.checkNullPointer(attacker, defender, act);

        }
        catch (NullPointerException exception) {
            // TODO: место под логер
            return false;
        }

        return true;
    }

    private void doAction(Pair<Integer, Integer> attacker, Pair<Integer, Integer> defender, String act) { //TODO: exception?

    }

    public boolean action(Pair<Integer, Integer> attacker, Pair<Integer, Integer> defender, boolean field, String act) {
        if (actionValidate(attacker, defender, act)) {
            doAction(attacker, defender, act);
            return true;
        }
        return false;
    }

}
