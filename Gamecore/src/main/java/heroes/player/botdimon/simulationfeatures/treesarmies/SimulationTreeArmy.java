package heroes.player.botdimon.simulationfeatures.treesarmies;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public abstract class SimulationTreeArmy {
    private static final List<Army> armies = new ArrayList<>();

    static {
        try {
            final General general_1 = new General(GeneralTypes.ARCHMAGE);
            armies.add(new Army(new Unit[][]{{
                    new Unit(UnitTypes.SWORDSMAN),
                    new Unit(UnitTypes.BOWMAN),
                    new Unit(UnitTypes.SWORDSMAN)
            }, {
                    new Unit(UnitTypes.MAGE),
                    general_1,
                    new Unit(UnitTypes.HEALER)
            }},
                    general_1
            ));
            final General general_2 = new General(GeneralTypes.SNIPER);
            armies.add(new Army(new Unit[][]{{
                    new Unit(UnitTypes.SWORDSMAN),
                    new Unit(UnitTypes.SWORDSMAN),
                    new Unit(UnitTypes.SWORDSMAN)
            }, {
                    new Unit(UnitTypes.HEALER),
                    new Unit(UnitTypes.BOWMAN),
                    general_2
            }},
                    general_2
            ));


        } catch (BoardException | UnitException e) {
            e.printStackTrace();
        }
    }

    public Army getArmy(final String params) {
        try {
            if (params.toLowerCase(Locale.ROOT).startsWith("const")) {
                final int army = Integer.parseInt(params.split(" ")[1]);
                    return (getArmyConst(army));
            } else {
                    return getArmyByArmy(params);
            }
        } catch (final IOException | UnitException | BoardException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param ID - номер армии
     * @return армию по номеру
     * @throws UnitException ошибка
     * @throws BoardException ошибка
     */
    private Army getArmyConst(final int ID) throws UnitException, BoardException {
        if (ID >= armies.size() || ID < 0) throw new IllegalArgumentException("ID of army is invalid");
        return new Army(armies.get(ID));
    }

    protected abstract Army getArmyByArmy(final String army) throws IOException, UnitException, BoardException;
}
