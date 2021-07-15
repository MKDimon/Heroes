package heroes.clientserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.player.Answer;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Data {
    @JsonProperty
    public final CommonCommands command;
    @JsonProperty
    public final Army army;
    @JsonProperty
    public final Board board;
    @JsonProperty
    public final Answer answer;

    @JsonCreator
    public Data(@JsonProperty("command") final CommonCommands command,@JsonProperty("oneArmy") final Army army,
                @JsonProperty("board") final Board board, @JsonProperty("answer") final Answer answer) {
        this.command = command;
        this.army = army;
        this.board = board;
        this.answer = answer;
    }

    public Data(final CommonCommands command) {
        this.command = command;
        this.army = null;
        this.board = null;
        this.answer = null;
    }

    public Data(final Answer answer) {
        this.command = null;
        this.army = null;
        this.board = null;
        this.answer = answer;
    }

    public Data(final CommonCommands command, Army one) {
        this.command = command;
        this.army = one;
        this.board = null;
        this.answer = null;
    }
}
