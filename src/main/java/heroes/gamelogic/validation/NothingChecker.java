package heroes.gamelogic.validation;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.mathutils.Position;

public class NothingChecker extends Checker {

    public NothingChecker(Position attacker, Position defender, ActionTypes action, int countLineAlive) {
        super(attacker, defender, action, countLineAlive);
    }

    @Override
    public boolean check() {
        return false;
    }
}
