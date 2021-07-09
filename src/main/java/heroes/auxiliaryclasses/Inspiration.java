package heroes.auxiliaryclasses;

import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.Unit;

@FunctionalInterface
public interface Inspiration {
    void inspire(final Unit unit) throws UnitException;
}
