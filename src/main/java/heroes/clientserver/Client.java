package heroes.clientserver;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.clientserver.commands.CommandFactory;
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
            ServersConfigs sc = Deserializer.getConfig();
            Client client = new Client(IP, sc.PORT, null);
            client.startClient();
        }
        catch (IOException ignore) {}
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
        Map<String, BaseBot.BaseBotFactory> botFactoryMap = new HashMap<>();
        botFactoryMap.put("Test", new TestBot.TestBotFactory());
        botFactoryMap.put("Random", new RandomBot.RandomBotFactory());
        botFactoryMap.put("Player", new PlayerBot.PlayerBotFactory());
        System.out.println("Choose your bot: Test, Random, Player");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String botTypeString = scanner.next();
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
    @SuppressWarnings("InfiniteLoopStatement")
    private void start() {
        try {//Первое сообщение  - поле игрока
            tw = new TerminalWrapper();
            tw.start();
            TextGraphics tg = tw.getScreen().newTextGraphics();
            String message;
            Data data = new Data();

            while (true) {
                if (in.ready()) {
                    message = in.readLine();
                    data = Deserializer.deserializeData(message);

                    CommandFactory commandFactory = new CommandFactory();
                    commandFactory.getCommand(data, out, this).execute();
                }
            }
        } catch (IOException | NullPointerException e) {
            logger.error("Error client running", e);
            downService();
        }
    }
}
/*Сервер отправляет клиенту запросы следующих типов:
    Создать армию (специальная строка)
*   Выбрать ход (Получает сериализованный борт, отправляет сериализованный ансвер)
    Закончить игру (спец строка)

На сервере ход не будет выполенен, если он выбран неверно,
т.е. запрос "сделать ход" будет отправляься до тех пор, пока не будет выбран возможный ход

Если получен запрос СОЗДАТЬ АРМИЮ, то клиент должен попросить игрока создать армию
 и отправить сериализованную армию на сервер

 Если получен запрос сделать ход, то клиент получает сериализованную доску, десериализует ее и отправляет боту,
  тот делает ход (Answer) и отправляет его клиенту, клиент отправляет сериализованный ответ на сервер
*/