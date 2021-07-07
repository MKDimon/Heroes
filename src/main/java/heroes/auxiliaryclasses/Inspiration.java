package heroes.auxiliaryclasses;

import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.Unit;

@FunctionalInterface
public interface Inspiration {
    void inspire(Unit unit) throws UnitException;
}
