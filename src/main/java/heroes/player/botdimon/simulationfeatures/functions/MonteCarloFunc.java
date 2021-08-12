package heroes.player.botdimon.simulationfeatures.functions;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.RandomBot;
import heroes.player.botdimon.MonteCarloBot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class MonteCarloFunc implements IUtilityFunc {
    private final int GAMES_COUNT = 30; // 30
    private final int DEPTH = 5; // 5
    private final IUtilityFunc func = new UtilityAnswerFuncFourV2();

    private double getResult(final Board board, final Fields field) {
        try {
            final BaseBot bot1 = new MonteCarloBot.MonteCarloFactory().createBot(Fields.PLAYER_ONE);
            final BaseBot bot2 = new MonteCarloBot.MonteCarloFactory().createBot(Fields.PLAYER_TWO);

            final GameLogic gameLogic = new GameLogic(board);

            final Map<Fields, BaseBot> getPlayer = new HashMap<>();
            getPlayer.put(Fields.PLAYER_ONE, bot1);
            getPlayer.put(Fields.PLAYER_TWO, bot2);

            for (int i = 0; gameLogic.isGameBegun() && i < DEPTH;) {

                final Answer answer = getPlayer.get(gameLogic.getBoard().getCurrentPlayer())
                        .getAnswer(new Board(gameLogic.getBoard()));
                if (gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType())) {
                    i++;
                }
            }
            return func.getValue(gameLogic.getBoard(), field);
        } catch (final GameLogicException | UnitException | BoardException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double getValue(final Board board, final Fields field) {
        long start = System.currentTimeMillis();
        double result = 0;
        try {
            ExecutorService service = Executors.newCachedThreadPool();
            for (int i = 0; i < GAMES_COUNT; i++) {
                result += service.submit(() -> getResult(board, field)).get();
            }
            service.shutdown();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        //System.out.println("Time monte carlo " + (System.currentTimeMillis() - start));
        return result;
    }
}
