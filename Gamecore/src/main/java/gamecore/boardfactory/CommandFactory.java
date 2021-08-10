package gamecore.boardfactory;

import gamecore.auxiliaryclasses.ActionTypes;
import gamecore.units.Unit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandFactory {
    private final Map<ActionTypes, Command> commands;

    public CommandFactory() {
        commands = new HashMap<>();
    }

    private void initializeMap(final Unit att, final List<Unit> def) {
        commands.put(ActionTypes.HEALING, new HealCommand(att, def));
        commands.put(ActionTypes.DEFENSE, new DefenseCommand(att, def));
        commands.put(ActionTypes.CLOSE_COMBAT, new DamageCommand(att, def));
        commands.put(ActionTypes.RANGE_COMBAT, new DamageCommand(att, def));
        commands.put(ActionTypes.AREA_DAMAGE, new DamageCommand(att, def));
    }

    public Command getCommand(final Unit att, final List<Unit> def, final ActionTypes key) {
        initializeMap(att, def);
        return commands.getOrDefault(key, new DoNothingCommand(att, def));
    }
}
