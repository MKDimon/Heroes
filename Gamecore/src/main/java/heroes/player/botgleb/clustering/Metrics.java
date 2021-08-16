package heroes.player.botgleb.clustering;

public class Metrics {
    public static Metric <ClusteringData> d2ClusteringDataMetric = (cl1, cl2) ->
        (cl1.getArmyHP() - cl2.getArmyHP()) *
                (cl1.getArmyHP() - cl2.getArmyHP()) +
        (cl1.getArmyAverageArmor() - cl2.getArmyAverageArmor()) *
                (cl1.getArmyAverageArmor() - cl2.getArmyAverageArmor()) +
        (cl1.getArmyCloseCombatDamage() - cl2.getArmyCloseCombatDamage()) *
                (cl1.getArmyCloseCombatDamage() - cl2.getArmyCloseCombatDamage()) +
        (cl1.getArmyRangeDamage() - cl2.getArmyRangeDamage()) *
                (cl1.getArmyRangeDamage() - cl2.getArmyRangeDamage()) +
        (cl1.getArmyAreaDamage() - cl2.getArmyAreaDamage()) *
                (cl1.getArmyAreaDamage() - cl2.getArmyAreaDamage()) +
                (cl1.getArmyHealing() - cl2.getArmyHealing()) *
                        (cl1.getArmyHealing() - cl2.getArmyHealing());

}
