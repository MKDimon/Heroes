package heroes.auxiliaryclasses;

import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.Unit;

public interface Inspiration {
    void inspire(Unit unit) throws UnitException;
}
