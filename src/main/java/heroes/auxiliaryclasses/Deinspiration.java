package heroes.auxiliaryclasses;

import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.Unit;

public interface Deinspiration {
    void deinspire(Unit unit) throws UnitException;
}
