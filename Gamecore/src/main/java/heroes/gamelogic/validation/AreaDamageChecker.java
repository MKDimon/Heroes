package heroes.gamelogic.validation;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.mathutils.Position;

public class AreaDamageChecker extends Checker {
    public AreaDamageChecker(final Position attacker, final Position defender,
                             final ActionTypes action, final int countLineAlive) {
        super(attacker, defender, action, countLineAlive);
    }

    @Override
    public boolean check() {
        return getDefender().F() != getAttacker().F();
    }
}
