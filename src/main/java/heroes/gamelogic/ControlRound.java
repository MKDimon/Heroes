package heroes.gamelogic;

import heroes.statistics.StatisticsCollector;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.boardfactory.DamageCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Класс с чисто статическими функциями для отслеживания действий
 * между ходами игроков и раундами
 */
public class ControlRound {
    static Logger logger = LoggerFactory.getLogger(DamageCommand.class);

    private static final int maxRound = 10;

    public static void nextPlayer(final Board board) {
        if (board.getCurrentPlayer() == Fields.PLAYER_TWO && Board.activeCount(board.getFieldPlayerOne()) != 0) {
            board.setCurrentPlayer(Fields.PLAYER_ONE);
        } else if (board.getCurrentPlayer() == Fields.PLAYER_ONE && Board.activeCount(board.getFieldPlayerTwo()) != 0) {
            board.setCurrentPlayer(Fields.PLAYER_TWO);
        }
    }

    /**
     * Проверяет последовательно на:
     * - Смерть генералов
     * - Смерть первой линии (если умерли, двигает заднюю вперед)
     * - Смерть всей армии
     * - Новый раунд
     * Меняет ходящего игрока
     *
     * @param board состояние игры
     * @return продолжается игра - true / false иначе
     * @throws UnitException ошибка ;D
     */
    public static boolean checkStep(final Board board) throws UnitException {
        // Количество активных юнитов
        if (!board.getGeneralPlayerOne().isAlive() && board.isArmyOneInspired()) {
            board.deinspireArmy(board.getFieldPlayerOne(), board.getGeneralPlayerOne());
            board.setArmyOneInspired(false);
        }
        if (!board.getGeneralPlayerTwo().isAlive() && board.isArmyTwoInspired()) {
            board.deinspireArmy(board.getFieldPlayerTwo(), board.getGeneralPlayerTwo());
            board.setArmyTwoInspired(false);
        }

        Board.checkAliveLine(board.getFieldPlayerOne());
        Board.checkAliveLine(board.getFieldPlayerTwo());

        logger.info("\n[NEW STEP]\n");

        long active = Board.activeCount(board.getFieldPlayerOne());
        active += Board.activeCount(board.getFieldPlayerTwo());
        ControlRound.nextPlayer(board);


        if (Board.aliveCountInArmy(board.getFieldPlayerTwo()) == 0) {
            board.setStatus(EndGameStatus.PLAYER_ONE_WINS);
            logger.info("Конец на раунде: {} \nПобедил PlayerOne\n", board.getCurNumRound());
            StatisticsCollector.recordMessageToCSV(String.valueOf(new StringBuffer().append(board.getCurNumRound()).
                            append(",")),
                    StatisticsCollector.actionStatisticsFilename);
            StatisticsCollector.recordWinnerToCSV(Fields.PLAYER_ONE, StatisticsCollector.actionStatisticsFilename);
            StatisticsCollector.recordMessageToCSV("GAME OVER\n", StatisticsCollector.actionStatisticsFilename);
            return false;
        }
        if (Board.aliveCountInArmy(board.getFieldPlayerOne()) == 0) {
            board.setStatus(EndGameStatus.PLAYER_TWO_WINS);
            logger.info("Конец на раунде: {} \nПобедил PlayerTwo\n", board.getCurNumRound());
            StatisticsCollector.recordMessageToCSV(String.valueOf(new StringBuffer().append(board.getCurNumRound()).
                            append(",")),
                    StatisticsCollector.actionStatisticsFilename);
            StatisticsCollector.recordWinnerToCSV(Fields.PLAYER_TWO, StatisticsCollector.actionStatisticsFilename);
            StatisticsCollector.recordMessageToCSV("GAME OVER\n", StatisticsCollector.actionStatisticsFilename);
            return false;
        }

        if (active == 0) {
            return newRound(board);
        }
        return true;
    }

    /**
     * Проверка на достижение максимального количества раундов
     * Меняет ходящего игрока в начале раунда
     * Делает всех юнитов активными и убирает бонусную защиту (если ее кто то нажимал)
     *
     * @param board состояние игры
     * @return продолжается игра - true / false иначе
     */
    public static boolean newRound(final Board board) {
        if (board.getCurNumRound() >= maxRound) {
            StatisticsCollector.recordMessageToCSV(new StringBuffer().append("DEAD HEAT\n").toString(),
                    StatisticsCollector.actionStatisticsFilename);
            board.setStatus(EndGameStatus.NO_WINNERS);
            logger.info("Конец игры: НИЧЬЯ");
            return false;
        }

        // Смена игрока начинающего раунд
        board.setCurrentPlayer((board.getRoundPlayer() == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_TWO);
        board.setRoundPlayer(board.getCurrentPlayer());

        Arrays.stream(board.getFieldPlayerOne()).forEach(x -> Arrays.stream(x).forEach(t -> t.setActive(true)));
        Arrays.stream(board.getFieldPlayerTwo()).forEach(x -> Arrays.stream(x).forEach(t -> t.setActive(true)));
        Arrays.stream(board.getFieldPlayerOne()).forEach(x -> Arrays.stream(x).forEach(t -> t.setBonusArmor(0)));
        Arrays.stream(board.getFieldPlayerTwo()).forEach(x -> Arrays.stream(x).forEach(t -> t.setBonusArmor(0)));
        board.setCurNumRound(board.getCurNumRound() + 1);

        return true;
    }
}
