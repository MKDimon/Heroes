package heroes.player;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.GameLogicExceptionType;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;

public abstract class BaseBot {

    public abstract static class BaseBotFactory {

        public abstract BaseBot createBot(Fields fields) throws GameLogicException;
    }

    public BaseBot(Fields field) throws GameLogicException {
        if (field == null) {
            throw new GameLogicException(GameLogicExceptionType.NULL_POINTER);
        }
        this.field = field;
    }

    private final Fields field;

    public abstract Army getArmy();

    public abstract Answer getAnswer(final Board board) throws GameLogicException;

    public Fields getField() {
        return field;
    }
}