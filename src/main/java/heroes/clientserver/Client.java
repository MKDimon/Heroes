package heroes.clientserver;

import com.googlecode.lanterna.input.KeyType;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.clientserver.commands.CommandFactory;
import heroes.controller.IController;
import heroes.gamelogic.Fields;
import heroes.gui.IGUI;
import heroes.gui.heroeslanterna.Lanterna;
import heroes.gui.heroeslanterna.menudrawers.botchoicedrawers.BotMenuMap;
import heroes.gui.heroeslanterna.menudrawers.botchoicedrawers.MenuBotDrawer;
import heroes.player.*;
import heroes.player.botnikita.NikitaBot;
import heroes.player.controlsystem.Controls;
import heroes.player.controlsystem.Selector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private static final Map<String, BaseBot.BaseBotFactory> playerBots = new HashMap<>();
    static {
        playerBots.put("Dimon", new NikitaBot.NikitaBotFactory());
    }

    private final String ip;
    private final int port;
    private final ClientsConfigs clientsConfigs;

    //Клиент хранит ссылку на своего бота, чтобы вызывать у него ответы
    private BaseBot player;

    private IGUI gui;
    private IController controller;

    private Socket socket = null;
    private BufferedReader in = null; // поток чтения из сокета
    private BufferedWriter out = null; // поток записи в сокет

    public static void main(String[] args) {
        final Client client;
        try {
            client = new Client(null);
            client.startClient();
        } catch (IOException ignore) {}
    }

    private Client(final BaseBot player) throws IOException {
        this.clientsConfigs = Deserializer.getClientsConfig();
        ip = clientsConfigs.HOST;
        port = clientsConfigs.PORT;
        this.player = player;
    }

    private void startClient() {
        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            start();
        } catch (IOException e) {
            logger.error("Error client starting", e);
        }
    }

    public void chooseBot(final Fields field) {
        final Map<String, BaseBot.BaseBotFactory> botFactoryMap = new HashMap<>();
        botFactoryMap.put("Test", new TestBot.TestBotFactory());
        botFactoryMap.put("Random", new RandomBot.RandomBotFactory());
        botFactoryMap.put("Player", playerBots.getOrDefault(clientsConfigs.TYPE_BOT, new RandomBot.RandomBotFactory()));
        //botFactoryMap.put("PlayerGUI", new PlayerGUIBot.PlayerGUIBotFactory());

        final Controls controls = new Controls(controller);
        final Selector selector = new Selector(1 , 4);

        while (true) {
            gui.clear();
            gui.drawBots(selector);
            gui.refresh();

            KeyType kt = controls.update();
            while(kt == null) {
                kt = controls.update();
            }
            selector.updateSelection(kt);

            if(kt == KeyType.Enter) {
                try { //
                    player = botFactoryMap.get(controller.getBot(selector)).createBot(field);
                    player.setTerminal(gui);

                    gui.clear();
                    gui.drawWait();
                    gui.refresh();
                    break;
                } catch (GameLogicException e) {
                    logger.error("Error create bot", e);
                }
            }
            gui.clear();
        }
    }

    public void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            logger.error("Error service downing", e);
        }
    }

    public BaseBot getPlayer() {
        return player;
    }

    public IGUI getGUI() {
        return gui;
    }

    public IController getController() {
        return controller;
    }

    /**
     * Первое сообщение - поле игрока
     * второе сообщение - выбери бота
     * Третье сообщение - выбери армию
     * Далее приходит доска, если сервер требует сделать ход, или сообщение о конце игры
     */
    private void start() {
        try {
            final Lanterna lanterna = new Lanterna();
            gui = lanterna;
            gui.start();
            controller = lanterna;

            while (!socket.isClosed()) {
                if (in.ready()) {
                    final String message = in.readLine();
                    final Data data = Deserializer.deserializeData(message);

                    final CommandFactory commandFactory = new CommandFactory();
                    commandFactory.getCommand(data, out, this).execute();
                }
                controller.pollInput();
            }
        } catch (final IOException | NullPointerException e) {
            logger.error("Error client running", e);
            downService();
        }
    }
}