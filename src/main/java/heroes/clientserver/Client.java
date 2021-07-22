package heroes.clientserver;

import heroes.auxiliaryclasses.gamelogicexception.GameLogicExceptionType;
import heroes.clientserver.commands.CommandFactory;
import heroes.auxiliaryclasses.gamelogicexception.GameLogicException;
import heroes.gamelogic.Fields;
import heroes.gui.TerminalWrapper;
import heroes.player.BaseBot;
import heroes.player.PlayerBot;
import heroes.player.RandomBot;
import heroes.player.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private static final String IP = "127.0.0.1";

    private final String ip;
    private final int port;

    //Клиент хранит ссылку на своего бота, чтобы вызывать у него ответы
    private BaseBot player;

    private TerminalWrapper tw;

    private Socket socket = null;
    private BufferedReader in = null; // поток чтения из сокета
    private BufferedWriter out = null; // поток записи в сокет

    public static void main(String[] args) {
        try {
            final ServersConfigs sc = Deserializer.getConfig();
            final Client client = new Client(IP, sc.PORT, null);
            client.startClient();
        } catch (IOException ignore) {}
    }

    private Client(final String ip, final int port,final BaseBot player) {
        this.ip = ip;
        this.port = port;
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
        botFactoryMap.put("Player", new PlayerBot.PlayerBotFactory());
        System.out.println("Choose your bot: Test, Random, Player");
        final Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                final String botTypeString = scanner.next();
                if(!botFactoryMap.containsKey(botTypeString)){
                    throw new GameLogicException(GameLogicExceptionType.INCORRECT_PARAMS);
                }
                player = botFactoryMap.get(botTypeString).createBot(field);
                break;
            } catch (IllegalArgumentException | GameLogicException e) {
                System.out.println("Incorrect bot type!!!");
                System.out.println("Choose your bot: Test, Random, Player");
            }
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

    public TerminalWrapper getTw() {
        return tw;
    }

    /**
     * Первое сообщение - поле игрока
     * второе сообщение - выбери бота
     * Третье сообщение - выбери армию
     * Далее приходит доска, если сервер требует сделать ход, или сообщение о конце игры
     */
    private void start() {
        try {
            tw = new TerminalWrapper();
            tw.start();

            while (!socket.isClosed()) {
                if (in.ready()) {
                    final String message = in.readLine();
                    final Data data = Deserializer.deserializeData(message);

                    final CommandFactory commandFactory = new CommandFactory();
                    commandFactory.getCommand(data, out, this).execute();
                }
                tw.getScreen().pollInput();
            }
        } catch (final IOException | NullPointerException e) {
            logger.error("Error client running", e);
            downService();
        }
    }
}