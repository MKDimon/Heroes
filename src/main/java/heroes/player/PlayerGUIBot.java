package heroes.player;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import heroes.auxiliaryclasses.ActionTypes;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gui.TerminalWrapper;
import heroes.gui.Visualisable;
import heroes.gui.menudrawers.MenuGeneralDrawer;
import heroes.gui.menudrawers.generalmenudrawers.SelectedGeneralMap;
import heroes.gui.utils.TerminalArmyDrawer;
import heroes.mathutils.Pair;
import heroes.mathutils.Position;
import heroes.player.controlsystem.Controls;
import heroes.player.controlsystem.Selector;
import heroes.units.General;
import heroes.units.GeneralTypes;
import heroes.units.Unit;
import heroes.units.UnitTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        Selector selector = new Selector(1, 3);
        while (true) {
            tw.getScreen().clear();
            MenuGeneralDrawer.drawGenerals(tw, selector.getSelectedNumber());
            try {
                tw.getScreen().refresh();
                System.out.println("refresh");
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
        Selector selector = new Selector(2, 3);

        while (true) {
            tw.getScreen().clear();

            if (firstPlayerArmy != null) {
                TerminalArmyDrawer.drawArmy(tw, new TerminalPosition(0, 0), firstPlayerArmy, true);
            }

            try {
                tw.getScreen().refresh();
                System.out.println("refresh");
            } catch (IOException e) {
                logger.error("Error refreshing terminal in playerGUIbot", e);
            }

            KeyType kt = controls.update();
            while(kt == null) {
                kt = controls.update();
            }
            selector.updateSelection(kt);
            System.out.println(selector.getCurrentSelection().getX() + "   " + selector.getCurrentSelection().getY());
            if(kt == KeyType.Enter) {
                return selector.getCurrentSelection();
            }

        }
    }

    @Override
    public Army getArmy(final Army firstPlayerArmy) {
        final Controls controls = new Controls(tw);

        final General general = selectGeneralWindowDraw(controls);

        final Pair<Integer, Integer> genPos = getGeneralPosition(controls, firstPlayerArmy);

        Unit[][] units = new Unit[2][3];
        units[genPos.getX()][genPos.getY()] = general;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (units[i][j] == null) {
                    System.out.println(String.format("Choose unit at (%d, %d): ", i, j));
                    while (true) {
                        try {
                            String unitTypeString = scanner.next();
                            units[i][j] = new Unit(UnitTypes.valueOf(unitTypeString));
                            break;
                        } catch (IllegalArgumentException | UnitException e) {
                            System.out.println("Incorrect unit!!!");
                            System.out.println(String.format("Choose unit at (%d, %d): ", i, j));
                        }
                    }
                }
            }
        }
        try {
            return new Army(units, general);
        } catch (BoardException | UnitException e) {
            logger.error("Error army creating");
            return null;
        }
    }

    @Override
    public Answer getAnswer(final Board board) throws GameLogicException {
        int attackerX;
        int attackerY;
        System.out.println("Choose attacker position: ");
        while (true) {
            try {
                System.out.print("X: ");
                attackerX = scanner.nextInt();
                System.out.print("Y: ");
                attackerY = scanner.nextInt();
                if (!board.getUnitByCoordinate(new Position(attackerX, attackerY, getField())).isActive()) {
                    throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
                }
                break;
            } catch (IllegalArgumentException | IndexOutOfBoundsException | GameLogicException e) {
                System.out.println("Incorrect attacker position!");
                System.out.println("Choose attacker position: ");

            }
        }
        final Position attacker = new Position(attackerX, attackerY, getField());
        System.out.println(new StringBuffer("Choose action: ")
                .append(board.getUnitByCoordinate(attacker).getActionType().toString())
                .append(", ").append(ActionTypes.DEFENSE));
        ActionTypes act;
        while (true) {
            try {
                String actionTypeString = scanner.next();
                act = ActionTypes.valueOf(actionTypeString);
                if (act != board.getUnitByCoordinate(attacker).getActionType() && act != ActionTypes.DEFENSE) {
                    throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
                }
                break;
            } catch (IllegalArgumentException | GameLogicException e) {
                System.out.println("Incorrect action!");
                System.out.println("Choose action: ");
            }
        }
        Fields defField = (getField() == Fields.PLAYER_ONE) ? Fields.PLAYER_TWO : Fields.PLAYER_ONE;
        if (act == ActionTypes.DEFENSE) {
            return new Answer(attacker, attacker, ActionTypes.DEFENSE);
        }
        if (act == ActionTypes.HEALING) {
            defField = getField();
        }
        int defenderX;
        int defenderY;
        System.out.println("Choose defender position: ");
        while (true) {
            try {
                System.out.print("X: ");
                defenderX = scanner.nextInt();
                System.out.print("Y: ");
                defenderY = scanner.nextInt();
                if (!board.getUnitByCoordinate(new Position(defenderX, defenderY, getField())).isAlive()) {
                    throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
                }
                break;
            } catch (IllegalArgumentException | IndexOutOfBoundsException | GameLogicException e) {
                System.out.println("Incorrect attacker position!");
                System.out.println("Choose defender position: ");
            }
        }
        Position defender = new Position(defenderX, defenderY, defField);
        return new Answer(attacker, defender, act);
    }

}
