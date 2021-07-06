package heroes.units;

import heroes.units.auxiliaryclasses.Deinspiration;
import heroes.units.auxiliaryclasses.Inspiration;
import heroes.units.auxiliaryclasses.UnitException;
import heroes.units.auxiliaryclasses.UnitExceptionTypes;

public class General extends Unit {
    private Inspiration inspiration;
    private Deinspiration deinspiration;

    public General(GeneralTypes generalType) throws UnitException {
        super(generalType.getUnitType());
        if(generalType.getInspiration() == null){
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        inspiration = generalType.getInspiration();
        deinspiration = generalType.getDeinspiration();
    }

    public Inspiration getInspiration() {
        return inspiration;
    }

    public Deinspiration getDeinspiration() {
        return deinspiration;
    }
}
