package heroes.gui.selectiondrawers.actiontypedrawers;

import heroes.gui.TerminalWrapper;
import heroes.gui.selectiondrawers.TerminalSelectionDrawer;
import heroes.gui.utils.UnitTerminalGrid;
import heroes.mathutils.Position;

public class DrawMassDamageCommand extends DrawCommand {
    public DrawMassDamageCommand(final TerminalWrapper tw, final Position att, final Position def) {
        super(tw, att, def);
    }

    @Override
    public void execute() {
        UnitTerminalGrid utg = new UnitTerminalGrid(super.getTw());
        TerminalSelectionDrawer.drawSelection(super.getTw(), utg.getPair(super.getAtt()), '|', "green");
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                TerminalSelectionDrawer.drawSelection(super.getTw(),
                        utg.getPair(new Position(i, j, super.getDef().F())), '|', "darkestred");
            }
        }
    }
}
