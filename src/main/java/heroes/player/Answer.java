package heroes.player;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.GameLogicExceptionType;
import heroes.mathutils.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Answer {
    Logger logger = LoggerFactory.getLogger(Answer.class);
    @JsonProperty
    private final Position attacker;
    @JsonProperty
    private final Position defender;
    @JsonProperty
    private final ActionTypes actionType;
    @JsonCreator
    public Answer(@JsonProperty("attacker") Position attacker,
                  @JsonProperty("defender") Position defender,
                  @JsonProperty("actionType") ActionTypes actionType) throws GameLogicException {
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
    public boolean equals(final Object o) {
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