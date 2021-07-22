package heroes.gamelogic.validation;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.mathutils.Position;

public class DefendChecker extends Checker{
    public DefendChecker(Position attacker, Position defender, ActionTypes action, int countLineAlive) {
        super(attacker, defender, action, countLineAlive);
    }

    @Override
    public boolean check() {
        return true;
    }
}
