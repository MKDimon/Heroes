package heroes.player.botgleb.clustering.clusteringfactory;

import heroes.player.botgleb.clustering.ClusteringData;
import heroes.player.botgleb.clustering.UnitDecomposition;
import heroes.units.Unit;

public abstract class UnitClustering {
    protected final Unit unit;
    protected final int x;
    protected final int y;

    public UnitClustering(final Unit unit, final int x, final int y) {
        this.unit = unit;
        this.x = x;
        this.y = y;
    }

    public abstract double getUnitsValue();

    public abstract UnitDecomposition getUnitDecomposition();
}
