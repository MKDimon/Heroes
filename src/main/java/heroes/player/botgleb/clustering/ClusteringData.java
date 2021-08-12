package heroes.player.botgleb.clustering;

import heroes.gamelogic.Army;
import heroes.player.botgleb.clustering.clusteringfactory.ClusteringFactory;
import heroes.units.General;
import heroes.units.Unit;

public class ClusteringData {
    private final double armyHP;
    private final double armyAverageArmor;
    private final double armyAreaDamage;
    private final double armyRangeDamage;
    private final double armyCloseCombatDamage;
    private final double armyHealing;

    public ClusteringData(final Army army){
        double armyHP = 0;
        double armyArmor = 0;
        double armyAreaDamage = 0;
        double armyRangeDamage = 0;
        double armyCloseCombatDamage = 0;
        double armyHealing = 0;
        final Unit[][] units = army.getPlayerUnits();
        final General gen = army.getGeneral();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                units[i][j].inspire(gen.inspirationArmorBonus, gen.inspirationDamageBonus, gen.inspirationAccuracyBonus);
                final UnitDecomposition unitDec = ClusteringFactory.getUnitDecomposition(units[i][j], i, j);
                armyHP += unitDec.getHP();
                armyArmor += unitDec.getHP();
                armyAreaDamage += unitDec.getAreaDamage();
                armyRangeDamage += unitDec.getRangeDamage();
                armyCloseCombatDamage += unitDec.getCloseCombatDamage();
                armyHealing += unitDec.getHealing();
            }
        }
        this.armyHP = armyHP;
        armyAverageArmor = armyArmor/6d;
        this.armyAreaDamage = armyAreaDamage;
        this.armyRangeDamage = armyRangeDamage;
        this.armyCloseCombatDamage = armyCloseCombatDamage;
        this.armyHealing = armyHealing;
    }

    public double getArmyHP() {
        return armyHP;
    }

    public double getArmyAverageArmor() {
        return armyAverageArmor;
    }

    public double getArmyAreaDamage() {
        return armyAreaDamage;
    }

    public double getArmyRangeDamage() {
        return armyRangeDamage;
    }

    public double getArmyCloseCombatDamage() {
        return armyCloseCombatDamage;
    }

    public double getArmyHealing() {
        return armyHealing;
    }

}
