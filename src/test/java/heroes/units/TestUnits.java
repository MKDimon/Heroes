package heroes.units;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.auxiliaryclasses.unitexception.UnitExceptionTypes;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestUnits {

    @Test
    void testUnits1() throws UnitException {
        Unit swordsman = new Unit(UnitTypes.SWORDSMAN);
        assertAll(
                () -> assertEquals(100, swordsman.getCurrentHP()),
                () -> assertEquals(30, swordsman.getPower()),
                () -> assertEquals(85, swordsman.getAccuracy()),
                () -> assertEquals(30, swordsman.getArmor()),
                () -> assertEquals(ActionTypes.CLOSE_COMBAT, swordsman.getActionType())
        );
        swordsman.inspire(new General(GeneralTypes.COMMANDER).getInspiration());
        assertEquals(40, swordsman.getArmor());
        swordsman.deinspire(new General(GeneralTypes.COMMANDER).getDeinspiration());
        assertEquals(30, swordsman.getArmor());
    }

    @Test
    void testUnits2() throws UnitException {
        General archmage = new General(GeneralTypes.ARCHMAGE);
        archmage.inspire(archmage.getInspiration());
        assertAll(
                () -> assertEquals(70, archmage.getCurrentHP()),
                () -> assertEquals(25, archmage.getPower()),
                () -> assertEquals(85, archmage.getAccuracy()),
                () -> assertEquals(10, archmage.getArmor()),
                () -> assertEquals(ActionTypes.AREA_DAMAGE, archmage.getActionType())
        );
    }

    @Test
    void testErrors1(){
        try{
            Unit healer = new Unit(UnitTypes.HEALER);
            healer.setCurrentHP(120);
        } catch (UnitException e){
            assertEquals(UnitExceptionTypes.INCORRECT_HP.getErrorType() ,e.getMessage());
        }
        try{
            Unit healer = new Unit(UnitTypes.HEALER);
            healer.setAccuracy(-4);
        } catch (UnitException e){
            assertEquals(UnitExceptionTypes.INCORRECT_ACCURACY.getErrorType() ,e.getMessage());
        }
        try{
            Unit healer = new Unit(UnitTypes.HEALER);
            healer.setArmor(-4);
        } catch (UnitException e){
            assertEquals(UnitExceptionTypes.INCORRECT_ARMOR.getErrorType() ,e.getMessage());
        }
        try{
            Unit healer = new Unit(UnitTypes.HEALER);
            healer.setPower(-333);
        } catch (UnitException e){
            assertEquals(UnitExceptionTypes.INCORRECT_POWER.getErrorType() ,e.getMessage());
        }
        try{
            Unit healer = new Unit((Unit) null);
        } catch (UnitException e){
            assertEquals(UnitExceptionTypes.NULL_POINTER.getErrorType() ,e.getMessage());
        }
    }

    @Test
    void testUnits3() throws UnitException {
        Unit bowman = new Unit(UnitTypes.BOWMAN);
        assertTrue(bowman.isAlive());
        bowman.setCurrentHP(-5);
        assertFalse(bowman.isAlive());
    }

}
