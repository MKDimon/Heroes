package heroes.gui.heroeslanterna.selectiondrawers.actiontypedrawers;

import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.selectiondrawers.LanternaSelectionDrawer;
import heroes.gui.heroeslanterna.utils.Colors;
import heroes.gui.heroeslanterna.utils.UnitTerminalGrid;
import heroes.mathutils.Position;

public class DrawHealCommand extends DrawCommand {
    public DrawHealCommand(final LanternaWrapper tw, final Position att, final Position def) {
        super(tw, att, def);
    }

    @Override
    public void execute() {
        UnitTerminalGrid utg = new UnitTerminalGrid(super.getTw());
        LanternaSelectionDrawer.drawSelection(super.getTw(), utg.getPair(super.getAtt()), '|', Colors.GREEN);
        LanternaSelectionDrawer.drawSelection(super.getTw(), utg.getPair(super.getDef()), '+', Colors.PINK);
    }
}
