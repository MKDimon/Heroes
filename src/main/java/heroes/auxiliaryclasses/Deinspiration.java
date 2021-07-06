package heroes.auxiliaryclasses;

import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.Unit;

@FunctionalInterface
public interface Deinspiration {
    void deinspire(Unit unit) throws UnitException;
}
