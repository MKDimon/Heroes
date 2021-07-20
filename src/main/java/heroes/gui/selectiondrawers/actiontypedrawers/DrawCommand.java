package heroes.gui.selectiondrawers.actiontypedrawers;

import heroes.gui.TerminalWrapper;
import heroes.mathutils.Position;

import java.util.List;


public abstract class DrawCommand {
    private final Position att;
    private final Position def;
    private final TerminalWrapper tw;

    public Position getAtt() {
        return att;
    }

    public Position getDef() {
        return def;
    }

    public TerminalWrapper getTw() {
        return tw;
    }

    public DrawCommand(final TerminalWrapper tw, final Position att, final Position def) {
        this.tw = tw;
        this.att = att;
        this.def = def;
    }

    public abstract void execute();
}
