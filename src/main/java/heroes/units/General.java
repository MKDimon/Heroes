package heroes.units;

import heroes.auxiliaryclasses.Deinspiration;
import heroes.auxiliaryclasses.Inspiration;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.auxiliaryclasses.unitexception.UnitExceptionTypes;

public class General extends Unit {
    private final Inspiration inspiration;
    private final Deinspiration deinspiration;

    public General(GeneralTypes generalType) throws UnitException {
        super(generalType.getUnitType());
        if (generalType.getInspiration() == null || generalType.getDeinspiration() == null) {
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        inspiration = generalType.getInspiration();
        deinspiration = generalType.getDeinspiration();
    }

    public General(General general) throws UnitException {
        super(general);
        if (general.getInspiration() == null || general.getDeinspiration() == null) {
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        inspiration = general.getInspiration();
        deinspiration = general.getDeinspiration();
    }

    public Inspiration getInspiration() {
        return inspiration;
    }

    public Deinspiration getDeinspiration() {
        return deinspiration;
    }
}
