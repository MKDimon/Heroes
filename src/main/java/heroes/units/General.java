package heroes.units;

import heroes.auxiliaryclasses.Deinspiration;
import heroes.auxiliaryclasses.Inspiration;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.auxiliaryclasses.unitexception.UnitExceptionTypes;

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
