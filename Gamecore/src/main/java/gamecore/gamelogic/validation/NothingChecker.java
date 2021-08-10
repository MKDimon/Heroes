package gamecore.gamelogic.validation;

import gamecore.auxiliaryclasses.ActionTypes;
import gamecore.mathutils.Position;

public class NothingChecker extends Checker {

    public NothingChecker(final Position attacker, final Position defender, final ActionTypes action,
                          final int countLineAlive) {
        super(attacker, defender, action, countLineAlive);
    }

    @Override
    public boolean check() {
        return false;
    }
}
