package heroes.player;

import gamecore.auxiliaryclasses.gamelogicexception.GameLogicException;
import gamecore.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import gamecore.gamelogic.Army;
import gamecore.gamelogic.Board;
import gamecore.gamelogic.Fields;
import gamecore.player.Answer;
import heroes.gui.Visualisable;
import heroes.gui.heroeslanterna.LanternaWrapper;

/**
 * Класс - базовый бот.
 **/

public abstract class BaseBot implements Visualisable {
    private final Fields field;
    protected LanternaWrapper tw;

    @Override
    public void setTerminal(final LanternaWrapper tw) {
        this.tw = tw;
    }

    public abstract static class BaseBotFactory {
        public abstract BaseBot createBot(final Fields fields) throws GameLogicException;
    }

    public BaseBot(final Fields field) throws GameLogicException {
        if (field == null) {
            throw new GameLogicException(GameLogicExceptionType.NULL_POINTER);
        }
        this.field = field;
    }

    public abstract Army getArmy(final Army firstPlayerArmy);

    public abstract Answer getAnswer(final Board board) throws GameLogicException;

    public Fields getField() {
        return field;
    }
}
