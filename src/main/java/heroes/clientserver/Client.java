package heroes.clientserver;

import com.googlecode.lanterna.graphics.TextGraphics;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Fields;
import heroes.gui.TerminalWrapper;
import heroes.player.BaseBot;
import heroes.player.PlayerBot;
import heroes.player.RandomBot;
import heroes.player.TestBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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

    //Метод, который вызывает у игрока создание армии
    private String sendArmyJson() throws IOException {
        return Serializer.serializeData(new Data(null, player.getArmy()));
    }

    //Метод, который вызывает у игрока ответ
    private String sendAnswerJson(final String json) throws GameLogicException, IOException {
        return Serializer.serializeData(
                new Data(player.getAnswer(Deserializer.deserializeData(json).board))
        );
    }

    private BaseBot chooseBot(final Fields field) {
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
     * второе сообщение - выбери бота
     * Третье сообщение - выбери армию
     * Далее приходит доска, если сервер требует сделать ход, или сообщение о конце игры
     */
    private void start() {
        try {//Первое сообщение  - поле игрока
            TerminalWrapper tw = new TerminalWrapper();
            tw.start();
            TextGraphics tg = tw.getScreen().newTextGraphics();
            String message = null;
            Data data = new Data();

            int i = 0;
            while (true) {
                if (in.ready()) {
                    message = in.readLine();
                    data = Deserializer.deserializeData(message);
                }
                if (message == null) { continue; }

                if (CommonCommands.FIELD_ONE.equals(data.command)){
                    player = chooseBot(Fields.PLAYER_ONE);
                    data = new Data(data);
                }
                else if (CommonCommands.FIELD_TWO.equals(data.command)) {
                    player = chooseBot(Fields.PLAYER_TWO);
                    data = new Data(data);
                }
                else if (CommonCommands.GET_ARMY.equals(data.command)) {
                    // TODO: армия на основе армии противника
                    // У игрока 2 прилетает армия 1 вместе с запросом
                    out.write(sendArmyJson() + '\n');
                    out.flush();
                    data = new Data(data);
                }
                else if(CommonCommands.END_GAME.equals(data.command)){
                    // TODO: победа или поражение
                    out.write(CommonCommands.DRAW_SUCCESSFUL.command + '\n');
                    out.flush();
                    data = new Data(data);
                }
                else if (CommonCommands.MAX_ROOMS.equals(data.command)) {
                    // TODO: можно писать причину
                    downService();
                    break;
                }
                else if (CommonCommands.GET_ANSWER.equals(data.command)){
                    out.write(sendAnswerJson(message) + '\n');
                    out.flush();
                    data = new Data(data);
                }
                else if (CommonCommands.GET_ROOM.equals(data.command)) {
                    logger.info(message);
                    // TODO: выбор комнаты, пока что рандом или 1 комната
                    int id = new Random().nextInt(Deserializer.getConfig().MAX_ROOMS);
                    out.write("1" + '\n');
                    out.flush();
                    data = new Data(data);
                }
                else if (CommonCommands.DRAW.equals(data.command)){
                    //  logger.info("BOARD TO DRAW");
                    out.write(CommonCommands.DRAW_SUCCESSFUL.command + '\n');
                    out.flush();
                    data = new Data(data);
                }

                if (data.board != null) {
                    tw.update(data.answer, data.board);
                }
                // типа кадры смотрим
                tg.putString(55, tw.getTerminal().getTerminalSize().getRows() -
                        (int)((tw.getTerminal().getTerminalSize().getRows() - 1) * 0.3), String.valueOf(i));
                tw.getScreen().refresh();
                i++;
                if (i > 1000) i = 0;
            }
        } catch (IOException | NullPointerException | GameLogicException | UnitException | BoardException e) {
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