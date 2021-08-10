package heroes.gui.heroeslanterna.selectiondrawers.actiontypedrawers;

import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.selectiondrawers.LanternaSelectionDrawer;
import heroes.gui.heroeslanterna.utils.Colors;
import heroes.gui.heroeslanterna.utils.UnitTerminalGrid;
import gamecore.mathutils.Position;

public class DrawDefenseCommand extends DrawCommand {
    public DrawDefenseCommand(final LanternaWrapper tw, final Position att, final Position def) {
        super(tw, att, def);
    }

    @Override
    public void execute() {
        UnitTerminalGrid utg = new UnitTerminalGrid(super.getTw());
        LanternaSelectionDrawer.drawSelection(super.getTw(), utg.getPair(super.getAtt()), '|', Colors.LIGHTBLUE);
    }
}
