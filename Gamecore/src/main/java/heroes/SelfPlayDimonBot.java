package heroes;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.*;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.botdimon.AntiDimon;
import heroes.player.botdimon.Dimon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SelfPlayDimonBot {
    static Logger logger = LoggerFactory.getLogger(SelfPlayDimonBot.class);

    public static void main(String[] args) throws GameLogicException, UnitException, BoardException {
        int countWin = 0;
        int countDefeat = 0;
        System.out.println(LocalDateTime.now());
        for (int i = 0; i < 50; i++) {
            final BaseBot bot1 = new AntiDimon.AntiDimonFactory().createBot(Fields.PLAYER_ONE);
            final BaseBot bot2 = new Dimon.DimonFactory().createBot(Fields.PLAYER_TWO);

            final GameLogic gameLogic = new GameLogic();
            final Army one = bot1.getArmy(null);
            final Army two = bot2.getArmy(one);
            gameLogic.gameStart(one, two);

            final Map<Fields, BaseBot> getPlayer = new HashMap<>();
            getPlayer.put(Fields.PLAYER_ONE, bot1);
            getPlayer.put(Fields.PLAYER_TWO, bot2);

            while (gameLogic.isGameBegun()) {
                final Answer answer = getPlayer.get(gameLogic.getBoard().getCurrentPlayer()).getAnswer(new Board(gameLogic.getBoard()));
                gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());

            }
            if (gameLogic.getBoard().getStatus() == GameStatus.PLAYER_ONE_WINS) countWin++;
            if (gameLogic.getBoard().getStatus() == GameStatus.PLAYER_TWO_WINS) countDefeat++;
            System.out.println(countWin + " / " + countDefeat);
        }
        System.out.println(LocalDateTime.now());
        System.out.println("Player One wins: " + countWin);
        System.out.println("Player Two wins: " + countDefeat);
    }
}
