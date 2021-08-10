package gamecore.gamelogic.validation;

import gamecore.auxiliaryclasses.ActionTypes;
import gamecore.mathutils.Position;

import java.util.HashMap;
import java.util.Map;

public class CheckerFactory {
    private Map<ActionTypes, Checker> checkers = new HashMap<>();

    private void initialize(final Position attacker, final Position defender,
                            final ActionTypes action, final int countLineAlive) {
        checkers.put(ActionTypes.AREA_DAMAGE, new AreaDamageChecker(attacker, defender, action, countLineAlive));
        checkers.put(ActionTypes.RANGE_COMBAT, new RangeCombatChecker(attacker, defender, action, countLineAlive));
        checkers.put(ActionTypes.CLOSE_COMBAT, new CloseCombatChecker(attacker, defender, action, countLineAlive));
        checkers.put(ActionTypes.HEALING, new HealingChecker(attacker, defender, action, countLineAlive));
        checkers.put(ActionTypes.DEFENSE, new DefendChecker(attacker, defender, action, countLineAlive));
    }

    public Checker getChecker(final Position attacker, final Position defender,
                              final ActionTypes action, final int countLineAlive) {
        initialize(attacker, defender, action, countLineAlive);
        return checkers.getOrDefault(action, new NothingChecker(attacker, defender, action, countLineAlive));
    }
}
