package heroes.player;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.mathutils.Position;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.units.General;
import heroes.units.Unit;

public interface IPlayer {
    Army getArmy() throws GameLogicException, UnitException;
    General getGeneral() throws UnitException;
    Answer getAnswer(Board board) throws GameLogicException;
    Fields getField();
}
