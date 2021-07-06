package heroes.units;

import heroes.units.auxiliaryclasses.Inspiration;
import heroes.units.auxiliaryclasses.UnitException;
import heroes.units.auxiliaryclasses.UnitExceptionTypes;

public class General extends Unit {
    private Inspiration inspiration;

    public General(GeneralTypes generalType) throws UnitException {
        super(generalType.getUnitType());
        if(generalType.getInspiration() == null){
            throw new UnitException(UnitExceptionTypes.NULL_POINTER);
        }
        inspiration = generalType.getInspiration();
    }
}
