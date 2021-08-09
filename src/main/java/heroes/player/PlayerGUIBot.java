package heroes.player;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyType;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.TerminalWrapper;
import heroes.gui.Visualisable;
import heroes.gui.menudrawers.MenuBoardDrawer;
import heroes.gui.menudrawers.MenuGeneralDrawer;
import heroes.gui.menudrawers.MenuTitleDrawers;
import heroes.gui.menudrawers.MenuUnitDrawer;
import heroes.gui.menudrawers.generalmenudrawers.SelectedGeneralMap;
import heroes.gui.menudrawers.unitmenudrawers.UnitMenuMap;
import heroes.gui.utils.TerminalArmyDrawer;
import heroes.mathutils.Pair;
import heroes.mathutils.Position;
import heroes.player.controlsystem.Controls;
import heroes.player.controlsystem.Selector;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Класс - бот, управляемый игроком.
 * Игрок через консоль задает армию и выбирает дейтсвия над юнитами.
 **/

public class PlayerGUIBot extends BaseBot implements Visualisable {
    private final Logger logger = LoggerFactory.getLogger(PlayerGUIBot.class);
    private final Scanner scanner = new Scanner(System.in);
    protected TerminalWrapper tw;

    @Override
    public void setTerminal(final TerminalWrapper tw) {
        super.tw = tw;
        this.tw = super.tw;
    }

    /**
     * Фабрика ботов.
     **/

    public static class PlayerGUIBotFactory extends BaseBotFactory {

        @Override
        public PlayerGUIBot createBot(final Fields fields) throws GameLogicException {
            return new PlayerGUIBot(fields);
        }
    }

    public PlayerGUIBot(final Fields fields) throws GameLogicException {
        super(fields);
    }

    private General selectGeneralWindowDraw(final Controls controls) {
        final Selector selector = new Selector(1, 3);
        while (true) {
            tw.getScreen().clear();
            MenuGeneralDrawer.drawGenerals(tw, selector.getSelectedNumber());
            MenuTitleDrawers.drawChooseGeneral(tw, new TerminalPosition(14, 2));
            try {
                tw.getScreen().refresh();
            } catch (IOException e) {
                logger.error("Error refreshing terminal in playerGUIbot", e);
            }

            KeyType kt = controls.update();
            while(kt == null) {
                kt = controls.update();
            }
            selector.updateSelection(kt);

            if(kt == KeyType.Enter) {
                try {
                    return new General(GeneralTypes.valueOf(SelectedGeneralMap.getDrawer(selector.getSelectedNumber())));
                } catch (UnitException e) {
                    logger.error("Error selecting general in playerGUIbot", e);
                }
            }
        }
    }

    private Pair<Integer, Integer> getGeneralPosition(final Controls controls, final Army firstPlayerArmy) {
        final Selector selector = new Selector(3, 2);

        while (true) {
            tw.getScreen().clear();

            if (firstPlayerArmy != null) {
                TerminalArmyDrawer.drawArmy(tw, new TerminalPosition(0, 0), firstPlayerArmy,
                        false, Fields.PLAYER_ONE);
                MenuTitleDrawers.drawArmyHereDisclaimer(tw, new TerminalPosition(10, 10));
            } else {
                MenuTitleDrawers.drawArmyNotHereDisclaimer(tw, new TerminalPosition(10, 20));
            }

            MenuTitleDrawers.drawGeneralPosition(tw, new TerminalPosition(10, 2));

            MenuBoardDrawer.drawBorders(tw, new TerminalPosition(110, 10),
                    new TerminalPosition(137, 46));
            MenuBoardDrawer.drawUnitBorders(tw, new TerminalPosition(110, 10),
                    new TerminalPosition(137, 46), selector.getSelectedNumber());
            try {
                tw.getScreen().refresh();
            } catch (IOException e) {
                logger.error("Error refreshing terminal in playerGUIbot", e);
            }

            KeyType kt = controls.update();
            while(kt == null) {
                kt = controls.update();
            }
            selector.updateSelection(kt);
            System.out.println(selector.getCurrentSelection().getY() + "   " + selector.getCurrentSelection().getX());
            if(kt == KeyType.Enter) {
                return selector.getCurrentSelection();
            }

        }
    }

    private Unit selectUnit(final Controls controls) {
        final Selector selector = new Selector(4, 1);
        while (true) {
            // tw.getScreen().clear();
            MenuUnitDrawer.drawUnits(tw, selector.getSelectedNumber());
            try {
                tw.getScreen().refresh();
            } catch (IOException e) {
                logger.error("Error refreshing terminal in playerGUIbot", e);
            }

            KeyType kt = controls.update();
            while(kt == null) {
                kt = controls.update();
            }
            selector.updateSelection(kt);

            if(kt == KeyType.Enter) {
                return UnitMenuMap.getDrawer(selector.getSelectedNumber());
            }
        }
    }

    private Unit[][] selectArmy(final Controls controls, final General general, final Army firstPlayerArmy)
            throws BoardException, UnitException {

        final Pair<Integer, Integer> genPos = getGeneralPosition(controls, firstPlayerArmy);
        final Selector selector = new Selector(3, 2);
        try {
            tw.getScreen().refresh();
        } catch (IOException e) {
            logger.error("Error refreshing terminal in playerGUIbot", e);
        }

        final Unit[][] units = new Unit[2][3];
        units[genPos.getY()][genPos.getX()] = general;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {

                tw.getScreen().clear();

                if (firstPlayerArmy != null) {
                    TerminalArmyDrawer.drawArmy(tw, new TerminalPosition(0, 0), firstPlayerArmy,
                            false, Fields.PLAYER_ONE);
                    MenuTitleDrawers.drawArmyHereDisclaimer(tw, new TerminalPosition(10, 10));
                } else {
                    MenuTitleDrawers.drawArmyNotHereDisclaimer(tw, new TerminalPosition(10, 20));
                }



                MenuBoardDrawer.drawBorders(tw, new TerminalPosition(110, 10),
                        new TerminalPosition(137, 46));
                MenuBoardDrawer.drawUnitBorders(tw, new TerminalPosition(110, 10),
                        new TerminalPosition(137, 46), selector.getSelectedNumber());

                TerminalArmyDrawer.drawArmy(tw, new TerminalPosition(0,0), units, general,
                        true, Fields.PLAYER_TWO);

                MenuTitleDrawers.drawChoose(tw, new TerminalPosition(5, 2));
                MenuTitleDrawers.drawTroops(tw, new TerminalPosition(85, 2));

                if (units[i][j] == null) {
                    MenuBoardDrawer.drawUnitBorders(tw, new TerminalPosition(110, 10),
                            new TerminalPosition(137, 46), selector.getSelectedNumber(j, i));

                    units[i][j] = selectUnit(controls);

                    try {
                        tw.getScreen().refresh();
                    } catch (IOException e) {
                        logger.error("Error refreshing terminal in playerGUIbot", e);
                    }

                }

                try {
                    tw.getScreen().refresh();
                } catch (IOException e) {
                    logger.error("Error refreshing terminal in playerGUIbot", e);
                }
            }
        }
        return units;
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        final Controls controls = new Controls(tw);

        final General general = selectGeneralWindowDraw(controls);

        try {
            return new Army(selectArmy(controls, general, firstPlayerArmy), general);
        } catch (BoardException | UnitException e) {
            logger.error("Error army creating");
            return null;
        }
    }

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        Random r = new Random();


        Fields defField = (getField() == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;

        List<Position> posAttack = new ArrayList<>();
        List<Position> posDefend = new ArrayList<>();

        Unit[][] armyAttack = board.getArmy(getField());
        Unit[][] armyDefend = board.getArmy(defField);

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (armyAttack[i][j].isActive()) {
                    posAttack.add(new Position(i, j, getField()));
                }
                if (armyDefend[i][j].isAlive()) {
                    posDefend.add(new Position(i, j, defField));
                }
            }
        }

        Position attackerPos = posAttack.get(r.nextInt(posAttack.size()));
        if(r.nextInt(100) < 20){
            return new Answer(attackerPos, attackerPos, ActionTypes.DEFENSE);
        }
        ActionTypes attackType = board.getUnitByCoordinate(attackerPos).getActionType();
        if (attackType == ActionTypes.HEALING) {
            defField = getField();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 3; j++) {
                    if (armyAttack[i][j].isAlive() && !posAttack.contains(new Position(i, j, getField()))) {
                        posAttack.add(new Position(i, j, getField()));
                    }
                }
            }
        }

        Position defenderPos = (defField == getField()) ?
                posAttack.get(r.nextInt(posAttack.size())) :
                posDefend.get(r.nextInt(posDefend.size()));

        return new Answer(attackerPos, defenderPos, attackType);
    }

}
