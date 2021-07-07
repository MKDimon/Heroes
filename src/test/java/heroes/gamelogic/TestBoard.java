package heroes.gamelogic;

import static org.junit.jupiter.api.Assertions.*;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

public class TestBoard {

    @Test
    public void boardTest1() throws UnitException {
        General firstGeneral = new General(GeneralTypes.ARCHMAGE);
        General secondGeneral = new General(GeneralTypes.COMMANDER);
        Unit[][] firstArmy = new Unit[][]{
                {new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), firstGeneral, new Unit(UnitTypes.HEALER)}
        };
        Unit[][] secondArmy = new Unit[][]{
                {new Unit(UnitTypes.MAGE), new Unit(UnitTypes.MAGE), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), secondGeneral, new Unit(UnitTypes.HEALER)}
        };
        Board board = new Board(firstArmy, secondArmy, firstGeneral, secondGeneral);
        Unit testSwordsman = new Unit(UnitTypes.SWORDSMAN);
        testSwordsman.inspire(GeneralTypes.ARCHMAGE.getInspiration());
        assertAll(
                () -> assertEquals(new General(firstGeneral), board.getFieldPlayerOne()[1][1]),
                () -> assertEquals(testSwordsman, board.getUnitByCoordinate(new Position(0,1, Fields.PLAYER_ONE)))
        );
        assertTrue(ControlRound.checkStep(board));
        Unit testUnit = new Unit(UnitTypes.HEALER);
        testUnit.inspire(GeneralTypes.COMMANDER.getInspiration());
        assertEquals(testUnit.getArmor() ,board.getUnitByCoordinate(new Position(1,2,Fields.PLAYER_TWO)).getArmor());
    }

    @Test
    public void boardTest2() throws UnitException, BoardException {
        General firstGeneral = new General(GeneralTypes.COMMANDER);
        General secondGeneral = new General(GeneralTypes.SNIPER);
        Unit[][] firstArmy = new Unit[][]{
                {new Unit(UnitTypes.BOWMAN), new Unit(UnitTypes.BOWMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.HEALER), firstGeneral, new Unit(UnitTypes.MAGE)}
        };
        Unit[][] secondArmy = new Unit[][]{
                {new Unit(UnitTypes.MAGE), new Unit(UnitTypes.MAGE), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), secondGeneral, new Unit(UnitTypes.BOWMAN)}
        };
        Board board1 = new Board(firstArmy, secondArmy, firstGeneral, secondGeneral);
        Board board2 = new Board(board1);
        assertTrue(board1 != board2);
        for(int i = 0; i<2; i++){
            for(int j = 0; j < 3; j++){
                assertEquals(board1.getArmy(Fields.PLAYER_ONE)[i][j], board2.getArmy(Fields.PLAYER_ONE)[i][j]);
            }
        }
        for(int i = 0; i<2; i++){
            for(int j = 0; j < 3; j++){
                assertEquals(board1.getArmy(Fields.PLAYER_ONE)[i][j], board2.getArmy(Fields.PLAYER_ONE)[i][j]);
            }
        }
    }

}
