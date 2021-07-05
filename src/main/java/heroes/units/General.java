package heroes.units;

import heroes.units.auxiliaryclasses.UnitException;

public class General extends Unit {
    //придумать как реализовать воодушевление

    public General(GeneralTypes generalType) throws UnitException {
        super(generalType.getUnitType());
    }
}
