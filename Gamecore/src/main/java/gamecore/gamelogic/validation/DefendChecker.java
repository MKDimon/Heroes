package gamecore.gamelogic.validation;

import gamecore.auxiliaryclasses.ActionTypes;
import gamecore.mathutils.Position;

public class DefendChecker extends Checker{
    public DefendChecker(final Position attacker, final Position defender, final ActionTypes action,
                         final int countLineAlive) {
        super(attacker, defender, action, countLineAlive);
    }

    @Override
    public boolean check() {
        return true;
    }
}
