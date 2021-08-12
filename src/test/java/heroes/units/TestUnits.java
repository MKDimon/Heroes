package heroes.units;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.auxiliaryclasses.unitexception.UnitExceptionTypes;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

public class TestUnits {
    Logger logger = LoggerFactory.getLogger(TestUnits.class);

    @Test
    void testUnits1() throws UnitException {
        final Unit swordsman = new Unit(UnitTypes.SWORDSMAN);
        assertAll(
                () -> assertEquals(80, swordsman.getCurrentHP()),
                () -> assertEquals(30, swordsman.getPower()),
                () -> assertEquals(70, swordsman.getAccuracy()),
                () -> assertEquals(25, swordsman.getArmor()),
                () -> assertEquals(ActionTypes.CLOSE_COMBAT, swordsman.getActionType())
        );
        final General gen = new General(GeneralTypes.COMMANDER);
        swordsman.inspire(gen.inspirationArmorBonus, gen.inspirationDamageBonus, gen.inspirationDamageBonus);
        assertEquals(35, swordsman.getArmor());
        swordsman.deinspire();
        assertEquals(25, swordsman.getArmor());
    }

    @Test
    void testUnits2() throws UnitException {
        final General archmage = new General(GeneralTypes.ARCHMAGE);
        archmage.inspire(archmage.inspirationArmorBonus, archmage.inspirationDamageBonus, archmage.inspirationAccuracyBonus);
        assertAll(
                () -> assertEquals(50, archmage.getCurrentHP()),
                () -> assertEquals(10, archmage.getPower()),
                () -> assertEquals(80, archmage.getAccuracy()),
                () -> assertEquals(10, archmage.getArmor()),
                () -> assertEquals(ActionTypes.AREA_DAMAGE, archmage.getActionType())
        );
    }

    @Test
    void testErrors1(){
        try{
            final Unit healer = new Unit(UnitTypes.HEALER);
            healer.setCurrentHP(120);
        } catch (UnitException e){
            assertEquals(UnitExceptionTypes.INCORRECT_HP.getErrorType() ,e.getMessage());
        }
        try{
            final Unit healer = new Unit(UnitTypes.HEALER);
            healer.setAccuracy(-4);
        } catch (UnitException e){
            assertEquals(UnitExceptionTypes.INCORRECT_ACCURACY.getErrorType() ,e.getMessage());
        }
        try{
            final Unit healer = new Unit(UnitTypes.HEALER);
            healer.setArmor(-4);
        } catch (UnitException e){
            assertEquals(UnitExceptionTypes.INCORRECT_ARMOR.getErrorType() ,e.getMessage());
        }
        try{
            final Unit healer = new Unit(UnitTypes.HEALER);
            healer.setPower(-333);
        } catch (UnitException e){
            assertEquals(UnitExceptionTypes.INCORRECT_POWER.getErrorType() ,e.getMessage());
        }
        try{
            final Unit healer = new Unit((Unit) null);
        } catch (UnitException e){
            assertEquals(UnitExceptionTypes.NULL_POINTER.getErrorType() ,e.getMessage());
        }
    }

    @Test
    void testUnits3() throws UnitException {
        final Unit bowman = new Unit(UnitTypes.BOWMAN);
        assertTrue(bowman.isAlive());
        bowman.setCurrentHP(-5);
        assertFalse(bowman.isAlive());
    }

}
