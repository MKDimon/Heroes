package heroes.player;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.mathutils.Position;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.units.General;
import heroes.units.Unit;

public interface IPlayer {
    boolean createArmyAndGeneral(final int TEST_PARAMETER); // MUST BE REMOVED AFTER TESTS

    Unit[][] getArmy();

    General getGeneral();

    Answer getAnswer(Board board) throws GameLogicException;

    Fields getField();
}
