package gamecore.gamelogic.validation;

import gamecore.auxiliaryclasses.ActionTypes;
import gamecore.mathutils.Position;

public abstract class Checker {
    private final Position attacker;
    private final Position defender;
    private final ActionTypes action;
    private final int countLineAlive;

    public Checker(final Position attacker, final Position defender, final ActionTypes action, final int countLineAlive) {
        this.attacker = attacker;
        this.defender = defender;
        this.action = action;
        this.countLineAlive = countLineAlive;
    }

    public Position getAttacker() {
        return attacker;
    }

    public Position getDefender() {
        return defender;
    }

    public ActionTypes getAction() {
        return action;
    }

    public int getCountLineAlive() {
        return countLineAlive;
    }

    public abstract boolean check();
}
