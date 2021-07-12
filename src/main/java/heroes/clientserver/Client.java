package heroes.clientserver;

import com.fasterxml.jackson.core.JsonProcessingException;
import heroes.auxiliaryclasses.GameLogicException;
import heroes.gamelogic.Fields;
import heroes.player.IPlayer;
import heroes.player.RandomBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class Client {
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    private static final String IP = "127.0.0.1";
    private static final int PORT = 8081;

    private final String ip;
    private final int port;
    //Клиент хранит ссылку на своего бота, чтобы вызывать у него ответы
    private IPlayer player;

    private Socket socket = null;
    private BufferedReader in = null; // поток чтения из сокета
    private BufferedWriter out = null; // поток записи в сокет

    public static void main(String[] args) {
        Client client = new Client(IP, PORT, null);
        client.startClient();
    }

    private Client(final String ip, final int port, IPlayer player){
        this.ip = ip;
        this.port = port;
        this.player = player;
    }

    private void startClient(){
        try{
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e){
            logger.error("Error client starting", e);
        }
        start();
    }

    //Метод, который вызывает у игрока создание армии
    private String sendArmyJson(){
        return Serializer.serializeArmy(player.getArmy());
    }

    //Метод, который вызывает у игрока ответ
    private String sendAnswerJson(String jsonBoard) throws GameLogicException, JsonProcessingException {
        return Serializer.serializeAnswer(player.getAnswer(Deserializer.deserializeBoard(jsonBoard)));
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            logger.error("Error service downing", e);
        }
    }

    public void start() {
        try {//Первое сообщение  - поле игрока
            String message = in.readLine();
            player = new RandomBot(Fields.valueOf(message));
            while (true) {
                message = in.readLine();
                if(message.equals(CommonCommands.GET_ARMY)){
                    out.write(sendArmyJson());
                }
                else if(message.equals(CommonCommands.END_GAME)){
                    downService();
                    break;
                } else {
                    out.write(sendAnswerJson(message));
                }
            }
        } catch (IOException | GameLogicException e){
            logger.error("Error client running", e);
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