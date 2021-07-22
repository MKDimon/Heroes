package heroes.gamelogic.validation;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.mathutils.Position;

public class CloseCombatChecker extends Checker {
    public CloseCombatChecker(final Position attacker, final Position defender,
                              final ActionTypes action, final int countLineAlive) {
        super(attacker, defender, action, countLineAlive);
    }

    @Override
    public boolean check() {
        return ((getAttacker().F() != getDefender().F() && getAttacker().X() == 0 && getDefender().X() == 0) &&
                (Math.abs(getAttacker().Y() - getDefender().Y()) < 2 || getCountLineAlive() == 1));
    }
}
