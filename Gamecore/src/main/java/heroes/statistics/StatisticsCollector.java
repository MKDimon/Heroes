package heroes.statistics;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.clientserver.ServersConfigs;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.mathutils.Position;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Классы для сбора статистики:
 * Коллектор - собирает данные в файл
 * Парсер - разбирает данные
 * Анализатор - анализирует данные.
 * Рекордер - записывает обработанные данные в соответтсвующие файлы
 * Логи одной игры имеют следующую структуру:
 * GAME START
 * PLAYER_ONE,ARMY
 * PLAYER_TWO,ARMY
 * ...
 * 0           1   2       3      4     5        6         7             8       9            10     11
 * PLAYER_ATTACK,atX,atY,PLAYER_DEF,defX,defY,actionType,attackerUnitType,atHP,defenderUnitType,defHP,actionPower
 * ...
 * *пустая строка*
 * lastRoundNumber,PLAYER_WINNER
 * GAME OVER
 **/

public class StatisticsCollector {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsCollector.class);
    public static final Map<ActionTypes, String> actToUnitMap = Map.of(
            ActionTypes.CLOSE_COMBAT, UnitTypes.SWORDSMAN.toString(),
            ActionTypes.RANGE_COMBAT, UnitTypes.BOWMAN.toString(),
            ActionTypes.AREA_DAMAGE, UnitTypes.MAGE.toString(),
            ActionTypes.HEALING, UnitTypes.HEALER.toString());
    public static final Map<ActionTypes, String> actToGeneralMap = Map.of(
            ActionTypes.CLOSE_COMBAT, GeneralTypes.COMMANDER.toString(),
            ActionTypes.RANGE_COMBAT, GeneralTypes.SNIPER.toString(),
            ActionTypes.AREA_DAMAGE, GeneralTypes.ARCHMAGE.toString());
    public static final String filenameTemplate = "gameStatistics";
    public static final String playersStatisticsFilenameTemplate = "players_statistics";

    private final String filename;

    private final String playersStatisticsFilename;

    public static ServersConfigs getServersConfig() {
        final FileInputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream("serverConfig.json");
            final ServersConfigs sc = new ObjectMapper().readValue(fileInputStream, ServersConfigs.class);
            fileInputStream.close();
            return sc;
        } catch (IOException e) {
            logger.error("Cannot open configs in StatisticsCollector", e);
            return null;
        }
    }
    public StatisticsCollector(final int fileID) {
        filename = new StringBuilder(getServersConfig().LOGBACK).append("/").append(filenameTemplate).
                append(fileID).append(".csv").toString();
        playersStatisticsFilename = new StringBuilder(getServersConfig().PATH_LOG).append("/").
                append(playersStatisticsFilenameTemplate).
                append(fileID).append(".csv").toString();
    }

    /**
     * Метод записывает состав армий игроков
     * playerField,unit00,unit01,unit02,unit10,unit11,unit12
     * Если на ij-ом месте встертился генерал, то указывается тип генерала
     **/

    public void recordArmyToCSV(final Fields field, final Army army) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            final StringBuffer record = new StringBuffer();
            record.append(field).append(",").append(armyToCSVString(army));
            record.delete(record.length() - 1, record.length()).append("\n");
            writer.write(record.toString());
        } catch (final IOException e) {
            logger.error("Error army recording", e);
        }
    }

    public void recordPlayersToCSV(final String playerOneName, final Army armyOne,
                                   final String playerTwoName, final Army armyTwo) {
        try (final BufferedWriter writer = new BufferedWriter(
                new FileWriter(playersStatisticsFilename, true))){
            final StringBuilder record = new StringBuilder();
            record.append(playerOneName).append(",").append(armyToCSVString(armyOne))
                    .append(playerTwoName).append(",").append(armyToCSVString(armyTwo));
            writer.write(record.toString());
            writer.flush();
        } catch (final IOException e) {
            logger.error("Error recording players to CSV", e);
        }
    }

    public void recordPlayerToCSVForDB(final String playerName, final String playerBotType,
                                       final Army army) {
        try (final BufferedWriter writer = new BufferedWriter(
                new FileWriter(filename, true))){
            final StringBuilder record = new StringBuilder();
            record.append(playerName).append(",")
                    .append(playerBotType).append(",")
                    .append(armyToCSVString(army)).append("\n");
            writer.write(record.toString());
            writer.flush();
        } catch (final IOException e) {
            logger.error("Error recording players to CSV", e);
        }
    }

    public void recordDateToCSV() {
        try (final BufferedWriter writer = new BufferedWriter(
                new FileWriter(filename, true))){
            final StringBuilder record = new StringBuilder();
            final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            record.append(dateFormat.format(date)).append("\n");
            writer.write(record.toString());
            writer.flush();
        } catch (final IOException e) {
            logger.error("Error recording players to CSV", e);
        }
    }

    private String armyToCSVString(final Army army) {
        final StringBuilder record = new StringBuilder();
        final Unit[][] unitArray = army.getPlayerUnits();
        for (Unit[] units : unitArray) {
            for (Unit unit : units) {
                if (unit.equals(army.getGeneral())) {
                    record.append(actToGeneralMap.get(unit.getActionType())).append(",");
                } else {
                    record.append(actToUnitMap.get(unit.getActionType())).append(",");
                }
            }
        }
        return record.toString();
    }

    /**
     * Метод записывает информацию о ходе текущего игрока в формате
     * PLAYER_ATTACK,atX,atY,PLAYER_DEF,defX,defY,actionType,attackerUnitType,atHP,defenderUnitType,defHP,actionPower
     * Замечание. Здоровье юнита записывается ПОСЛЕ примененного действия.
     *
     * @param attackPos - позиция атакующего юнита
     * @param defPos    - позиция защищающегося юнита
     * @param actType   - тип действия
     * @param attacker  - кем ходит
     * @param defender  - на кого ходит
     * @param actPower  - с какой силой ходит
     **/

    public void recordActionToCSV(final Position attackPos, final Position defPos, ActionTypes actType,
                                  final Unit attacker, final Unit defender, int actPower) {
        if (actType == ActionTypes.DEFENSE) {
            actPower = defender.getArmor();
        }
        final StringBuilder record = new StringBuilder();
        record.append(attackPos.F().toString()).append(",").append(attackPos.X()).append(",").
                append(attackPos.Y()).append(",").append(defPos.F().toString()).append(",").
                append(defPos.X()).append(",").append(defPos.Y()).append(",").append(actType.toString()).append(",").
                append(actToUnitMap.get(attacker.getActionType())).append(",").
                append(attacker.getCurrentHP()).append(",").
                append(actToUnitMap.get(defender.getActionType())).append(",").
                append(defender.getCurrentHP()).append(",").append(actPower).append("\n");
        recordMessageToCSV(record.toString());
    }

    /**
     * Записывает сообщение в файл filename. Нужно для записи CSV-логов.
     **/

    public void recordMessageToCSV(final String message) {
        recordMessageToCSV(message, filename);
    }

    /**
     * Записывает сообщение в файл outputFilename. Нужно в общем случае.
     **/

    public void recordMessageToCSV(final String message, final String outputFilename) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename, true))) {
            writer.write(message);
        } catch (final IOException e) {
            logger.error("Error action recording", e);
        }
    }

    public String getPlayersStatisticsFilename() {
        return playersStatisticsFilename;
    }

}