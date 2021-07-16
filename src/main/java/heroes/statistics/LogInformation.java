package heroes.statistics;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.mathutils.Position;
import heroes.units.UnitTypes;

import java.util.Objects;

public class LogInformation {
    private final Position attacker;
    private final Position defender;
    private final ActionTypes actionType;
    private final UnitTypes attackerType;
    private final int attackerHP;
    private final UnitTypes defenderType;
    private final int defenderHP;
    private final int actPower;

    public LogInformation(Position attacker, Position defender, ActionTypes actionType,
                          UnitTypes attackerType, int attackerHP, UnitTypes defenderType,
                          int defenderHP, int actPower) {
        this.attacker = attacker;
        this.defender = defender;
        this.actionType = actionType;
        this.attackerType = attackerType;
        this.attackerHP = attackerHP;
        this.defenderType = defenderType;
        this.defenderHP = defenderHP;
        this.actPower = actPower;
    }

    public Position getAttacker() {
        return new Position(attacker.X(), attacker.Y(), attacker.F());
    }

    public Position getDefender() {
        return new Position(defender.X(), defender.Y(), defender.F());
    }

    public ActionTypes getActionType() {
        return actionType;
    }

    public UnitTypes getAttackerType() {
        return attackerType;
    }

    public int getAttackerHP() {
        return attackerHP;
    }

    public UnitTypes getDefenderType() {
        return defenderType;
    }

    public int getDefenderHP() {
        return defenderHP;
    }

    public int getActPower() {
        return actPower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogInformation that = (LogInformation) o;
        return attackerHP == that.attackerHP && defenderHP == that.defenderHP && actPower == that.actPower &&
                Objects.equals(attacker, that.attacker) && Objects.equals(defender, that.defender) &&
                actionType == that.actionType && attackerType == that.attackerType &&
                defenderType == that.defenderType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attacker, defender, actionType, attackerType, attackerHP,
                defenderType, defenderHP, actPower);
    }
}
