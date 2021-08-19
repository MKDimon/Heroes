package heroes.player;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.clientserver.ClientsConfigs;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.IGUI;
import heroes.gui.Visualisable;

/**
 * Класс - базовый бот.
 **/

public abstract class BaseBot implements Visualisable {
    private final Fields field;
    protected IGUI gui;

    @Override
    public void setTerminal(final IGUI gui) {
        this.gui = gui;
    }

    public abstract static class BaseBotFactory {
        public abstract BaseBot createBot(final Fields fields) throws GameLogicException;
        public abstract BaseBot createBotWithConfigs(final Fields fields, final ClientsConfigs clientsConfigs)
                throws GameLogicException;
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
