package heroes.player;

import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.units.General;
import heroes.units.Unit;

public interface IPlayer {
    Unit[][] getArmy();
    General getGeneral();
    Answer getAnswer(Board board);
    Fields getField();
}
