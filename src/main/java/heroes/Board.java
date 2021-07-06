package heroes;

import heroes.boardexception.BoardException;
import heroes.boardfactory.CommandFactory;
import heroes.mathutils.Pair;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.Unit;
import heroes.units.auxiliaryclasses.ActionTypes;

public class Board {
    /*
     *          ---> X
     *          |
     *          |
     *          V Y
     */

    private int curNumRound;

    //TODO (@MKDimon) : int -> UNITS, String -> Action
    private Unit[][] fieldPlayerOne;
    private Unit[][] fieldPlayerTwo;

    private General generalPlayerOne;
    private General generalPlayerTwo;

    public Board(Unit[][] fieldPlayerOne, Unit[][] fieldPlayerTwo, General generalPlayerOne, General generalPlayerTwo) { //TODO: Validation
        curNumRound = 1;

        this.fieldPlayerOne = fieldPlayerOne;
        this.fieldPlayerTwo = fieldPlayerTwo;
        this.generalPlayerOne = generalPlayerOne;
        this.generalPlayerTwo = generalPlayerTwo;
    }

    private Unit getUnitByCoordinate(Position pair) { //TODO: Validation
        if (pair.F() == 1) {
            return fieldPlayerOne[pair.X()][pair.Y()];
        }
        else {
            return fieldPlayerTwo[pair.X()][pair.Y()];
        }
    }

    private boolean actionValidate(Position attacker, Position defender, ActionTypes act) { //TODO: Validation?

        // TODO: Check Exception Hierarchy
        try {
            // TODO
            Validator.checkNullPointer(attacker, defender, act);
            Validator.checkNullPointer(attacker.X(), attacker.Y(), defender.Y(), defender.X());
            Validator.checkIndexOutOfBounds(attacker);
            Validator.checkIndexOutOfBounds(defender);

        }
        catch (NullPointerException | BoardException exception) {
            // TODO: место под логер
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    private void doAction(Position attacker, Position defender, ActionTypes act) { //TODO: exception?
        Unit att = getUnitByCoordinate(attacker);
        Unit def = getUnitByCoordinate(defender);
        CommandFactory cf = new CommandFactory();
        cf.getCommand(att, def, act).execute();
    }

    public boolean action(Position attacker, Position defender, ActionTypes act) {
        if (actionValidate(attacker, defender, act)) {
            doAction(attacker, defender, act);
            return true;
        }
        return false;
    }

}
