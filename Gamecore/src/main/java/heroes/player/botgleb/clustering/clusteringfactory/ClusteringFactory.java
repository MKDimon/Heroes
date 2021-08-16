package heroes.player.botgleb.clustering.clusteringfactory;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.player.botgleb.clustering.UnitDecomposition;
import heroes.units.Unit;

import java.util.Map;

public class ClusteringFactory {

    private static Map<ActionTypes, UnitClustering> initializeMap(final Unit unit, final int x, final int y) {
        return Map.of(
                ActionTypes.CLOSE_COMBAT, new SwordsmanClustering(unit, x, y),
                ActionTypes.RANGE_COMBAT, new BowmanClustering(unit, x, y),
                ActionTypes.AREA_DAMAGE, new MageClustering(unit, x, y),
                ActionTypes.HEALING, new HealerClustering(unit, x, y)
        );
    }

    public static UnitDecomposition getUnitDecomposition(final Unit unit, final int x, final int y) {
        return initializeMap(unit,x,y).get(unit.getActionType()).getUnitDecomposition();
    }
}
