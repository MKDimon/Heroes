package heroes.statistics;


import heroes.auxiliaryclasses.statisticsexception.StatisticsException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.Deserializer;
import heroes.clientserver.ServersConfigs;
import heroes.gamelogic.Army;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
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

    public static final String armiesStatisticsFilename = "src/main/resources/statistics/armiesStatistics";

    public static void main(String[] args) {
        recordStatistics();
    }

    /**
     * Основной метод.
     * Обрабатывает и записывает всю имеющуюся статистику.
     **/

    public static void recordStatistics(){
        try {
            final ServersConfigs sc = new ServersConfigs(Deserializer.getConfig().PORT, Deserializer.getConfig().MAX_ROOMS);
            final List<GameLogInformation> games = new LinkedList<>();
            //Собираем данные со всех файлов в список games
            for(int id = 0; id < sc.MAX_ROOMS; id++){
                final String filename = new StringBuilder(StatisticsCollector.filenameTemplate).append(id).
                        append(".csv").toString();
                final List<GameLogInformation> oneFileLogs = StatisticsParser.parseLogFile(filename);
                if(oneFileLogs != null) {
                    games.addAll(oneFileLogs);
                }
            }
            if(games.isEmpty()){
                return;
            }
            //Записывем статистку о  составах армии, количестве их побед, поражений, ничьих
            recordArmiesStatisticsToCSV(games, armiesStatisticsFilename);
            //Далее при появлении новых методов в аналитике статистики просто допишем их сюда.
            //Таким образом, при вызове этого метода будет собираться и анализироваться вся имеющаяся статистика.
        } catch (IOException  e) {
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
                Unit[][] unitArray = army.getPlayerUnits();
                for (Unit[] units : unitArray) {
                    for (Unit unit : units) {
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
            }
        } catch (IOException | UnitException | StatisticsException e) {
            logger.error("Error armies statistics recording", e);
        }
    }
}
