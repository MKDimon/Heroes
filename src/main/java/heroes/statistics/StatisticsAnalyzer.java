package heroes.statistics;


import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.statisticsexception.StatisticsException;
import heroes.auxiliaryclasses.statisticsexception.StatisticsExceptionTypes;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        } catch (final StatisticsException |  BoardException | UnitException e) {
            logger.error("Error army statistics counting", e);
            throw new StatisticsException(e);
        }

    }

    /**
     * Метод для даннй армии подсчитывает количетсво побед, поражений, ничих, исходя из данных обо всех играх.
     * @param army  - армия, для которой собирается статистика
     * @param games - список всех игр
     * @return Массив целых чисел, где на 0-ом месте - количетсво побед,
     *                                 на 1-ом месте - колчиество поражений,
     *                                 на 2-ом месте количетсво ничьих.
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
                    case "DEAD HEAT" -> statistics[2]++;
                }
            }
            if (isArmyTwo) {
                switch (game.getWinner()) {
                    case "PLAYER_ONE" -> statistics[1]++;
                    case "PLAYER_TWO" -> statistics[0]++;
                    case "DEAD HEAT" -> statistics[2]++;
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
        if(games == null || games.isEmpty()){
                throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
            }
        int count = 0;
        for(GameLogInformation game: games){
            count += game.getCountOfRounds();
        }
        return (double)count/games.size();
        } catch (final StatisticsException e) {
            logger.error("Error average game duration analyzing",e);
            throw new StatisticsException(e);
        }
    }

}
