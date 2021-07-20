package heroes.player;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;

/**
 * Класс - базовый бот.
 * @field - поле, к которому привязан бот.
 **/

public abstract class BaseBot {
    private final Fields field;

    public abstract static class BaseBotFactory {

        public abstract BaseBot createBot(final Fields fields) throws GameLogicException;
    }

    public BaseBot(final Fields field) throws GameLogicException {
        if (field == null) {
            throw new GameLogicException(GameLogicExceptionType.NULL_POINTER);
        }
        this.field = field;
    }

    public abstract Army getArmy();

    public abstract Answer getAnswer(final Board board) throws GameLogicException;

    public Fields getField() {
        return field;
    }
}
