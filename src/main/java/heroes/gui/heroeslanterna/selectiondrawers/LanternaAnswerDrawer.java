package heroes.gui.heroeslanterna.selectiondrawers;

import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.gui.heroeslanterna.selectiondrawers.actiontypedrawers.DrawCommandMap;
import gamecore.player.Answer;

public class LanternaAnswerDrawer {
    public static void drawAnswer(final LanternaWrapper tw, final Answer answer) {
        final DrawCommandMap drawCommandMap = new DrawCommandMap();
        drawCommandMap.getCommand(tw, answer.getAttacker(), answer.getDefender(), answer.getActionType()).execute();
    }
}
