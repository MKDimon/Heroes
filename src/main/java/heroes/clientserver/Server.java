package heroes.clientserver;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.commands.CommonCommands;
import heroes.gamelogic.Army;
import heroes.gamelogic.Board;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.player.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Сервер
 */
public class Server {
    Logger logger = LoggerFactory.getLogger(Server.class);

    private final int PORT;
    private final int maxRooms;
    private int countRooms = 0;
    private final Map<Integer, Rooms> getRoom;

    public Server(final int PORT,final  int maxRooms) {
        this.maxRooms = maxRooms;
        this.PORT = PORT;
        getRoom = new Hashtable<>();
    }

    private final ConcurrentLinkedQueue<Rooms> serverList = new ConcurrentLinkedQueue<>();

    /**
     * Комната с игрой
     */
    private class Rooms extends Thread {
        private int id;

        private final Server server;
        private final Socket socketOne;
        private final Socket socketTwo;

        private final BufferedReader inPlayerOne; // поток чтения из сокета
        private final BufferedWriter outPlayerOne; // поток записи в сокет
        private final BufferedReader inPlayerTwo; // поток чтения из сокета
        private final BufferedWriter outPlayerTwo; // поток записи в сокет

        private final GameLogic gameLogic;
        private final Map<Fields, BufferedWriter> getOuter;
        private final Map<Fields, BufferedReader> getReader;

        private Rooms() {
            server = null;
            socketOne = null;
            socketTwo = null;
            inPlayerOne = null;
            outPlayerOne = null;
            inPlayerTwo = null;
            outPlayerTwo = null;
            gameLogic = null;
            getOuter = null;
            getReader = null;
        }

        /**
         * Для общения с клиентом необходим сокет (адресные данные)
         *
         * @param server сервер
         * @param socketOne сокет
         * @param socketTwo сокет
         */
        private Rooms(final Server server, final Socket socketOne, final Socket socketTwo) throws IOException {
            for (int i = 1; i <= maxRooms; i++) {
                if (getRoom.get(i) == getRoom.get(-1)) {
                    this.id = i;
                    getRoom.put(i, this);
                    break;
                }
            }
            this.server = server;
            this.socketOne = socketOne;
            this.socketTwo = socketTwo;
            this.getOuter = new HashMap<>();
            this.getReader = new HashMap<>();

            logger.warn(String.valueOf(socketOne));
            logger.warn(socketTwo.toString());
            logger.warn(String.valueOf(this.getThreadGroup()));

            // если потоку ввода/вывода приведут к генерированию исключения, оно проброситься дальше
            inPlayerOne = new BufferedReader(new InputStreamReader(socketOne.getInputStream()));
            outPlayerOne = new BufferedWriter(new OutputStreamWriter(socketOne.getOutputStream()));
            inPlayerTwo = new BufferedReader(new InputStreamReader(socketTwo.getInputStream()));
            outPlayerTwo = new BufferedWriter(new OutputStreamWriter(socketTwo.getOutputStream()));

            getOuter.put(Fields.PLAYER_ONE, outPlayerOne);
            getOuter.put(Fields.PLAYER_TWO, outPlayerTwo);
            getReader.put(Fields.PLAYER_ONE, inPlayerOne);
            getReader.put(Fields.PLAYER_TWO, inPlayerTwo);

            gameLogic = new GameLogic();
        }

        @Override
        public void run() {
            server.serverList.add(this);
            try {
                if (countRooms >= maxRooms) {
                    logger.warn("MAX ROOM");
                    downService(CommonCommands.MAX_ROOMS);
                    return;
                }
                countRooms++;
                Data data;
                sendAsk(Serializer.serializeData(new Data(CommonCommands.FIELD_ONE)), outPlayerOne);
                sendAsk(Serializer.serializeData(new Data(CommonCommands.FIELD_TWO)), outPlayerTwo);

                // Выдача армий и отрисовка их на поле
                sendAsk(Serializer.serializeData(new Data(CommonCommands.GET_ARMY)), outPlayerOne);
                Army one = Deserializer.deserializeData(inPlayerOne.readLine()).army;

                data = new Data(CommonCommands.DRAW, new Board(one, Fields.PLAYER_ONE));
                sendDraw(Serializer.serializeData(data), outPlayerOne, inPlayerOne);
                sendDraw(Serializer.serializeData(data), outPlayerTwo, inPlayerTwo);

                sendAsk(Serializer.serializeData(new Data(CommonCommands.GET_ARMY, one)), outPlayerTwo);
                Army two = Deserializer.deserializeData(inPlayerTwo.readLine()).army;

                gameLogic.gameStart(one, two);
                data = new Data(CommonCommands.DRAW, gameLogic.getBoard());
                sendDraw(Serializer.serializeData(data), outPlayerOne, inPlayerOne);
                sendDraw(Serializer.serializeData(data), outPlayerTwo, inPlayerTwo);

                // весь игровой процесс
                Answer answer;
                while (gameLogic.isGameBegun()) {
                    data = new Data(CommonCommands.GET_ANSWER, gameLogic.getBoard());
                    sendAsk(Serializer.serializeData(data),
                            getOuter.get(gameLogic.getBoard().getCurrentPlayer())
                    );

                    String str = getReader.get(gameLogic.getBoard().getCurrentPlayer()).readLine();
                    answer = Deserializer.deserializeData(str).answer;

                    gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
                    data = new Data(CommonCommands.DRAW, one, gameLogic.getBoard(), answer);
                    sendDraw(Serializer.serializeData(data), outPlayerOne, inPlayerOne);
                    sendDraw(Serializer.serializeData(data), outPlayerTwo, inPlayerTwo);
                }

                sendAsk(Serializer.serializeData(new Data(CommonCommands.END_GAME)), outPlayerOne);
                sendAsk(Serializer.serializeData(new Data(CommonCommands.END_GAME)), outPlayerTwo);

                this.downService(CommonCommands.END_GAME);
            } catch (final IOException | UnitException | BoardException e) {
                this.downService(CommonCommands.END_GAME);
            }//*/
        }

        /**
         *  Отправляет сериализованное сообщение клиенту
         */
        private void sendAsk(final String message, final BufferedWriter out) throws IOException {
            out.write(message + '\n');
            out.flush();
        }

        public void sendDraw(final String message, final BufferedWriter out, final BufferedReader in) throws IOException {
            out.write(message + '\n');
            out.flush();
            if (CommonCommands.DRAW_UNSUCCESSFUL.command.equals(in.readLine())) {
                throw new IOException("Error drawing");
            }
        }

        /**
         * закрытие сервера, удаление себя из списка нитей
         * гуи остается следить за комнатой
         */
        private void downService(final CommonCommands command) {
            try {
                getRoom.put(id, getRoom.get(-1));
                if (!socketOne.isClosed()) {
                    if (command == CommonCommands.MAX_ROOMS) {
                        sendAsk(Serializer.serializeData(new Data(CommonCommands.MAX_ROOMS)), outPlayerOne);
                    }
                    socketOne.close();
                    inPlayerOne.close();
                    outPlayerOne.close();
                }
                if (!socketTwo.isClosed()) {
                    if (command == CommonCommands.MAX_ROOMS) {
                        sendAsk(Serializer.serializeData(new Data(CommonCommands.MAX_ROOMS)), outPlayerTwo);
                    }
                    socketTwo.close();
                    inPlayerTwo.close();
                    outPlayerTwo.close();
                }
                if (server.serverList.contains(this)) {
                    server.serverList.remove(this);
                    if (command == CommonCommands.END_GAME) {
                        countRooms--;
                    }
                }
            } catch (final IOException ignored) {
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void startServer() throws IOException {
        System.out.println(String.format("Server started, port: %d", PORT));
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            getRoom.put(-1, new Rooms());
            for (int i = 0; i <= maxRooms; i++) {
                getRoom.put(i, getRoom.get(-1));
            }
            while (true) {
                // Блокируется до возникновения нового соединения
                // Ждет первого игрока
                final Socket socketOne = serverSocket.accept();
                final Socket socketTwo = serverSocket.accept();
                try {
                    new Rooms(this, socketOne, socketTwo).start();
                } catch (final IOException e) {
                    socketTwo.close();
                    socketOne.close();
                }
            }
        } catch (final BindException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) throws IOException {
        ServersConfigs sc = Deserializer.getConfig();
        final Server server = new Server(sc.PORT, sc.MAX_ROOMS);
        server.startServer();
    }
}
