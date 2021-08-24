package client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.lanterna.input.KeyType;
import client.commands.CommandFactory;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.clientserver.ClientsConfigs;
import heroes.clientserver.Data;
import heroes.clientserver.Deserializer;
import heroes.controller.AutoController;
import heroes.controller.IController;
import heroes.gamelogic.Fields;
import heroes.gui.IGUI;
import heroes.gui.WithoutGUI;
import heroes.gui.heroeslanterna.Lanterna;
import heroes.mathutils.Pair;
import heroes.player.BaseBot;
import heroes.player.RandomBot;
import heroes.player.TestBot;
import heroes.player.botdimon.Dimon;
import heroes.player.botgleb.BotGlebAbstractFactory;
import heroes.player.botgleb.ExpectiMaxBot;
import heroes.player.botgleb.MultithreadedExpectiMaxBot;
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
        try {
        playerBots.put("Nikita", new NikitaBot.NikitaBotFactory());
        playerBots.put("Dimon", new Dimon.DimonFactory());
        playerBots.put("Gleb", BotGlebAbstractFactory.botFactoryMap.getOrDefault(
                getClientsConfig().SIMULATION_TREE, new ExpectiMaxBot.ExpectiMaxBotFactory()
        ));
        } catch (final IOException e) {
            logger.error("Error create bot factory", e);
        }
    }

    /**
     * Парсит serverConfig.json из каталога и возвращает конфиги клиента
     *
     * @return все нужные конфиги
     * @throws IOException json
     */
    public static ClientsConfigs getClientsConfig() throws IOException {
        final FileInputStream fileInputStream = new FileInputStream("clientConfig.json");

        final ClientsConfigs cc = new ObjectMapper().readValue(fileInputStream, ClientsConfigs.class);
        fileInputStream.close();
        return cc;
    }

    private final Map<String, Pair<IGUI, IController>> clientsGui = new HashMap<>();

    private final String ip;
    private final int port;
    private final ClientsConfigs clientsConfigs;

    //Клиент хранит ссылку на своего бота, чтобы вызывать у него ответы
    private BaseBot player;

    private final IGUI gui;
    private final IController controller;

    private Socket socket = null;
    private BufferedReader in = null; // поток чтения из сокета
    private BufferedWriter out = null; // поток записи в сокет

    public static void main(String[] args) {
        final Client client;
        try {
            client = new Client(null);
            client.startClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Client(final BaseBot player) throws IOException {
        this.clientsConfigs = getClientsConfig();
        ip = clientsConfigs.HOST;
        port = clientsConfigs.PORT;
        this.player = player;
        initGUIMap();
        Pair<IGUI, IController> pair = clientsGui.get(clientsConfigs.GUI);
        gui = pair.getX();
        gui.start();
        controller = pair.getY();
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

    private void initGUIMap() {
        final Lanterna lanterna = new Lanterna();
        clientsGui.put("lanterna", new Pair<>(lanterna, lanterna));
        clientsGui.put("withoutGUI", new Pair<>(new WithoutGUI(), new AutoController(clientsConfigs)));
    }

    public void chooseBot(final Fields field) {
        final Map<String, BaseBot.BaseBotFactory> botFactoryMap = new HashMap<>();
        botFactoryMap.put("Test", new TestBot.TestBotFactory());
        botFactoryMap.put("Random", new RandomBot.RandomBotFactory());
        botFactoryMap.put("Player", playerBots.getOrDefault(clientsConfigs.TYPE_BOT, new RandomBot.RandomBotFactory()));

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
                    player = botFactoryMap.get(controller.getBot(selector)).createBotWithConfigs(field, clientsConfigs);
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

    public String getBotType() {
        return clientsConfigs.TYPE_BOT;
    }
}