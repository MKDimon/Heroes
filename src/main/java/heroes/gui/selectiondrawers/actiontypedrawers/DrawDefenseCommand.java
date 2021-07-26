package heroes.gui.selectiondrawers.actiontypedrawers;

import heroes.gui.TerminalWrapper;
import heroes.gui.selectiondrawers.TerminalSelectionDrawer;
import heroes.gui.utils.Colors;
import heroes.gui.utils.UnitTerminalGrid;
import heroes.mathutils.Position;

public class DrawDefenseCommand extends DrawCommand {
    public DrawDefenseCommand(final TerminalWrapper tw, final Position att, final Position def) {
        super(tw, att, def);
    }

    @Override
    public void execute() {
        UnitTerminalGrid utg = new UnitTerminalGrid(super.getTw());
        TerminalSelectionDrawer.drawSelection(super.getTw(), utg.getPair(super.getAtt()), '|', Colors.LIGHTBLUE);
    }
}
