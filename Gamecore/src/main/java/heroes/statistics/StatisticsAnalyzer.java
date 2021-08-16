package heroes.statistics;


import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.statisticsexception.StatisticsException;
import heroes.auxiliaryclasses.statisticsexception.StatisticsExceptionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.mathutils.Pair;
import heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс для анализа статистики
 **/

public class StatisticsAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(StatisticsAnalyzer.class);

    /**
     * Метод собирает статистику по всем играм. Для каждой армии считает количетсво побед, поражений, ничьих.
     **/

    public static Map<Army, Integer[]> analyzeArmiesStatistics(final List<GameLogInformation> games) throws StatisticsException {
        try {
            if (games == null || games.isEmpty()) {
                throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
            }
            final Map<Army, Integer[]> result = new HashMap<>();
            for (GameLogInformation gameLog : games) {
                result.put(gameLog.getPlayerOneArmy(), getArmyStatistics(gameLog.getPlayerOneArmy(), games));
                result.put(gameLog.getPlayerTwoArmy(), getArmyStatistics(gameLog.getPlayerTwoArmy(), games));
            }
            return result;
        } catch (final StatisticsException | BoardException | UnitException e) {
            logger.error("Error army statistics counting", e);
            throw new StatisticsException(e);
        }

    }

    /**
     * Метод для даннй армии подсчитывает количетсво побед, поражений, ничих, исходя из данных обо всех играх.
     *
     * @param army  - армия, для которой собирается статистика
     * @param games - список всех игр
     * @return Массив целых чисел, где на 0-ом месте - количетсво побед,
     * на 1-ом месте - колчиество поражений,
     * на 2-ом месте количетсво ничьих.
     **/
    public static Integer[] getArmyStatistics(final Army army, final List<GameLogInformation> games) throws BoardException, UnitException, StatisticsException {
        if (army == null || games == null || games.isEmpty()) {
            throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
        }
        final Integer[] statistics = new Integer[3];
        Arrays.fill(statistics, 0);
        for (GameLogInformation game : games) {
            final Army playerOneArmy = game.getPlayerOneArmy();
            final Army playerTwoArmy = game.getPlayerTwoArmy();
            final boolean isArmyOne = playerOneArmy.equals(army);
            final boolean isArmyTwo = playerTwoArmy.equals(army);
            if (isArmyOne) {
                switch (game.getWinner()) {
                    case "PLAYER_ONE" -> statistics[0]++;
                    case "PLAYER_TWO" -> statistics[1]++;
                    case "DRAW" -> statistics[2]++;
                }
            }
            if (isArmyTwo) {
                switch (game.getWinner()) {
                    case "PLAYER_ONE" -> statistics[1]++;
                    case "PLAYER_TWO" -> statistics[0]++;
                    case "DRAW" -> statistics[2]++;
                }
            }
        }
        return statistics;
    }

    /**
     * Метод возвращает среднюю продолжительность игры (в раундах)
     **/

    public static double getAverageGameDuration(final List<GameLogInformation> games) throws StatisticsException {
        try {
            if (games == null || games.isEmpty()) {
                throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
            }
            int count = 0;
            for (GameLogInformation game : games) {
                count += game.getCountOfRounds();
            }
            return (double) count / games.size();
        } catch (final StatisticsException e) {
            logger.error("Error average game duration analyzing", e);
            throw new StatisticsException(e);
        }
    }

    /**
     * Метод анализирует ходы победивших игроков (по всех играм). Возвращает Map с соедржанием
     * unitType -> avXPos,avYPos,avActionsCount,avActionPower,unitCount (сколько раз встретился в армиях победителей)
     **/

    public static Map<UnitTypes, Double[]> getWinnerUnitsStatistics(final List<GameLogInformation> games)
            throws StatisticsException {
        try {
            if (games == null || games.isEmpty()) {
                throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
            }
            final Map<UnitTypes, Double[]> result = Map.of(
                    UnitTypes.SWORDSMAN, new Double[5],
                    UnitTypes.BOWMAN, new Double[5],
                    UnitTypes.MAGE, new Double[5],
                    UnitTypes.HEALER, new Double[5]
            );
            for (UnitTypes unitType : result.keySet()) {
                Arrays.fill(result.get(unitType), 0.0);
            }
            for (GameLogInformation game : games) {
                if (!game.getWinner().equals("DRAW")) {
                    final Map<UnitTypes, Double[]> temp = analyzeGameLogsOfOnePlayer(game.getLogList(),
                            Fields.valueOf(game.getWinner()));
                    for (UnitTypes unit : temp.keySet()) {
                        if (temp.get(unit)[2] != 0) {
                            for (int i = 0; i < 4; i++) {
                                result.get(unit)[i] += temp.get(unit)[i];
                            }
                            result.get(unit)[4]++;
                        }
                    }
                }
            }
            for (UnitTypes unitType : result.keySet()) {
                for (int i = 0; i < 4; i++) {
                    result.get(unitType)[i] /= result.get(unitType)[4];
                }
            }
            return result;
        } catch (StatisticsException e) {
            logger.error("Error unit statistics analyzing");
            throw new StatisticsException(e);
        }
    }

    /**
     * Анализатор логов одной игры
     * Выдает следующую информацию
     * unitType -> averageXPos, averageYPos, quantityOfActions, averageDamage
     * Возвращает пару таких отображнией, где на месте Х стоит статистика по логам PLAYER_ONE,
     * на месте У - PLAYER_TWO
     **/

    public static Pair<Map<UnitTypes, Integer[]>, Map<UnitTypes, Integer[]>>
    analyzeGameLogs(final List<LogInformation> logs) throws StatisticsException {
        if (logs == null || logs.isEmpty()) {
            throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
        }
        return new Pair(analyzeGameLogsOfOnePlayer(logs, Fields.PLAYER_ONE),
                analyzeGameLogsOfOnePlayer(logs, Fields.PLAYER_TWO));

    }

    /**
     * Метод анализирует логи одной игры для игрока player и выдает следующую информацию:
     * unitType -> averageXPos, averageYPos, quantityOfActions, averageDamage
     **/

    public static Map<UnitTypes, Double[]> analyzeGameLogsOfOnePlayer(final List<LogInformation> logs,
                                                                      final Fields player) {
        final Map<UnitTypes, Double[]> result = Map.of(
                UnitTypes.SWORDSMAN, new Double[4],
                UnitTypes.BOWMAN, new Double[4],
                UnitTypes.MAGE, new Double[4],
                UnitTypes.HEALER, new Double[4]
        );
        for (UnitTypes unitType : result.keySet()) {
            Arrays.fill(result.get(unitType), 0d);
        }
        final List<LogInformation> playerLogList = logs.stream().filter(log ->
                log.getAttacker().F() == player).collect(Collectors.toList());
        for (UnitTypes unitType : result.keySet()) {
            final List<LogInformation> unitLogs =
                    playerLogList.stream().filter(log -> log.getAttackerType() == unitType).
                            collect(Collectors.toList());
            if (unitLogs.size() != 0) {
                for (LogInformation log : unitLogs) {
                    result.get(unitType)[0] += (double) log.getAttacker().X() / unitLogs.size();
                    result.get(unitType)[1] += (double) log.getAttacker().Y() / unitLogs.size();
                    result.get(unitType)[2]++;
                    result.get(unitType)[3] += (double) log.getActPower() / unitLogs.size();
                }
            }
        }
        return result;
    }

    /**
     * Метод возвращает армию с наилучшей статистикой побед поражений, исходя из имеющейся статистики
     */

    public static Army getBestStatisticsArmy() {
        final Map<Army, Integer[]> armyMap = StatisticsParser.parseArmiesStatisticsFile();
        int maxWinChance = 0;
        int maxChanceGamesCount = 0;
        Army result = null;
        for (final Army army : armyMap.keySet()) {
            final int gamesCount = armyMap.get(army)[0] + armyMap.get(army)[1] + armyMap.get(army)[2];
            final int winChance = (int) ((double) armyMap.get(army)[0] / gamesCount) * 100;
            if (winChance > maxWinChance) {
                result = army;
                maxChanceGamesCount = gamesCount;
                maxWinChance = winChance;
            }
            if (winChance == maxWinChance && maxChanceGamesCount < gamesCount) {
                result = army;
                maxChanceGamesCount = gamesCount;
            }
        }
        return result;
    }

    /**
     * Метод возвращает среднюю продолжительность игры в раундах, исходя из имеющейся статистики
     **/

    public static double getAverageGameDuration() {
        return StatisticsParser.parseGameDurationStatisticsFile();
    }
}
