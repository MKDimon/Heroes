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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class StatisticsParser {
    static final Logger logger = LoggerFactory.getLogger(StatisticsParser.class);

    /**
     * Метод считывает строчку из armystatistics и возвращает пару армий,
     * где на первом месте стоит победитель,  а на втором - проигравший
     **/

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
     * Парсер для строки с армией
     **/

    public static Army parseArmy(String[] armyStrings, int startPos) throws GameLogicException {
        if(armyStrings.length != 7){
            throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
        }
        try{
            startPos++;
            Unit[][] units = new Unit[2][3];
            General general = null;
            for(int i = 0; i < 2; i++){
                for(int j = 0; j < 3; j++){
                    String unitType = armyStrings[startPos];
                    if(StatisticsCollector.actToGeneralMap.containsValue(unitType)){
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

    /**
     * Метод для парсинга логов игры.
     **/

    public static List<LogInformation> parseLog(BufferedReader reader){
        List<LogInformation> result = new ArrayList<>(120);
        try{
            String log;
            while((log = reader.readLine()).startsWith("PLAYER")){
                String[] logString = log.split(",");
                result.add(new LogInformation(
                        new Position(Integer.parseInt(logString[1]),Integer.parseInt(logString[2]),Fields.valueOf(logString[0])),
                        new Position(Integer.parseInt(logString[4]),Integer.parseInt(logString[5]),Fields.valueOf(logString[3])),
                        ActionTypes.valueOf(logString[6]),
                        UnitTypes.valueOf(logString[7]), Integer.parseInt(logString[8]),
                        UnitTypes.valueOf(logString[9]),Integer.parseInt(logString[10]),
                        Integer.parseInt(logString[11])));
                //Можно добавить еще парсинг логов для массовых действий. Нужен класс наследник для LogInformation.
            }
        } catch (IOException | IllegalArgumentException e){
            logger.error("Error log parsing", e);
        }
        return result;
    }

    /**
     * Парсер для последних логов одной игры. Возвращает пару (PLAYER_WINNER, countOfRounds)
     * PLAYER_WINNER хранится в виде строки, т.к. возможна ничья
     */
    public static Pair<String, Integer> parseWinner(BufferedReader reader){
        try{
            String[] logString = reader.readLine().split(",");
            int countOfRounds = Integer.parseInt(logString[0]);
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
    /**
     * Метод считывает и парсит информацию об одной игре (От GAME START до GAME OVER)
     **/

    public static GameLogInformation parseGameLogInformation(BufferedReader reader) {
        try{
            String logLine = reader.readLine();
            if(logLine == null){
                throw new NullPointerException("Null line");
            }
            if(!logLine.equals("GAME START")){
                throw new IOException("Incorrect logs format");
            }
            String armyOneLine = reader.readLine();
            String armyTwoLine = reader.readLine();
            if(!armyOneLine.startsWith("PLAYER_ONE") || !armyTwoLine.startsWith("PLAYER_TWO")){
                throw new IOException("Incorrect logs format");
            }
            Army armyPlayerOne = parseArmy(armyOneLine.split(","), 0);
            Army armyPlayerTwo = parseArmy(armyTwoLine.split(","), 0);
            List<LogInformation> logList = parseLog(reader);
            Pair<String, Integer> winnerPair = parseWinner(reader);
            logLine = reader.readLine();
            if(!logLine.equals("GAME OVER")){
                throw new IOException("Incorrect logs format");
            }
            return new GameLogInformation(armyPlayerOne, armyPlayerTwo,logList,
                    winnerPair.getX(), winnerPair.getY());
        } catch (IOException | GameLogicException | NumberFormatException | NullPointerException e) {
            logger.error("Error game logs parsing", e);
            //Если встретился некорректно написанный лог, то метод передаст null.
            //Таким образом, отчистив результат от null`ов, получим набор распарсенных логов по всем играм.
            return null;
        }
    }

    /**
     * Метод считывает всю информацию из файла с CSV-логами
     * @return Список распарсенных игровых логов.
     **/
    public static List<GameLogInformation> parseLogFile(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            List<GameLogInformation> result = new LinkedList<>();
            while (reader.ready()) {
                result.add(parseGameLogInformation(reader));
            }
            return result.stream().filter(item -> item != null).collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error statistics file parsing", e);
            return null;
        }
    }
}
