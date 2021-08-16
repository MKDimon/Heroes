package heroes.player;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.IGUI;

/**
 * Класс - базовый бот.
 **/

public abstract class BaseBot {
    private final Fields field;
    protected IGUI gui;

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
