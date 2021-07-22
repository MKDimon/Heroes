package heroes.clientserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.commands.CommonCommands;
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

    public Data(final CommonCommands command, final Board board, final Answer answer) {
        this.command = command;
        this.army = null;
        this.board = board;
        this.answer = answer;
    }

    /**
     * Вспомогательный для снятия команды
     * @param data - данные
     */
    public Data(final Data data) throws GameLogicException, UnitException, BoardException {
        command = null;
        army = (data.army != null)? new Army(data.army): null;
        board = (data.board != null)? new Board(data.board): null;
        answer = (data.answer != null)? new Answer(data.answer): null;
    }

    public Data(final CommonCommands command) {
        this.command = command;
        this.army = null;
        this.board = null;
        this.answer = null;
    }

    public Data(final CommonCommands command, final Board board) {
        this.command = command;
        this.army = null;
        this.board = board;
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

    public Data() {
        this.command = null;
        this.army = null;
        this.board = null;
        this.answer = null;
    }


}
