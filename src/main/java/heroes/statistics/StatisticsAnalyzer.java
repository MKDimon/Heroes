package heroes.statistics;


import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsAnalyzer {
    static final Logger logger = LoggerFactory.getLogger(StatisticsAnalyzer.class);
    /*
     * Какие данные можно хранить для статистики?
     * Состав армии,победы,поражения,ничьи +
     * юнит,сколько раз в среднем бьет,с какой средней силой ?
     * какой генерал чаще всего в команде победителя ?
     */

    /**
     * Метод собирает статистику по всем играм. Для каждой армии считает количетсво побед, поражений, ничьих.
     **/

    public static Map<Army, Integer[]> analyzeArmiesStatistics(final List<GameLogInformation> games) throws StatisticsException {
        try{
            if(games == null || games.isEmpty()){
                throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
            }
            Map<Army,Integer[]> result = new HashMap<>();
            for(GameLogInformation gameLog: games){
                result.put(gameLog.getPlayerOneArmy(), getArmyStatistics(gameLog.getPlayerOneArmy(), games));
                result.put(gameLog.getPlayerTwoArmy(), getArmyStatistics(gameLog.getPlayerTwoArmy(), games));
            }
            return result;
        } catch(StatisticsException | BoardException | UnitException e){
            logger.error("Error army statistics counting", e);
            throw new StatisticsException(e);
        }

    }

    /**
     * Метод для даннй армии подсчитывает количетсво побед, поражений, ничих, исходя из данных обо всех играх.
     * @param army - армия, для которой собирается статистика
     * @param games - список всех игр
     * @return Массив целых чисел, где на 0-ом месте - количетсво побед,
     *                                 на 1-ом месте - колчиество поражений,
     *                                 на 2-ом месте количетсво ничьих.
     **/
    public static Integer[] getArmyStatistics(final Army army, final List<GameLogInformation> games) throws BoardException, UnitException, StatisticsException {
        if (army == null || games == null || games.isEmpty()){
            throw new StatisticsException(StatisticsExceptionTypes.INCORRECT_PARAMS);
        }
        Integer[] statistics = new Integer[3];
        for (int i = 0; i < statistics.length; i++) {
            statistics[i] = 0;
        }
        for (GameLogInformation game:games){
            Army playerOneArmy = game.getPlayerOneArmy();
            Army playerTwoArmy = game.getPlayerTwoArmy();
            boolean isArmyOne = playerOneArmy.equals(army);
            boolean isArmyTwo = playerTwoArmy.equals(army);
            if (isArmyOne){
                switch (game.getWinner()) {
                    case "PLAYER_ONE" -> statistics[0]++;
                    case "PLAYER_TWO" -> statistics[1]++;
                    case "DEAD HEAT" -> statistics[2]++;
                }
            }
            if (isArmyTwo){
                switch (game.getWinner()) {
                    case "PLAYER_ONE" -> statistics[1]++;
                    case "PLAYER_TWO" -> statistics[0]++;
                    case "DEAD HEAT" -> statistics[2]++;
                }
            }
        }
        return statistics;
    }


}
