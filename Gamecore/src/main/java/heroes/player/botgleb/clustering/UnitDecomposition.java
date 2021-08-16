package heroes.player.botgleb.clustering;

public class UnitDecomposition {
    private final double HP;
    private final double armor;
    private final double areaDamage;
    private final double rangeDamage;
    private final double closeCombatDamage;
    private final double healing;

    public UnitDecomposition(final double hp, final double armor, final double areaDamage,
                             final double rangeDamage, final double closeCombatDamage, double healing) {
        HP = hp;
        this.armor = armor;
        this.areaDamage = areaDamage;
        this.rangeDamage = rangeDamage;
        this.closeCombatDamage = closeCombatDamage;
        this.healing = healing;
    }

    public double getHP() {
        return HP;
    }

    public double getArmor() {
        return armor;
    }

    public double getAreaDamage() {
        return areaDamage;
    }

    public double getRangeDamage() {
        return rangeDamage;
    }

    public double getCloseCombatDamage() {
        return closeCombatDamage;
    }

    public double getHealing() {
        return healing;
    }
}
