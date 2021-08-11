package heroes.clientserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import gamecore.auxiliaryclasses.boardexception.BoardException;
import gamecore.auxiliaryclasses.unitexception.UnitException;
import gamecore.gamelogic.Army;
import gamecore.gamelogic.Board;
import gamecore.player.Answer;
import heroes.clientserver.commands.CommonCommands;

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
    @JsonProperty
    public final int info;

    @JsonCreator
    public Data(@JsonProperty("command") final CommonCommands command,@JsonProperty("oneArmy") final Army army,
                @JsonProperty("board") final Board board, @JsonProperty("answer") final Answer answer,
                @JsonProperty("info") final int info) {
        this.command = command;
        this.army = army;
        this.board = board;
        this.answer = answer;
        this.info = info;
    }

    public Data(final CommonCommands command, final Board board, final Answer answer) {
        this.command = command;
        this.army = null;
        this.board = board;
        this.answer = answer;
        this.info = 0;
    }

    /**
     * Вспомогательный для снятия команды
     * @param data - данные
     */
    public Data(final Data data) throws UnitException, BoardException {
        command = null;
        army = (data.army != null)? new Army(data.army): null;
        board = (data.board != null)? new Board(data.board): null;
        answer = (data.answer != null)? new Answer(data.answer): null;
        this.info = 0;
    }

    public Data(final CommonCommands command) {
        this.command = command;
        this.army = null;
        this.board = null;
        this.answer = null;
        this.info = 0;
    }

    public Data(final CommonCommands command, final int info) {
        this.command = command;
        this.army = null;
        this.board = null;
        this.answer = null;
        this.info = info;
    }

    public Data(final CommonCommands command, final Board board) {
        this.command = command;
        this.army = null;
        this.board = board;
        this.answer = null;
        this.info = 0;
    }

    public Data(final Answer answer) {
        this.command = null;
        this.army = null;
        this.board = null;
        this.answer = answer;
        this.info = 0;
    }

    public Data(final CommonCommands command, Army one) {
        this.command = command;
        this.army = one;
        this.board = null;
        this.answer = null;
        this.info = 0;
    }

    public Data() {
        this.command = null;
        this.army = null;
        this.board = null;
        this.answer = null;
        this.info = 0;
    }

    public Data(final int info) {
        this.command = null;
        this.army = null;
        this.board = null;
        this.answer = null;
        this.info = info;
    }
}
