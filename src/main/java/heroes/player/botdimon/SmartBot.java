package heroes.player.botdimon;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.player.Answer;
import heroes.player.BaseBot;
import heroes.player.botdimon.simulationfeatures.functions.IUtilityFunc;
import heroes.player.botdimon.simulationfeatures.treesanswers.SimulationTreeFactory;

public abstract class SmartBot extends BaseBot {
    protected final IUtilityFunc func;
    protected final SimulationTreeFactory factory;
    protected final int depth;

    public SmartBot(final Fields field, final IUtilityFunc func,
                    final SimulationTreeFactory factory, final int depth) throws GameLogicException {
        super(field);
        this.func = func;
        this.factory = factory;
        this.depth = depth;
    }

    @Override
    public abstract Army getArmy(Army firstPlayerArmy);

    @Override
    public abstract Answer getAnswer(Board board) throws GameLogicException;
}
