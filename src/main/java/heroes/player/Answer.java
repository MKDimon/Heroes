package heroes.player;

import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.GameLogicExceptionType;
import heroes.mathutils.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Answer {
    Logger logger = LoggerFactory.getLogger(Answer.class);

    private Position attacker;
    private Position defender;
    private ActionTypes actionType;

    public Answer(Position attacker, Position defender, ActionTypes actionType) throws GameLogicException {
        if (attacker == null || defender == null || actionType == null) {
            throw new GameLogicException(GameLogicExceptionType.NULL_POINTER);
        }
        this.attacker = attacker;
        this.defender = defender;
        this.actionType = actionType;
        logger.info("Attacker position = {}, defender position = {}, action type = {}", attacker, defender, actionType);
    }

    public Position getAttacker() {
        return attacker;
    }

    public Position getDefender() {
        return defender;
    }

    public ActionTypes getActionType() {
        return actionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return Objects.equals(attacker, answer.attacker) && Objects.equals(defender, answer.defender) &&
                actionType == answer.actionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(attacker, defender, actionType);
    }
}
