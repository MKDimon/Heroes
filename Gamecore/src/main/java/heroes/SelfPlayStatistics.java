package heroes;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.gamelogic.GameStatus;
import heroes.player.*;
import heroes.statistics.StatisticsCollector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelfPlayStatistics {

    public static void main(final String[] args) throws GameLogicException, UnitException {
        final List<BaseBot.BaseBotFactory> factories = Arrays.asList(
                new RandomBot.RandomBotFactory(),
                new TestBot.TestBotFactory(),
                new PlayerBot.PlayerBotFactory());
        int playerOneCount = 0;
        int playerTwoCount = 0;
        int drawCount = 0;
        for(int i = 0; i < 1; i++) {
        final BaseBot playerOne = factories.get(1).createBot(Fields.PLAYER_ONE);
        final BaseBot playerTwo = factories.get(1).createBot(Fields.PLAYER_TWO);
        final Map<Fields, BaseBot> getPlayer = new HashMap<>();
        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTwo);
            GameLogic gl = new GameLogic();
            final Army firstPlayerArmy = playerOne.getArmy(null);
            final Army secondPlayerArmy = playerTwo.getArmy(firstPlayerArmy);
            gl.gameStart(firstPlayerArmy, secondPlayerArmy);
            final StatisticsCollector collector = new StatisticsCollector(0);
            collector.recordMessageToCSV("GAME START\n");
            collector.recordPlayerToCSVForDB("Angry", "TEST", firstPlayerArmy);
            collector.recordPlayerToCSVForDB("Weber", "TEST", secondPlayerArmy);
            collector.recordDateToCSV();

            while (gl.isGameBegun()) {
                final Answer answer = getPlayer.get(gl.getBoard().getCurrentPlayer()).getAnswer(gl.getBoard());
                final int defenderHP = gl.getBoard().getUnitByCoordinate(answer.getDefender()).getCurrentHP();
                final boolean isActionSuccess = gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());

                // ????????????????????
                if (isActionSuccess) {
                    collector.recordActionToCSV(answer.getAttacker(), answer.getDefender(), answer.getActionType(),
                            gl.getBoard().getUnitByCoordinate(answer.getAttacker()),
                            gl.getBoard().getUnitByCoordinate(answer.getDefender()), Math.abs(
                                    gl.getBoard().getUnitByCoordinate(answer.getDefender()).getCurrentHP()
                                            - defenderHP));
                }
            }
            final GameStatus status = gl.getBoard().getStatus();
            collector.recordMessageToCSV(new StringBuffer().append("\n").append(gl.getBoard().getCurNumRound()).
                    append(",").toString());
            switch (status) {
                case PLAYER_ONE_WINS -> {collector.recordMessageToCSV(Fields.PLAYER_ONE.toString()); playerOneCount++;}
                case PLAYER_TWO_WINS -> {collector.recordMessageToCSV(Fields.PLAYER_TWO.toString()); playerTwoCount++;}
                case NO_WINNERS -> {collector.recordMessageToCSV("DRAW"); drawCount++;}
            }
            collector.recordMessageToCSV("\n");
            collector.recordDateToCSV();
            collector.recordMessageToCSV("\nGAME OVER\n");


        }
        System.out.println("SimulationOneStep: " + playerOneCount + " MinMax: " + playerTwoCount + " draw: " + drawCount);
    }
}
