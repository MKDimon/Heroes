package heroes.statistics;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.statisticsexception.StatisticsException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStatisticAnalyzer {

    @Test
    public void testAnalyzeArmyStatistics() throws StatisticsException, BoardException, UnitException {
        final List<GameLogInformation> games = StatisticsParser.parseLogFile("src/main/resources/teststatistics.csv");
        final Map<Army, Integer[]> testMap = StatisticsAnalyzer.analyzeArmiesStatistics(games);
        final General generalPlayerTwo = new General(GeneralTypes.ARCHMAGE);
        final Unit[][] armyPlayerTwo = new Unit[2][3];
        armyPlayerTwo[0][0] = new Unit(UnitTypes.BOWMAN); armyPlayerTwo[1][0] = new Unit(UnitTypes.BOWMAN);
        armyPlayerTwo[0][1] = generalPlayerTwo; armyPlayerTwo[1][1] = new Unit(UnitTypes.MAGE);
        armyPlayerTwo[0][2] = new Unit(UnitTypes.HEALER); armyPlayerTwo[1][2] = new Unit(UnitTypes.HEALER);
        final Army armyTwo = new Army(armyPlayerTwo, generalPlayerTwo);
        final Integer[] testArr = testMap.get(armyTwo);
        assertAll(
                ()-> assertEquals(2, testArr[0]),
                ()-> assertEquals(0, testArr[1]),
                ()-> assertEquals(0, testArr[2])
        );
    }
}
