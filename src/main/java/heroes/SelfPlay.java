package heroes;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.gamelogic.GameStatus;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.RandomBot;
import heroes.player.TestBot;
import heroes.player.botdimon.AntiDimon;
import heroes.player.botdimon.Dimon;
import heroes.statistics.StatisticsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SelfPlay {
    static Logger logger = LoggerFactory.getLogger(SelfPlay.class);

    private class Game extends Thread {
        private final int id;

        private Game(int id) {
            this.id = id;
        }


        @Override
        public void run() {
            for (int i = 0; i < 5000; i++) {
                try {
                    final BaseBot bot1 = new RandomBot.RandomBotFactory().createBot(Fields.PLAYER_ONE);
                    final BaseBot bot2 = new RandomBot.RandomBotFactory().createBot(Fields.PLAYER_TWO);
                    final StatisticsCollector collector = new StatisticsCollector(id);

                    final GameLogic gameLogic = new GameLogic();
                    final Army one = bot1.getArmy(null);
                    final Army two = bot2.getArmy(one);
                    gameLogic.gameStart(one, two);
                    //статистика
                    collector.recordMessageToCSV("GAME START\n");
                    collector.recordArmyToCSV(Fields.PLAYER_ONE, one);
                    collector.recordArmyToCSV(Fields.PLAYER_TWO, two);

                    final Map<Fields, BaseBot> getPlayer = new HashMap<>();
                    getPlayer.put(Fields.PLAYER_ONE, bot1);
                    getPlayer.put(Fields.PLAYER_TWO, bot2);

                    while (gameLogic.isGameBegun()) {
                        final Answer answer = getPlayer.get(gameLogic.getBoard().getCurrentPlayer()).getAnswer(gameLogic.getBoard());
                        //для статистики
                        final int defenderHP = gameLogic.getBoard().getUnitByCoordinate(answer.getDefender()).getCurrentHP();

                        // логика
                        final boolean isActionSuccess = gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());

                        // статистика
                        if (isActionSuccess) {
                            collector.recordActionToCSV(answer.getAttacker(), answer.getDefender(), answer.getActionType(),
                                    gameLogic.getBoard().getUnitByCoordinate(answer.getAttacker()),
                                    gameLogic.getBoard().getUnitByCoordinate(answer.getDefender()), Math.abs(
                                            gameLogic.getBoard().getUnitByCoordinate(answer.getDefender()).getCurrentHP()
                                                    - defenderHP));
                        }
                    }

                    final GameStatus status = gameLogic.getBoard().getStatus();
                    // статистика
                    collector.recordMessageToCSV(new StringBuffer().append("\n").append(gameLogic.getBoard().getCurNumRound()).
                            append(",").toString());
                    switch (status) {
                        case PLAYER_ONE_WINS -> collector.recordMessageToCSV(Fields.PLAYER_ONE.toString());
                        case PLAYER_TWO_WINS -> collector.recordMessageToCSV(Fields.PLAYER_TWO.toString());
                        case NO_WINNERS -> collector.recordMessageToCSV("DEAD HEAT");
                    }
                    collector.recordMessageToCSV("\nGAME OVER\n");
                } catch (final UnitException | GameLogicException ignore) {

                }
            }
        }
    }

    public void starter() {
        for (int i = 0; i < 20; i++) {
            new Game(i).start();
        }
    }

    public static void main(String[] args) throws GameLogicException, UnitException {
        int countWin = 0;
        int countDefeat = 0;
        System.out.println(LocalDateTime.now());
        for (int i = 0; i < 1; i++) {
            final BaseBot bot1 = new Dimon.DimonFactory().createBot(Fields.PLAYER_ONE);
            final BaseBot bot2 = new TestBot.TestBotFactory().createBot(Fields.PLAYER_TWO);

            final GameLogic gameLogic = new GameLogic();
            final Army one = bot1.getArmy(null);
            final Army two = bot2.getArmy(one);
            gameLogic.gameStart(one, two);

            final Map<Fields, BaseBot> getPlayer = new HashMap<>();
            getPlayer.put(Fields.PLAYER_ONE, bot1);
            getPlayer.put(Fields.PLAYER_TWO, bot2);

            while (gameLogic.isGameBegun()) {
                final Answer answer = getPlayer.get(gameLogic.getBoard().getCurrentPlayer()).getAnswer(gameLogic.getBoard());
                gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());

            }
            if (gameLogic.getBoard().getStatus() == GameStatus.PLAYER_ONE_WINS) countWin++;
            if (gameLogic.getBoard().getStatus() == GameStatus.PLAYER_TWO_WINS) countDefeat++;
        }
        System.out.println(LocalDateTime.now());
        System.out.println("Player One wins: " + countWin);
        System.out.println("Player Two wins: " + countDefeat);
    }
}
