package heroes.statistics;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Класс - сборщик статистики.
 * */

public class StatisticsCollector {
    static final Logger logger = LoggerFactory.getLogger(StatisticsCollector.class);
    public static final String actionStatisticsFilename = "src/main/resources/statistics/actionstatistics.csv";
    public static final String armyStatisticsFilename = "src/main/resources/statistics/armystatistics.csv";
    static final Map<ActionTypes, String> actToUnitMap = Map.of(
            ActionTypes.CLOSE_COMBAT, UnitTypes.SWORDSMAN.toString(),
            ActionTypes.RANGE_COMBAT, UnitTypes.BOWMAN.toString(),
            ActionTypes.AREA_DAMAGE, UnitTypes.MAGE.toString(),
            ActionTypes.HEALING, UnitTypes.HEALER.toString());
    static final Map<ActionTypes, String> actToGeneralMap = Map.of(
            ActionTypes.CLOSE_COMBAT, GeneralTypes.COMMANDER.toString(),
            ActionTypes.RANGE_COMBAT, GeneralTypes.SNIPER.toString(),
            ActionTypes.AREA_DAMAGE, GeneralTypes.ARCHMAGE.toString());

    public static void recordWinnerToCSV(final Fields field, final String filename){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))){
            writer.write(field.toString());
            writer.write("\n");
        } catch (IOException e){
            logger.error("Error winner recording", e);
        }
    }

    /**
     * Метод записывает состав армий игроков
     * playerField,unit00,unit01,unit02,unit10,unit11,unit12
     * Если на ij-ом месте встертился генерал, то указывается тип генерала
     **/

    public static void recordArmyToCSV(final Fields field, final Army army, final String filename){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))){
            StringBuffer record = new StringBuffer();
            record.append(field).append(",");
            Unit[][] unitArray = army.getPlayerUnits();
            for (Unit[] units : unitArray) {
                for (Unit unit : units) {
                    if(unit.equals(army.getGeneral())){
                        record.append(actToGeneralMap.get(unit.getActionType())).append(",");
                    } else {
                        record.append(actToUnitMap.get(unit.getActionType())).append(",");
                    }
                }
            }
            record.append("\n");
            writer.write(record.toString());
        } catch (IOException | UnitException e){
            logger.error("Error army recording", e);
        }
    }

    /**
     * Метод записывает информацию о ходе текущего игрока в формате
     * attackerUnitType,attackerHP,defenderUnitType,defenderHP,actPower
     * Замечание. Здоровье юнита записывается ПОСЛЕ примененного действия.
     * @param attacker - кем ходит
     * @param defender - на кого ходит
     * @param actPower - с какой силой ходит
     * @param filename - куда записывать
     **/

    public static void recordActionToCSV(final Unit attacker, final Unit defender,
                                         final int actPower, final String filename){
            StringBuffer record = new StringBuffer();
            record.append(actToUnitMap.get(attacker.getActionType())).append(",").
                    append(attacker.getCurrentHP()).append(",");
            record.append(actToUnitMap.get(defender.getActionType())).append(",").
                    append(defender.getCurrentHP()).append(",");
            record.append(actPower).append(",");
            recordMessageToCSV(record.toString(), filename);
    }

    /**
     * Записывает сообщение в файл
     **/

    public static void recordMessageToCSV(final String message, final String filename){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))){
            writer.write(message);
        } catch (IOException e){
            logger.error("Error action recording", e);
        }
    }

    /**
     * Далее набор классов, которые выводят обработанную статистику в файл
     **/

    public static void recordArmiesStatisticsToCSV(final Map<Army, Integer[]> armiesStatistics, final String filename){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            for(Army army:armiesStatistics.keySet()){
                StringBuffer record = new StringBuffer();
                Unit[][] unitArray = army.getPlayerUnits();
                for (Unit[] units : unitArray) {
                    for (Unit unit : units) {
                        if (unit.equals(army.getGeneral())) {
                            record.append(actToGeneralMap.get(unit.getActionType())).append(",");
                        } else {
                            record.append(actToUnitMap.get(unit.getActionType())).append(",");
                        }
                    }
                }
                record.append(armiesStatistics.get(army)[0]).append(",").
                        append(armiesStatistics.get(army)[1]).append(",").
                        append(armiesStatistics.get(army)[2]).append("\n");
                writer.write(record.toString());
            }
        } catch (IOException | UnitException e) {
            logger.error("Error armies statistics recording", e);
        }
    }
}

/**
 * Классы для сбора статистики:
 * Коллектор - собирает данные в файл
 * Парсер - разбирает данные
 * Анализатор - анализирует данные.
 *
 * Логи одной игры имеют следующую структуру:
 * GAME START
 * PLAYER_ONE ARMY
 * PLAYER_TWO ARMY
 * ...
 *    0           1   2       3        4   5      6         7              8       9             10     11
 * PLAYER_ATTACK,atX,atY,PLAYER_DEF,defX,defY,actionType,attackerUnitType,atHP,defenderUnitType,defHP,actionPower,
 * ...
 * *пустая строка*
 * lastRoundNumber,PLAYER_WINNER
 * GAME OVER
 **/