package heroes.player;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;

public interface IPlayer {
    Army getArmy();
    Answer getAnswer(final Board board) throws GameLogicException;
    Fields getField();
}
