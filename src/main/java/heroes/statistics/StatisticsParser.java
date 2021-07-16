package heroes.statistics;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.GameLogicExceptionType;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.mathutils.Pair;
import heroes.mathutils.Position;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StatisticsParser {
    static final Logger logger = LoggerFactory.getLogger(StatisticsParser.class);

    /**
     * Метод считывает строчку из armystatistics и возвращает пару армий,
     * где на первом месте стоит победитель,  а на втором - проигравший
     **/

    public static Army parseArmy(String[] armyStrings, int startPos) throws GameLogicException {
        if(armyStrings.length != 6){
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
        try{
            Unit[][] units = new Unit[2][3];
            int genX = 0;
            int genY = 0;
            General general = null;
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < 3; j++){
                    String unitType = armyStrings[startPos];
                    if(StatisticsCollector.actToGeneralMap.values().contains(unitType)){
                        general = new General(GeneralTypes.valueOf(unitType));
                        units[i][j] = general;
                    } else {
                        units[i][j] = new Unit(UnitTypes.valueOf(unitType));
                    }
                    startPos++;
                }
            }
            return new Army(units, general);
        } catch (UnitException | BoardException e) {
            logger.error("Error army parsing", e);
            return null;
        }
    }

    public static Pair<Army, Army> parseArmies(){
        try(BufferedReader reader = new BufferedReader(new FileReader(StatisticsCollector.armyStatisticsFilename))){
            String[] statisticsLine = reader.readLine().split(",");
            Fields winner = Fields.valueOf(statisticsLine[statisticsLine.length-1]);
            int winnerPos = Arrays.binarySearch(Arrays.copyOfRange(statisticsLine,0,statisticsLine.length-1),
                    winner.toString());
            if (winnerPos == 0){
                Army winnerArmy = new Army(parseArmy(Arrays.copyOfRange(statisticsLine, 1, 7), 1));
                Army loserArmy = new Army(parseArmy(Arrays.copyOfRange(statisticsLine, 7,
                        statisticsLine.length-1), 7));
                return new Pair<>(winnerArmy, loserArmy);
            } else {
                Army winnerArmy = new Army(parseArmy(Arrays.copyOfRange(statisticsLine, 1, 7), 1));
                Army loserArmy = new Army(parseArmy(Arrays.copyOfRange(statisticsLine, 7,
                        statisticsLine.length-1), 7));
                return new Pair<>(winnerArmy, loserArmy);
            }
        } catch (IOException | GameLogicException | UnitException | BoardException e) {
            logger.error("Error armystatistics parsing", e);
        }
        return null;
    }

    /**
     * Далее - набор методов для парсинга логов игры.
     *
     **/

    public static List<LogInformation> parseLog(String filename){
        List<LogInformation> result = new ArrayList<>(120);
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String log;
            while((log = reader.readLine()).startsWith("PLAYER")){
                String[] logString = log.split(",");
                result.add(new LogInformation(
                        new Position(Integer.valueOf(logString[1]),Integer.valueOf(logString[2]),Fields.valueOf(logString[0])),
                        new Position(Integer.valueOf(logString[4]),Integer.valueOf(logString[5]),Fields.valueOf(logString[3])),
                        ActionTypes.valueOf(logString[6]),
                        UnitTypes.valueOf(logString[7]), Integer.valueOf(logString[8]),
                        UnitTypes.valueOf(logString[9]),Integer.valueOf(logString[10]),
                        Integer.valueOf(logString[11])));
                //Можно добавить еще парсинг логов для массовых действий. Нужен класс наследник для LogInformation.
            }
        } catch (IOException | IllegalArgumentException e){
            logger.error("Error log parsing", e);
        }
        return result;
    }

    /**
     * Прасер для последних логов одной игры. Возвращает пару (PLAYER_WINNER, countOfRounds)
     * PLAYER_WINNER хранится в виде строки, т.к. возможна ничья
     */
    public static Pair<String, Integer> parseWinner(String filename){
        try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
            String[] logString = reader.readLine().split(",");
            int countOfRounds = Integer.valueOf(logString[0]);
            if(countOfRounds != 10){
                return new Pair<>(logString[1], countOfRounds);
            } else {
                return new Pair<>("DEAD HEAT", 10);
            }
        } catch (IOException | IllegalArgumentException e){
            logger.error("Error winner parsing", e);
            return null;
        }
    }



}
