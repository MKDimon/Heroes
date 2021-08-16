package heroes.gamelogic;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.validation.ValidationUnits;
import heroes.gamelogic.validation.Validator;
import heroes.mathutils.Position;
import heroes.player.Answer;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameLogic {
    private static final Logger logger = LoggerFactory.getLogger(GameLogic.class);

    private Board board;
    private boolean gameBegun;

    public GameLogic() {
        gameBegun = false;
    }

    public GameLogic(final Board board) throws UnitException, BoardException {
        this.board = new Board(board);
        gameBegun = board.getStatus() == GameStatus.GAME_PROCESS;
    }

    /**
     * Создает доску и объявляет начало игры
     * Валидирует данные
     *
     * @param fieldPlayerOne - армия игрока 1
     * @param fieldPlayerTwo - армия игрока 2
     * @return - успешное начало игры true / ошибка false
     */
    public boolean gameStart(final Army fieldPlayerOne, final Army fieldPlayerTwo) {
        try {
            if (fieldPlayerOne == fieldPlayerTwo
            || fieldPlayerOne.getPlayerUnits() == fieldPlayerTwo.getPlayerUnits()
            || fieldPlayerOne.getGeneral() == fieldPlayerTwo.getGeneral()) {
                throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
            }

            Validator.checkNullPointer(fieldPlayerOne, fieldPlayerTwo,
                    fieldPlayerOne.getGeneral(), fieldPlayerTwo.getGeneral());

            Validator.checkNullPointerInArmy(fieldPlayerOne.getPlayerUnits());
            Validator.checkNullPointerInArmy(fieldPlayerTwo.getPlayerUnits());

            board = new Board(fieldPlayerOne, fieldPlayerTwo);
            gameBegun = true;
            return true;
        } catch (final NullPointerException | GameLogicException | UnitException exception) {
            logger.error(" Game Start failed ",exception);
            return false;
        }
    }

    /**
     * Валидирует данные
     * Если при проверке пришла ошибка UnitException (UNIT_CANT_STEP)
     * меняет act на ActionTypes.DEFENSE
     *
     * @param attacker инициатор
     * @param defender цель
     * @param act действие
     * @return успешная валидация - true / false иначе
     */
    private ValidationUnits actionValidate(final Position attacker, final Position defender, final ActionTypes act) {
        if (!gameBegun) {
            return ValidationUnits.INVALID_STEP;
        }
        try {
            Validator.checkNullPointer(attacker, defender, act);
            Validator.checkNullPointer(attacker.X(), attacker.Y(), defender.Y(), defender.X());
            Validator.checkIndexOutOfBounds(attacker);
            Validator.checkIndexOutOfBounds(defender);

            Validator.checkCorrectPlayer(board, attacker);
            Validator.checkCorrectAction(board.getUnitByCoordinate(attacker), act);
            Validator.checkCorrectAttacker(board.getUnitByCoordinate(attacker));
            Validator.checkCorrectDefender(board.getUnitByCoordinate(defender));

            int countAliveAtc = 0, x = attacker.X();
            final Unit[][] units = board.getArmy(attacker.F());
            for (int i = 0; i < 3; i++) {
                if (units[x][i].isAlive()) {
                    countAliveAtc++;
                }
            }
            Validator.checkTargetAction(attacker, defender, act, countAliveAtc);
        } catch (final NullPointerException | BoardException exception) {
            logger.warn(exception.getMessage());
            return ValidationUnits.INVALID_STEP;
        }
        return ValidationUnits.SUCCESSFUL_STEP;
    }

    /**
     * Возвращает список целей для действия
     *
     * @param def - текущая цель (защищающаяся)
     * @param act - действие над целью
     * @return list целей
     */
    private List<Unit> actionGetList(final Position def, final ActionTypes act) {
        List<Unit> result = new ArrayList<>();
        if (act.isMassEffect()) {
            result.addAll(Arrays.asList((board.getArmy(def.F())[0])));
            result.addAll(Arrays.asList((board.getArmy(def.F())[1])));
        } else {
            result.add(board.getUnitByCoordinate(def));
        }
        return result;
    }

    /**
     * Запрашивает валидацию, далее смотрит по ситуации
     * INVALID_STEP - действие не может быть выполнено
     * UNIT_CANT_STEP - отправляет юнита в защиту, т.к. у него нет ходов
     * SUCCESSFUL_STEP - выполняет действие
     * после прогоняет шаг в ControlRound для смены шага
     *
     * @param attacker инициатор
     * @param defender цель
     * @param act действвие
     * @return прошло или нет
     * @throws UnitException у юнитов может что то пойти не так
     */
    public boolean action(final Position attacker, final Position defender, final ActionTypes act) throws UnitException {
        logger.info("Attacker position = {}, defender position = {}, action type = {}", attacker, defender, act);
        ValidationUnits result = actionValidate(attacker, defender, act);
        if (result == ValidationUnits.SUCCESSFUL_STEP) {
            logger.info(result.toString());
            board.doAction(board.getUnitByCoordinate(attacker), actionGetList(defender, act), act);
            gameBegun = ControlRound.nextStep(board);
            return true;
        }
        return false;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isGameBegun() {
        return gameBegun;
    }

    /**
     * Метод возвращает список возмодных для игрока player ходов.
     **/

    public List<Answer> getAvailableMoves(final Fields player) throws GameLogicException {
        final Fields defField = player == Fields.PLAYER_TWO ? Fields.PLAYER_ONE : Fields.PLAYER_TWO;
        final List<Answer> result = new LinkedList<>();
        final List<Position> attackers = board.getActiveUnitsPositions(player);
        final List<Position> defenders = board.getAliveUnitsPositions(defField);
        final List<Position> aliveAttackers = board.getAliveUnitsPositions(player);

        for(final Position atPos : attackers){
            if(board.getUnitByCoordinate(atPos).getActionType() == ActionTypes.HEALING){
                for(final Position healingPos : aliveAttackers){
                    result.add(new Answer(atPos, healingPos, ActionTypes.HEALING));
                }
            } else {
                for(final Position defPos : defenders){
                    final Answer curAnswer = new Answer(atPos, defPos, board.getUnitByCoordinate(atPos).getActionType());
                    if(actionValidate(curAnswer.getAttacker(), curAnswer.getDefender(), curAnswer.getActionType()) ==
                            ValidationUnits.SUCCESSFUL_STEP){
                        result.add(curAnswer);
                        //Если встрелся маг, то прекращаем искать ему новые цели, чтобы не плодить одинаковые ветки
                        if(curAnswer.getActionType() == ActionTypes.AREA_DAMAGE){
                            break;
                        }
                    }
                }
            }
            result.add(new Answer(atPos, atPos, ActionTypes.DEFENSE));
        }
        return result;
    }

}
