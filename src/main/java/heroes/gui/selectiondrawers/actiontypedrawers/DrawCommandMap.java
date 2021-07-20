package heroes.gui.selectiondrawers.actiontypedrawers;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.boardfactory.*;
import heroes.gui.TerminalWrapper;
import heroes.mathutils.Position;
import heroes.units.Unit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawCommandMap {
    private final Map<ActionTypes, DrawCommand> commands;

    public DrawCommandMap() {
        commands = new HashMap<>();
    }

    private void initializeMap(final TerminalWrapper tw, final Position att, final Position def) {
        commands.put(ActionTypes.HEALING, new DrawHealCommand(tw, att, def));
        commands.put(ActionTypes.DEFENSE, new DrawDefenseCommand(tw, att, def));
        commands.put(ActionTypes.CLOSE_COMBAT, new DrawDamageCommand(tw, att, def));
        commands.put(ActionTypes.RANGE_COMBAT, new DrawRangeDamageCommand(tw, att, def));
        commands.put(ActionTypes.AREA_DAMAGE, new DrawMassDamageCommand(tw, att, def));
    }

    public DrawCommand getCommand(final TerminalWrapper tw, final Position att, final Position def, final ActionTypes key) {
        initializeMap(tw, att, def);
        return commands.get(key);
    }
}
