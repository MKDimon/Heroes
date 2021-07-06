package heroes.boardfactory;

import heroes.units.Unit;
import heroes.units.auxiliaryclasses.ActionTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFactory {

    private final Map<ActionTypes, Command> commands;

    public CommandFactory() {
        commands = new HashMap<>();
    }

    private void initializeMap(Unit att, Unit def) {
        commands.put(ActionTypes.HEALING, new HealCommand(att, def));
        commands.put(ActionTypes.DEFENSE, new DefenseCommand(att, def));
        commands.put(ActionTypes.CLOSE_COMBAT, new DamageCommand(att, def));
        commands.put(ActionTypes.RANGE_COMBAT, new DamageCommand(att, def));
        commands.put(ActionTypes.AREA_DAMAGE, new DamageCommand(att, def));
    }

    public Command getCommand(Unit att, Unit def, ActionTypes key) {
        initializeMap(att, def);
        return commands.getOrDefault(key, new DoNothingCommand(att, def));
    }
}
