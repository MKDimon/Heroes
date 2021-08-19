package heroes.player.botgleb;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.clientserver.ClientsConfigs;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameStatus;
import heroes.gui.Visualisable;
import heroes.player.BaseBot;
import heroes.player.TestBot;

public abstract class AIBot extends BaseBot implements Visualisable {
    public static final UtilityFunction baseUtilityFunction = UtilityFunctions.HPUtilityFunction;
    public static final int baseMaxRecLevel = 3;
    private final UtilityFunction utilityFunction;
    private final int maxRecLevel;

    public abstract static class AIBotFactory extends BaseBotFactory {

        @Override
        public abstract AIBot createBot(final Fields fields) throws GameLogicException;

        public abstract AIBot createAIBot(final Fields fields, final UtilityFunction utilityFunction,
                                          int maxRecLevel) throws GameLogicException;
        @Override
        public abstract AIBot createBotWithConfigs(final Fields field, final ClientsConfigs configs)
                throws GameLogicException;
    }

    public AIBot(final Fields field, final UtilityFunction utilityFunction, final int maxRecLevel)
            throws GameLogicException {
        super(field);
        this.utilityFunction = utilityFunction;
        this.maxRecLevel = maxRecLevel;
    }

    public AIBot(final Fields field) throws GameLogicException {
        this(field, baseUtilityFunction, baseMaxRecLevel);
    }

    public AIBot(final Fields field, final ClientsConfigs clientsConfigs)
            throws GameLogicException {
        this(field, baseUtilityFunction, clientsConfigs.HEIGHT);
    }

    public int getMaxRecLevel() {
        return maxRecLevel;
    }

    public UtilityFunction getUtilityFunction(){
        return utilityFunction;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            return new TestBot(getField()).getArmy(firstPlayerArmy);
        } catch (GameLogicException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Метод вычисляет тип терминального состояния и выдает в соответствии с ним значение функции полезности
     * (+- условная бесконечность, либо DRAW_VALUE, если ничья).
     **/

    protected double getTerminalStateValue(final Board board) throws GameLogicException {
        if (board.getStatus() == GameStatus.GAME_PROCESS) {
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
        if (board.getStatus() == GameStatus.NO_WINNERS) {
            return UtilityFunctions.DRAW_VALUE;
        }
        if (board.getStatus() == GameStatus.PLAYER_ONE_WINS && getField() == Fields.PLAYER_ONE ||
                board.getStatus() == GameStatus.PLAYER_TWO_WINS && getField() == Fields.PLAYER_TWO) {
            return UtilityFunctions.MAX_VALUE;
        } else {
            return UtilityFunctions.MIN_VALUE;
        }
    }
}
