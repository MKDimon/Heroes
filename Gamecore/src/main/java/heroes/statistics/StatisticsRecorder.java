package heroes.statistics;


import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.auxiliaryclasses.statisticsexception.StatisticsException;
import heroes.clientserver.ServersConfigs;
import heroes.gamelogic.Army;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Класс, в котором происходит обработка всей статистики и запись ее в отдельынй файл.
 **/

public class StatisticsRecorder {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsRecorder.class);

    public static final String armiesStatisticsFilename = "armiesStatistics";
    public static final String gameDurationStatisticsFilename = "gameDurationStatistics";
    public static final String winnerUnitsStatisticsFilename = "winnerUnitsStatistics";
    public static final String botsStatisticsFilename = "botsStatistics.csv";


    public static void main(final String[] args) {
        recordStatistics();
    }

    /**
     * Парсит serverConfig.json из каталога и возвращает конфиги сервера
     *
     * @return все нужные конфиги
     * @throws IOException json
     */
    public static ServersConfigs getServersConfig() throws IOException {
        final FileInputStream fileInputStream = new FileInputStream("serverConfig.json");

        final ServersConfigs sc = new ObjectMapper().readValue(fileInputStream, ServersConfigs.class);
        fileInputStream.close();
        return sc;
    }

    /**
     * Основной метод.
     * Обрабатывает и записывает всю имеющуюся статистику.
     **/
    public static void recordStatistics() {
        try {
            final ServersConfigs sc = getServersConfig();
            final List<GameLogInformation> games = new LinkedList<>();
            //Собираем данные со всех файлов в список games
            for (int id = 1; id <= sc.MAX_ROOMS; id++) {
                final String filename = new StringBuilder(sc.LOGBACK).append("/").
                        append(StatisticsCollector.filenameTemplate).append(id).
                        append(".csv").toString();
                final List<GameLogInformation> oneFileLogs = StatisticsParser.parseLogFile(filename);
                if (oneFileLogs != null) {
                    games.addAll(oneFileLogs);
                }
            }
            if (games.isEmpty()) {
                return;
            }
            //Записывем статистку о  составах армии, количестве их побед, поражений, ничьих
            recordArmiesStatisticsToCSV(games, new StringBuilder(sc.LOGBACK).append("/").
                    append(armiesStatisticsFilename).toString());
            //Записываем статистику о характеристиках юнитов и продолжительности игры
            recordGameDurationStatisticsToCSV(games, new StringBuilder(sc.LOGBACK).append("/").
                    append(gameDurationStatisticsFilename).toString());
            //Записываем статистику о юнитах победителей
            recordWinnerUnitsStatisticsToCSV(games, new StringBuilder(sc.LOGBACK).append("/").
                    append(winnerUnitsStatisticsFilename).toString());

            //Записываем статистику по ботам
            recordBotsStatistics();

            //Далее при появлении новых методов в аналитике статистики просто допишем их сюда.
            //Таким образом, при вызове этого метода будет собираться и анализироваться вся имеющаяся статистика.
        } catch (IOException e) {
            logger.error("Error statistics recording", e);
        }
    }

    /**
     * Обрабатывает и записывает статистику об армиях (победы, поражения, ничьи) в отдельынй файл.
     **/

    public static void recordArmiesStatisticsToCSV(List<GameLogInformation> games,
                                                   final String filename) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            final Map<Army, Integer[]> armiesStatistics = StatisticsAnalyzer.analyzeArmiesStatistics(games);
            for (Army army : armiesStatistics.keySet()) {
                final StringBuilder record = new StringBuilder();
                final Unit[][] unitArray = army.getPlayerUnits();
                for (final Unit[] units : unitArray) {
                    for (final Unit unit : units) {
                        if (unit.equals(army.getGeneral())) {
                            record.append(StatisticsCollector.actToGeneralMap.get(unit.getActionType())).append(",");
                        } else {
                            record.append(StatisticsCollector.actToUnitMap.get(unit.getActionType())).append(",");
                        }
                    }
                }
                record.append(armiesStatistics.get(army)[0]).append(",").
                        append(armiesStatistics.get(army)[1]).append(",").
                        append(armiesStatistics.get(army)[2]).append("\n");
                writer.write(record.toString());
                writer.flush();
            }
        } catch (IOException | StatisticsException e) {
            logger.error("Error armies statistics recording", e);
        }
    }

    /**
     * Метод записывает характеристики юнитов и генералов и среднюю продолжительность игры в формате
     * averageGameDuration
     * unitType,hp,armor,power,accuracy
     * ...
     * generalType,armorBonus,powerBonus,accuracyBonus
     * ...
     **/

    public static void recordGameDurationStatisticsToCSV(final List<GameLogInformation> games,
                                                         final String filename) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.valueOf(StatisticsAnalyzer.getAverageGameDuration(games)));
            writer.write("\n");
            for (final UnitTypes unit : UnitTypes.values()) {
                writer.write(new StringBuilder(unit.toString()).append(",").append(unit.HP).
                        append(",").append(unit.armor).append(",").append(unit.power).
                        append(",").append(unit.accuracy).append("\n").toString());
            }
            for (final GeneralTypes general : GeneralTypes.values()) {
                writer.write(new StringBuilder(general.toString()).append(",").
                        append(general.inspirationArmorBonus).append(",").
                        append(general.inspirationDamageBonus).append(",").
                        append(general.inspirationAccuracyBonus).append("\n").toString());
            }
            writer.flush();
        } catch (IOException | StatisticsException e) {
            logger.error("Error game duration statistics recording", e);
        }
    }

    /**
     * Метод записывает Map с содержанием
     * unitType -> avXPos,avYPos,avActionsCount,avActionPower,unitCount (сколько раз встретился
     * в армиях победителей)
     * в файл в формате CSV
     **/

    public static void recordWinnerUnitsStatisticsToCSV(final List<GameLogInformation> games,
                                                        final String filename) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            final Map<UnitTypes, Double[]> unitsStatistics = StatisticsAnalyzer.getWinnerUnitsStatistics(games);
            for (UnitTypes unitType : unitsStatistics.keySet()) {
                final StringBuilder record = new StringBuilder();
                record.append(unitType.toString());
                for (int i = 0; i < unitsStatistics.get(unitType).length; i++) {
                    record.append(",").append(unitsStatistics.get(unitType)[i]);
                }
                record.append("\n");
                writer.write(record.toString());
                writer.flush();
            }

        } catch (IOException | StatisticsException e) {
            logger.error("Error game duration statistics recording", e);
        }
    }

    /**
     * Основной метод для сичтывания и обработки статистики по ботам. Выводит сводные данные в файл
     * botsStatisticsFilename.
     **/

    public static void recordBotsStatistics() {
        try {
            final ServersConfigs sc = getServersConfig();
            final List<BotsLogInformation> logs = new LinkedList<>();
            //Собираем данные со всех файлов в список games
            for (int id = 1; id <= sc.MAX_ROOMS; id++) {
                final String filename = new StringBuilder(sc.PATH_LOG).append("/").
                        append(StatisticsCollector.playersStatisticsFilenameTemplate).append(id).
                        append(".csv").toString();
                final List<BotsLogInformation> oneFileLogs = StatisticsParser.parseBotsStatisticsFile(filename);
                if (oneFileLogs != null) {
                    logs.addAll(oneFileLogs);
                }
            }
            if (logs.isEmpty()) {
                return;
            }

            recordBotsStatisticsToCSV(StatisticsAnalyzer.countPlayersStatistics(logs),
                    new StringBuilder(sc.PATH_LOG).append("/").append(botsStatisticsFilename).toString());
        } catch (final IOException e) {
            logger.error("Error bots statistics recording", e);
        }
    }

    /**
     * Мето записывает подсчитанную статистику по играм ботов в формате
     * bot1name,bot1wins,bot2name,bot2wins,draws
     **/

    public static void recordBotsStatisticsToCSV(final List<TwoPlayersStatistics> list,
                                                 final String filename) {
        try (final BufferedWriter writer = new BufferedWriter(
                new FileWriter(filename))) {
            for (final TwoPlayersStatistics playersStats : list) {
                final StringBuilder record = new StringBuilder();
                record.append(playersStats.getPlayerOne()).append(",")
                        .append(playersStats.getPlayerOneWins()).append(",")
                        .append(playersStats.getPlayerTwo()).append(",").append(playersStats.getPlayerTwoWins())
                        .append(",").append(playersStats.getDraws()).append("\n");
                writer.write(record.toString());
            }
            writer.flush();
        } catch (final IOException e) {
            logger.error("Error bots statistics recording", e);
        }
    }
}
