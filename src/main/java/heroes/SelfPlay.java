package heroes;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.player.IPlayer;
import heroes.player.TestBot;
import heroes.units.Unit;

public class SelfPlay {
    public static void main(String[] args) {
        /*
            Инициализация ботов
         */
        IPlayer playerOne = new TestBot(Fields.PLAYER_ONE, 1);
        IPlayer playerTwo = new TestBot(Fields.PLAYER_TWO, 2);
        Board board = new Board(playerOne.getArmy(), playerTwo.getArmy(),
                playerOne.getGeneral(), playerTwo.getGeneral());

        try {
            GameLogic gl = new GameLogic(board);
            while(gl.isGameBegun()) {
                gl.gameStart(playerOne.getArmy(), playerTwo.getArmy(),
                        playerOne.getGeneral(), playerTwo.getGeneral());
            }
        } catch (UnitException | BoardException e) {
            // TODO: Log this cutie
            e.printStackTrace();
        }


    }
}
