package heroes.player.botgleb.clustering.clusteringfactory;

import heroes.player.botgleb.clustering.UnitDecomposition;
import heroes.units.Unit;

public class MageClustering extends UnitClustering {

    public MageClustering(Unit unit, int x, int y) {
        super(unit, x, y);
    }

    @Override
    public double getUnitsValue() {
        if (y == 0) {
            return 0.5;
        }
        return 1d;
    }

    @Override
    public UnitDecomposition getUnitDecomposition() {
        final double value = getUnitsValue();
        return new UnitDecomposition(value * unit.getMaxHP(), value * unit.getArmor(),
                (double)unit.getAccuracy()/100 * unit.getPower() * value, 0,
                0, 0);
    }
}
