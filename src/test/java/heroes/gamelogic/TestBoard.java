package heroes.gamelogic;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestBoard {

    @Test
    public void boardTest1() throws UnitException, BoardException {
        final General firstGeneral = new General(GeneralTypes.ARCHMAGE);
        final General secondGeneral = new General(GeneralTypes.COMMANDER);
        final Unit[][] firstArmy = new Unit[][]{
                {new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), firstGeneral, new Unit(UnitTypes.HEALER)}
        };
        final Unit[][] secondArmy = new Unit[][]{
                {new Unit(UnitTypes.MAGE), new Unit(UnitTypes.MAGE), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), secondGeneral, new Unit(UnitTypes.HEALER)}
        };
        final Board board = new Board(new Army(firstArmy, firstGeneral), new Army(secondArmy, secondGeneral));
        final Unit testSwordsman = new Unit(UnitTypes.SWORDSMAN);
        testSwordsman.inspire(GeneralTypes.ARCHMAGE.inspirationArmorBonus, GeneralTypes.ARCHMAGE.inspirationDamageBonus,
                GeneralTypes.ARCHMAGE.inspirationAccuracyBonus);
        assertAll(
                () -> assertEquals(new General(firstGeneral), board.getFieldPlayerOne()[1][1]),
                () -> assertEquals(testSwordsman, board.getUnitByCoordinate(new Position(0,1, Fields.PLAYER_ONE)))
        );
        assertTrue(ControlRound.checkStep(board));
        final Unit testUnit = new Unit(UnitTypes.HEALER);
        testUnit.inspire(GeneralTypes.COMMANDER.inspirationArmorBonus, GeneralTypes.COMMANDER.inspirationDamageBonus,
                GeneralTypes.COMMANDER.inspirationAccuracyBonus);
        assertEquals(testUnit.getArmor() ,board.getUnitByCoordinate(new Position(1,2,Fields.PLAYER_TWO)).getArmor());
    }

    @Test
    public void boardTest2() throws UnitException, BoardException {
        final General firstGeneral = new General(GeneralTypes.COMMANDER);
        final General secondGeneral = new General(GeneralTypes.SNIPER);
        final Unit[][] firstArmy = new Unit[][]{
                {new Unit(UnitTypes.BOWMAN), new Unit(UnitTypes.BOWMAN), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.HEALER), firstGeneral, new Unit(UnitTypes.MAGE)}
        };
        final Unit[][] secondArmy = new Unit[][]{
                {new Unit(UnitTypes.MAGE), new Unit(UnitTypes.MAGE), new Unit(UnitTypes.SWORDSMAN)},
                {new Unit(UnitTypes.BOWMAN), secondGeneral, new Unit(UnitTypes.BOWMAN)}
        };
        final Board board1 = new Board(new Army(firstArmy, firstGeneral), new Army(secondArmy, secondGeneral));
        final Board board2 = new Board(board1);
        assertNotSame(board1, board2);
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
