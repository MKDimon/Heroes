package heroes.gamelogic.validation;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.mathutils.Position;

public abstract class Checker {
    private final Position attacker;
    private final Position defender;
    private final ActionTypes action;
    private final int countLineAlive;

    public Checker(Position attacker, Position defender, ActionTypes action, int countLineAlive) {
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
