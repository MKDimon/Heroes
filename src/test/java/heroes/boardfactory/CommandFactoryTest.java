package heroes.boardfactory;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommandFactoryTest {
    @Test
    public void test_command() throws UnitException {
        Unit att = new Unit(UnitTypes.SWORDSMAN);
        Unit defUnit = new Unit(UnitTypes.SWORDSMAN);
        List<Unit> def = new ArrayList<>();
        def.add(defUnit);
        CommandFactory cf = new CommandFactory();
        assertAll(
                ()-> assertEquals(DamageCommand.class, cf.getCommand(att, def, ActionTypes.CLOSE_COMBAT).getClass()),
                ()-> assertEquals(DamageCommand.class, cf.getCommand(att, def, ActionTypes.RANGE_COMBAT).getClass()),
                ()-> assertEquals(HealCommand.class, cf.getCommand(att, def, ActionTypes.HEALING).getClass()),
                ()-> assertEquals(DefenseCommand.class, cf.getCommand(att, def, ActionTypes.DEFENSE).getClass()),
                ()-> assertEquals(DamageCommand.class, cf.getCommand(att, def, ActionTypes.AREA_DAMAGE).getClass())
        );
    }
    @Test
    public void test_command_heal() throws UnitException {
        Unit att = new Unit(UnitTypes.HEALER);
        Unit defUnit = new Unit(UnitTypes.SWORDSMAN);
        List<Unit> def = new ArrayList<>();
        def.add(defUnit);
        defUnit.setCurrentHP(90);
        CommandFactory cf = new CommandFactory();
        cf.getCommand(att, def, ActionTypes.HEALING).execute();

        assertAll(
                ()-> assertEquals(HealCommand.class, cf.getCommand(att, def, ActionTypes.HEALING).getClass()),
                ()-> assertEquals(100, defUnit.getCurrentHP())
        );
    }
}
