package heroes.clientserver;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.gamelogic.Fields;
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

    private Client(final String ip, final int port, BaseBot player) {
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

    //Метод, который вызывает у игрока создание армии
    private String sendArmyJson() throws IOException {
        return Serializer.serializeArmy(player.getArmy());
    }

    //Метод, который вызывает у игрока ответ
    private String sendAnswerJson(String jsonBoard) throws GameLogicException, IOException {
        return Serializer.serializeAnswer(player.getAnswer(Deserializer.deserializeBoard(jsonBoard)));
    }

    private BaseBot chooseBot(Fields field) {
        Map<String, BaseBot.BaseBotFactory> botFactoryMap = new HashMap<>();
        botFactoryMap.put("Test", new TestBot.TestBotFactory());
        botFactoryMap.put("Random", new RandomBot.RandomBotFactory());
        botFactoryMap.put("Player", new PlayerBot.PlayerBotFactory());
        System.out.println("Choose your bot: Test, Random, Player");
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String botTypeString = scanner.next();
                return botFactoryMap.get(botTypeString).createBot(field);
            } catch (IllegalArgumentException | GameLogicException e) {
                System.out.println("Incorrect bot type!!!");
                System.out.println("Choose your bot: Test, Random, Player");
            }
        }
    }

    private void downService() {
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
    /**
     * Первое сообщение - поле игрока
     * Второе сообщение - выбери армию
     * Далее приходит доска, если сервер требует сделать ход, или сообщение о конце игры
     */
    private void start() {
        try {
            String message = in.readLine();
            if (message.equals(CommonCommands.FIELD_ONE.command)) {
                player = chooseBot(Fields.PLAYER_ONE);
            } else {
                player = chooseBot(Fields.PLAYER_TWO);
            }
            while (true) {
                message = in.readLine();
                if (message == null) { continue; }
                if (message.equals(CommonCommands.GET_ARMY.command)) {
                    out.write(sendArmyJson() + '\n');
                    out.flush();
                }
                else if(message.equals(CommonCommands.END_GAME.command)){
                    // TODO: победа или поражение
                    downService();
                    break;
                }
                else if (message.equals(CommonCommands.MAX_ROOMS.command)) {
                    // TODO: можно писать причину
                    downService();
                    break;
                }
                else {
                    out.write(sendAnswerJson(message) + '\n');
                    out.flush();
                }
            }
        } catch (IOException | GameLogicException e) {
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