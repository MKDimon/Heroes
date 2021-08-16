package heroes.statistics;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.mathutils.Position;
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
    public static final String filenameTemplate = "statistics/gameStatistics";

    private final String filename;

    public StatisticsCollector(final int fileID) {
        filename = new StringBuffer(filenameTemplate).
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
            record.append(field).append(",");
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
            record.delete(record.length() - 1, record.length()).append("\n");
            writer.write(record.toString());
        } catch (final IOException e) {
            logger.error("Error army recording", e);
        }
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
        final StringBuffer record = new StringBuffer();
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
     * Записывает сообщение в файл
     **/

    public void recordMessageToCSV(final String message) {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(message);
        } catch (final IOException e) {
            logger.error("Error action recording", e);
        }
    }

}