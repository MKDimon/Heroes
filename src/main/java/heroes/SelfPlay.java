package heroes;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.boardfactory.DamageCommand;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.player.Answer;
import heroes.player.IPlayer;
import heroes.player.TestBot;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class SelfPlay {


    public static void main(String[] args) throws GameLogicException, UnitException {
        Logger logger = LoggerFactory.getLogger(SelfPlay.class);
        /*
            Инициализация ботов
         */
        IPlayer playerOne = new TestBot(Fields.PLAYER_ONE);
        IPlayer playerTwo = new TestBot(Fields.PLAYER_TWO);
        Map<Fields, IPlayer> getPlayer = new HashMap<>();
        getPlayer.put(Fields.PLAYER_ONE, playerOne);
        getPlayer.put(Fields.PLAYER_TWO, playerTwo);

        GameLogic gl = new GameLogic();
        gl.gameStart(playerOne.getArmy(), playerTwo.getArmy());
        while (gl.isGameBegun()) {
            Answer answer = getPlayer.get(gl.getBoard().getCurrentPlayer()).getAnswer(gl.getBoard());
            gl.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
        }
    }
}
