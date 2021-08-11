package heroes.player.botnikita;

import gamecore.auxiliaryclasses.boardexception.BoardException;
import gamecore.auxiliaryclasses.gamelogicexception.GameLogicException;
import gamecore.auxiliaryclasses.unitexception.UnitException;
import gamecore.gamelogic.*;
import gamecore.player.Answer;
import heroes.gui.Visualisable;
import heroes.gui.heroeslanterna.LanternaWrapper;
import heroes.player.BaseBot;
import heroes.player.TestBot;
import heroes.player.botnikita.decisionalgorythms.FindMinAnswerWin;
import heroes.player.botnikita.decisionalgorythms.IDecisionAlgorythm;
import heroes.player.botnikita.decisionalgorythms.MiniMaxAlgorythm;
import heroes.player.botnikita.simulation.BoardSimulation;
import heroes.player.botnikita.simulation.FieldsWrapper;
import heroes.player.botnikita.utilityfunction.HealerUtilityFunction;
import heroes.player.botnikita.utilityfunction.IMinMax;
import heroes.player.botnikita.utilityfunction.IUtilityFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

public class NikitaBot extends BaseBot implements Visualisable {
    private final IUtilityFunction utilityFunction = new HealerUtilityFunction();
    private final IDecisionAlgorythm decisionAlgorythm = new MiniMaxAlgorythm();

    private static final Logger logger = LoggerFactory.getLogger(NikitaBot.class);

    public static class NikitaBotFactory extends BaseBotFactory {
        @Override
        public NikitaBot createBot(final Fields fields) throws GameLogicException {
            return new NikitaBot(fields);
        }
    }

    @Override
    public void setTerminal(final LanternaWrapper tw) {
        super.setTerminal(tw);
    }

    public NikitaBot(final Fields field) throws GameLogicException {
        super(field);
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        try {
            return new TestBot(super.getField()).getArmy(firstPlayerArmy);
        } catch (GameLogicException e) {
            logger.error("Cannot create army from RandomBot in NikitaBot.getArmy()", e);
        }
        return null;
    }

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        final long start = System.currentTimeMillis();

        final Answer answer = decisionAlgorythm.getAnswer(board, utilityFunction, super.getField(), 4);

        final long finish = System.currentTimeMillis();

        System.out.println("TIME = " + (finish - start));

        return answer;
    }

    @Override
    public Fields getField() {
        return super.getField();
    }
}
