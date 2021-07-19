package heroes.statistics;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestStatisticsParser {
    @Test
    public void testParseGameLogInformation() throws UnitException, GameLogicException, BoardException, IOException {
        General generalPlayerOne = new General(GeneralTypes.SNIPER);
        Unit[][] armyPlayerOne = new Unit[2][3];
        armyPlayerOne[0][0] = new Unit(UnitTypes.MAGE); armyPlayerOne[1][0] = new Unit(UnitTypes.SWORDSMAN);
        armyPlayerOne[0][1] = new Unit(UnitTypes.BOWMAN); armyPlayerOne[1][1] = new Unit(UnitTypes.MAGE);
        armyPlayerOne[0][2] = generalPlayerOne; armyPlayerOne[1][2] = new Unit(UnitTypes.BOWMAN);
        Army armyOne = new Army(armyPlayerOne, generalPlayerOne);
        General generalPlayerTwo = new General(GeneralTypes.ARCHMAGE);
        Unit[][] armyPlayerTwo = new Unit[2][3];
        armyPlayerTwo[0][0] = new Unit(UnitTypes.SWORDSMAN); armyPlayerTwo[1][0] = new Unit(UnitTypes.BOWMAN);
        armyPlayerTwo[0][1] = new Unit(UnitTypes.MAGE); armyPlayerTwo[1][1] = new Unit(UnitTypes.MAGE);
        armyPlayerTwo[0][2] = generalPlayerTwo; armyPlayerTwo[1][2] = new Unit(UnitTypes.BOWMAN);
        Army armyTwo = new Army(armyPlayerTwo, generalPlayerTwo);
        Fields winner = Fields.PLAYER_TWO;
        int countOfRounds = 2;
        BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/statistics/teststatistics.csv"));
        GameLogInformation gameLog = StatisticsParser.
                parseGameLogInformation(reader);
        LogInformation log1 = new LogInformation(new Position(0,2,Fields.PLAYER_ONE), new Position(1,0,Fields.PLAYER_TWO),
                ActionTypes.RANGE_COMBAT, UnitTypes.BOWMAN, 50, UnitTypes.BOWMAN, 38, 20);
        LogInformation log2 = new LogInformation(new Position(1,0,Fields.PLAYER_ONE), new Position(0,1,Fields.PLAYER_TWO),
                ActionTypes.CLOSE_COMBAT, UnitTypes.SWORDSMAN, 3, UnitTypes.MAGE, -8, 27);
        assertAll(
                ()-> assertEquals(armyOne, gameLog.getPlayerOneArmy()),
                ()-> assertEquals(armyTwo, gameLog.getPlayerTwoArmy()),
                ()-> assertEquals(winner, gameLog.getWinner()),
                ()-> assertEquals(countOfRounds, gameLog.getCountOfRounds()),
                ()-> assertEquals(14, gameLog.getLogList().size()),
                ()-> assertEquals(log1, gameLog.getLogList().get(2)),
                ()-> assertEquals(log2, gameLog.getLogList().get(12))
        );
        GameLogInformation gameLog2 = StatisticsParser.
                parseGameLogInformation(reader);
        GameLogInformation gameLog3 = StatisticsParser.
                parseGameLogInformation(reader);
        List<GameLogInformation> fileInfo = StatisticsParser.parseLogFile("src/main/resources/statistics/teststatistics.csv");
        assertEquals(gameLog2, fileInfo.get(1));
        assertEquals(gameLog3, fileInfo.get(2));
        assertEquals(3, fileInfo.size());
    }
}
