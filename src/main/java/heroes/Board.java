package heroes;

import heroes.boardexception.BoardException;
import heroes.mathutils.Pair;
import heroes.mathutils.Position;
import heroes.units.Unit;
import heroes.units.auxiliaryclasses.ActionTypes;

import java.util.Map;

public class Board {
    /*
     *          ---> X
     *          |
     *          |
     *          V Y
     */

    private int curNumRound;

    private Unit[][] fieldPlayerOne;
    private Unit[][] fieldPlayerTwo;

    private Map<Fields,Unit[][]> getUnits;

    private Unit generalPlayerOne;
    private Unit generalPlayerTwo;

    public Board(Unit[][] fieldPlayerOne, Unit[][] fieldPlayerTwo, Unit generalPlayerOne, Unit generalPlayerTwo) { //TODO: Validation
        curNumRound = 1;

        this.fieldPlayerOne = fieldPlayerOne;
        this.fieldPlayerTwo = fieldPlayerTwo;

        getUnits.put(Fields.PLAYER_ONE, fieldPlayerOne);
        getUnits.put(Fields.PLAYER_TWO, fieldPlayerTwo);

        this.generalPlayerOne = generalPlayerOne;
        this.generalPlayerTwo = generalPlayerTwo;
    }

    private Unit getUnitByCoordinate(Position pair) { //TODO: Validation
        return getUnits.get(pair.F())[pair.X()][pair.Y()];
    }

    private boolean actionValidate(Position attacker, Position defender, ActionTypes act) {
        try {
            Validator.checkNullPointer(attacker, defender, act);
            Validator.checkNullPointer(attacker.X(), attacker.Y(), defender.Y(), defender.X());
            Validator.checkIndexOutOfBounds(attacker);
            Validator.checkIndexOutOfBounds(defender);

            Validator.checkCorrectAction(getUnitByCoordinate(attacker), act);

            // TODO: упростить код
            int countAlive = 0, x = attacker.X();
            Unit[][] units = getUnits.get(attacker.F());
            for (int i = 0; i < 3; i++) {
                if (units[x][i].isAlive()) { countAlive++; }
            }
            Validator.checkTargetAction(attacker, defender, act, countAlive);
        }
        catch (NullPointerException | BoardException exception) {
            // TODO: место под логер
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    private void doAction(Position attacker, Position defender, ActionTypes act) { //TODO: exception?

    }

    public boolean action(Position attacker, Position defender, ActionTypes act) {
        if (actionValidate(attacker, defender, act)) {
            doAction(attacker, defender, act);
            return true;
        }
        return false;
    }

}
