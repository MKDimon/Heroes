package heroes.statistics;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.statisticsexception.StatisticsException;
import heroes.auxiliaryclasses.statisticsexception.StatisticsExceptionTypes;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс - парсер CSV логов.
 * Распарсенные данные записываются в специальный класс для хранения информации об играх.
 **/

public class StatisticsParser {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsParser.class);

    /**
     * Основной метод.
     * Считывает всю информацию из файла с CSV-логами
     *
     * @return Список распарсенных игровых логов.
     **/
    public static List<GameLogInformation> parseLogFile(final String filename) {
        try (final BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            final List<GameLogInformation> result = new LinkedList<>();
            while (reader.ready()) {
                result.add(parseGameLogInformation(reader));
            }
            //Возвращаем отчищенный от null`ов список распарсенных логов.
            return result.stream().filter(item -> item != null).collect(Collectors.toList());
        } catch (final IOException e) {
            logger.error("Error statistics file parsing", e);
            //Если файл с логами пуст, то вернется null => в рекордере
            //будем собирать в итоговый список только не null`овые списки.
            return null;
        }
    }

    /**
     * Метод считывает и парсит информацию об одной игре (От GAME START до GAME OVER)
     **/

    public static GameLogInformation parseGameLogInformation(final BufferedReader reader) {
        try {
            String logLine = reader.readLine();
            if (logLine == null) {
                throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
            }
            if (!logLine.equals("GAME START")) {
                throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
            }
            final String armyOneLine = reader.readLine();
            final String armyTwoLine = reader.readLine();
            if (!armyOneLine.startsWith("PLAYER_ONE") || !armyTwoLine.startsWith("PLAYER_TWO")) {
                throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
            }
            final Army armyPlayerOne = parseArmy(armyOneLine.split(","), 0);
            final Army armyPlayerTwo = parseArmy(armyTwoLine.split(","), 0);
            final List<LogInformation> logList = parseLog(reader);
            final Pair<String, Integer> winnerPair = parseWinner(reader);
            logLine = reader.readLine();
            if (!logLine.equals("GAME OVER")) {
                throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
            }
            return new GameLogInformation(armyPlayerOne, armyPlayerTwo, logList,
                    winnerPair.getX(), winnerPair.getY());
        } catch (final IOException | StatisticsException e) {
            logger.error("Error game logs parsing", e);
            //Если встретился некорректно написанный лог, то метод передаст null.
            //Таким образом, отчистив результат от null`ов, получим набор распарсенных логов по всем играм.
            return null;
        }
    }

    /**
     * Парсер для строки с армией
     **/

    private static Army parseArmy(final String[] armyStrings, int startPos) throws StatisticsException {
        if (armyStrings.length != 7) {
            throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
        }
        try {
            startPos++;
            final Unit[][] units = new Unit[2][3];
            General general = null;
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    String unitType = armyStrings[startPos];
                    if (StatisticsCollector.actToGeneralMap.containsValue(unitType)) {
                        general = new General(GeneralTypes.valueOf(unitType));
                        units[i][j] = general;
                    } else {
                        units[i][j] = new Unit(UnitTypes.valueOf(unitType));
                    }
                    startPos++;
                }
            }
            return new Army(units, general);
        } catch (final UnitException | BoardException e) {
            logger.error("Error army parsing", e);
            throw new StatisticsException(e);
        }
    }

    /**
     * Метод для парсинга логов игры.
     **/

    private static List<LogInformation> parseLog(final BufferedReader reader) throws StatisticsException {
        final List<LogInformation> result = new ArrayList<>(120);
        try {
            String log;
            while ((log = reader.readLine()).startsWith("PLAYER")) {
                final String[] logString = log.split(",");
                result.add(new LogInformation(
                        new Position(Integer.parseInt(logString[1]), Integer.parseInt(logString[2]), Fields.valueOf(logString[0])),
                        new Position(Integer.parseInt(logString[4]), Integer.parseInt(logString[5]), Fields.valueOf(logString[3])),
                        ActionTypes.valueOf(logString[6]),
                        UnitTypes.valueOf(logString[7]), Integer.parseInt(logString[8]),
                        UnitTypes.valueOf(logString[9]), Integer.parseInt(logString[10]),
                        Integer.parseInt(logString[11])));
            }
            return result;
        } catch (final IOException | IllegalArgumentException e) {
            logger.error("Error log parsing", e);
            throw new StatisticsException(e);
        }
    }

    /**
     * Парсер для последних логов одной игры. Возвращает пару (PLAYER_WINNER, countOfRounds)
     * PLAYER_WINNER хранится в виде строки, т.к. возможна ничья
     */

    private static Pair<String, Integer> parseWinner(final BufferedReader reader) throws StatisticsException {
        try {
            final String[] logString = reader.readLine().split(",");
            final int countOfRounds = Integer.parseInt(logString[0]);
            if(!logString[1].startsWith("PLAYER_") && !logString[1].equals("DEAD HEAT")){
                throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
            }
                return new Pair<>(logString[1], countOfRounds);
        } catch (final IOException | IllegalArgumentException e) {
            logger.error("Error winner parsing", e);
            throw new StatisticsException(e);
        }
    }

    /**
     * Далее набор методов для парсинга файлов с обработанной статистикой
     **/

    public static Map<Army, Integer[]> parseArmiesStatisticsFile(){
        try(final BufferedReader reader = new BufferedReader(
                new FileReader(StatisticsRecorder.armiesStatisticsFilename))){
            final Map<Army, Integer[]> result = new HashMap<>();
            while(reader.ready()){
                final String[] line = reader.readLine().split(",");
                result.put(parseArmy(line, 0), new Integer[]{Integer.valueOf(line[6]),
                        Integer.valueOf(line[7]), Integer.valueOf(line[8])});
            }
            return result;
        } catch (IOException | StatisticsException e){
            logger.error("Error armiesStatistics file parsing", e);
            return null;
        }
    }

    public static double parseGameDurationStatisticsFile(){
        try(final BufferedReader reader = new BufferedReader(
                new FileReader(StatisticsRecorder.gameDurationStatisticsFilename))){
            return Double.parseDouble(reader.readLine());
        } catch (IOException | NumberFormatException e){
            logger.error("Error gameDurationStatistics file parsing", e);
            return 0;
        }
    }
}
