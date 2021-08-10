package heroes.statistics;

import gamecore.units.General;
import gamecore.units.GeneralTypes;
import gamecore.units.Unit;
import gamecore.units.UnitTypes;
import gamecore.auxiliaryclasses.ActionTypes;
import gamecore.auxiliaryclasses.boardexception.BoardException;
import gamecore.auxiliaryclasses.unitexception.UnitException;
import gamecore.gamelogic.Army;
import gamecore.gamelogic.Fields;
import gamecore.mathutils.Position;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStatisticsParser {
    @Test
    public void testParseGameLogInformation() throws UnitException, BoardException, IOException {
        final General generalPlayerOne = new General(GeneralTypes.ARCHMAGE);
        final Unit[][] armyPlayerOne = new Unit[2][3];
        armyPlayerOne[0][0] = new Unit(UnitTypes.HEALER); armyPlayerOne[1][0] = new Unit(UnitTypes.BOWMAN);
        armyPlayerOne[0][1] = new Unit(UnitTypes.HEALER); armyPlayerOne[1][1] = generalPlayerOne;
        armyPlayerOne[0][2] = new Unit(UnitTypes.BOWMAN); armyPlayerOne[1][2] = new Unit(UnitTypes.SWORDSMAN);
        final Army armyOne = new Army(armyPlayerOne, generalPlayerOne);
        final General generalPlayerTwo = new General(GeneralTypes.ARCHMAGE);
        Unit[][] armyPlayerTwo = new Unit[2][3];
        armyPlayerTwo[0][0] = new Unit(UnitTypes.BOWMAN); armyPlayerTwo[1][0] = new Unit(UnitTypes.BOWMAN);
        armyPlayerTwo[0][1] = generalPlayerTwo; armyPlayerTwo[1][1] = new Unit(UnitTypes.MAGE);
        armyPlayerTwo[0][2] = new Unit(UnitTypes.HEALER); armyPlayerTwo[1][2] = new Unit(UnitTypes.HEALER);
        final Army armyTwo = new Army(armyPlayerTwo, generalPlayerTwo);
        final Fields winner = Fields.PLAYER_TWO;
        int countOfRounds = 3;
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/teststatistics.csv"));
        GameLogInformation gameLog = StatisticsParser.
                parseGameLogInformation(reader);
        LogInformation log1 = new LogInformation(new Position(1,0,Fields.PLAYER_TWO),
                new Position(1,0,Fields.PLAYER_ONE),
                ActionTypes.RANGE_COMBAT, UnitTypes.BOWMAN, 70, UnitTypes.BOWMAN, 42, 28);
        LogInformation log2 = new LogInformation(new Position(0,1,Fields.PLAYER_TWO),
                new Position(0,2,Fields.PLAYER_ONE),
                ActionTypes.AREA_DAMAGE, UnitTypes.MAGE, 64, UnitTypes.SWORDSMAN, 0, 18);
        assertAll(
                ()-> assertEquals(armyOne, gameLog.getPlayerOneArmy()),
                ()-> assertEquals(armyTwo, gameLog.getPlayerTwoArmy()),
                ()-> assertEquals(winner.toString(), gameLog.getWinner()),
                ()-> assertEquals(countOfRounds, gameLog.getCountOfRounds()),
                ()-> assertEquals(21, gameLog.getLogList().size()),
                ()-> assertEquals(log1, gameLog.getLogList().get(3)),
                ()-> assertEquals(log2, gameLog.getLogList().get(20))
        );
        GameLogInformation gameLog2 = StatisticsParser.
                parseGameLogInformation(reader);
        GameLogInformation gameLog3 = StatisticsParser.
                parseGameLogInformation(reader);
        final List<GameLogInformation> fileInfo = StatisticsParser.parseLogFile("src/main/resources/teststatistics.csv");
        assertEquals(gameLog2, fileInfo.get(1));
        assertEquals(gameLog3, fileInfo.get(2));
        assertEquals(4, fileInfo.size());
    }
}
