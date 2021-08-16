package heroes.player.botgleb.clustering.clusteringfactory;

import heroes.player.botgleb.clustering.UnitDecomposition;
import heroes.units.Unit;

public class SwordsmanClustering extends UnitClustering {

    public SwordsmanClustering(final Unit unit, final int x, final int y) {
        super(unit, x, y);
    }

    @Override
    public double getUnitsValue() {
        if (super.y > 0) {
            return 0.2;
        }
        return 1d;
    }

    @Override
    public UnitDecomposition getUnitDecomposition() {
        final double value = getUnitsValue();
        return new UnitDecomposition(value * unit.getMaxHP(), value * unit.getArmor(),
                0, 0,
                (double)unit.getAccuracy()/100 * unit.getPower() * value, 0);
    }
}
