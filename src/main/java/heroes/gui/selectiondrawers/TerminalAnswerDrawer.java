package heroes.gui.selectiondrawers;

import heroes.gui.TerminalWrapper;
import heroes.gui.selectiondrawers.actiontypedrawers.DrawCommandMap;
import heroes.player.Answer;

public class TerminalAnswerDrawer {
    public static void drawAnswer(final TerminalWrapper tw, final Answer answer) {
        DrawCommandMap drawCommandMap = new DrawCommandMap();
        drawCommandMap.getCommand(tw, answer.getAttacker(), answer.getDefender(), answer.getActionType()).execute();
    }
}
