package heroes.player.botdimon.simulationfeatures.treesarmies;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    /**
     *
     * @param ID - номер армии
     * @return армию по номеру
     * @throws UnitException ошибка
     * @throws BoardException ошибка
     */
    public Army getArmyConst(final int ID) throws UnitException, BoardException {
        //if (ID >= armies.size() || ID < 0) throw new IllegalArgumentException("ID of army is invalid");
        //return new Army(armies.get(ID));
        try {
            final Random r = new Random();
            final Unit[][] armyArr = new Unit[2][3];
            final UnitTypes[] unitTypes = UnitTypes.values();
            final GeneralTypes[] generalTypes = GeneralTypes.values();
            final General general = new General(generalTypes[r.nextInt(generalTypes.length)]);
            armyArr[r.nextInt(2)][r.nextInt(3)] = general;
            try {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 3; j++) {
                        if (armyArr[i][j] == null) {
                            armyArr[i][j] = new Unit(unitTypes[r.nextInt(unitTypes.length)]);
                        }
                    }
                }
            } catch (UnitException e) {
            }
            return new Army(armyArr, general);
        } catch (final BoardException | UnitException e) {
            return null;
        }
    }

    public abstract Army getArmyByArmy(final Army army);
}
