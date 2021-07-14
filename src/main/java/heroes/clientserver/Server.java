package heroes.clientserver;

import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
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
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Сервер
 */
public class Server {
    Logger logger = LoggerFactory.getLogger(Server.class);

    private final int PORT;
    private final int maxRooms;
    private int countRooms = 0;

    public Server(int PORT, int maxRooms) {
        this.maxRooms = maxRooms;
        this.PORT = PORT;
    }

    private final ConcurrentLinkedQueue<GUI> guiList = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<Rooms> serverList = new ConcurrentLinkedQueue<>();

    /**
     * GUI сервер, отвечает за прием ClientGUI
     */
    private class GUIThread extends Thread {
        private final int PORT;
        private final Server server;

        private GUIThread(final int PORT, final Server server) {
            this.PORT = PORT;
            this.server = server;
        }

        @SuppressWarnings("InfiniteLoopStatement")
        @Override
        public void run() {
            System.out.println(String.format("Server started, port: %d", PORT));
            try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
                while (true) {
                    final Socket socket = serverSocket.accept();
                    try {
                        server.guiList.add(new GUI(socket, server));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * GUI клиент установленный за комнатой
     */
    private class GUI {
        private final Server server;
        private final Socket socket;
        private final BufferedWriter out;
        private final BufferedReader in;

        public final int id;

        public GUI(Socket socket, Server server) throws IOException {
            this.socket = socket;
            this.server = server;

            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            out.write("GET_ROOM" + '\n');
            out.flush();

            id = Integer.parseInt(in.readLine());
        }

        public boolean send(final String message) throws IOException {
            out.write(message + '\n');
            out.flush();
            return CommonCommands.DRAW_SUCCESSFUL.command.equals(in.readLine());
        }

        private void downService() {
            try {
                if (!socket.isClosed()) {
                    send(CommonCommands.MAX_ROOMS.command);
                    socket.close();
                    in.close();
                    out.close();
                }
                if (server.guiList.contains(this)) {
                    server.guiList.remove(this);
                }
            } catch (final IOException ignored) {
            }
        }
    }

    /**
     * Комната с игрой
     */
    private class Rooms extends Thread {
        private final int id;

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

        /**
         * Для общения с клиентом необходим сокет (адресные данные)
         *
         * @param server сервер
         * @param socketOne сокет
         * @param socketTwo сокет
         */
        private Rooms(final Server server, final Socket socketOne, final Socket socketTwo, final int id) throws IOException {
            this.id = id;
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
                    downService(CommonCommands.MAX_ROOMS);
                    return;
                }
                countRooms++;
                sendAsk(CommonCommands.FIELD_ONE.command, outPlayerOne);
                sendAsk(CommonCommands.FIELD_TWO.command, outPlayerTwo);

                sendAsk(CommonCommands.GET_ARMY.command, outPlayerOne);
                Army one = Deserializer.deserializeArmy(inPlayerOne.readLine());
                // TODO: sendDraw();

                sendAsk(CommonCommands.GET_ARMY.command, outPlayerTwo);
                Army two = Deserializer.deserializeArmy(inPlayerTwo.readLine());
                // TODO: sendDraw();

                gameLogic.gameStart(one, two);

                // весь игровой процесс
                while (gameLogic.isGameBegun()) {
                    sendAsk(Serializer.serializeBoard(gameLogic.getBoard()),
                            getOuter.get(gameLogic.getBoard().getCurrentPlayer())
                    );

                    String str = getReader.get(gameLogic.getBoard().getCurrentPlayer()).readLine();
                    Answer answer = Deserializer.deserializeAnswer(
                            str
                    );

                    gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());

                    sendDraw();
                }

                sendAsk(CommonCommands.END_GAME.command, outPlayerOne);
                sendAsk(CommonCommands.END_GAME.command, outPlayerTwo);

                this.downService(CommonCommands.END_GAME);
            } catch (final IOException | UnitException e) {
                this.downService(CommonCommands.END_GAME);
            }//*/
        }

        private void sendDraw() throws IOException {
            for (final GUI item: guiList) {
                if (item.id == this.id) {
                    if (!item.send(Serializer.serializeBoard(gameLogic.getBoard()))) {
                        //downService(CommonCommands.END_GAME);
                    }
                }
            }
        }

        /**
         *  Отправляет сериализованное сообщение клиенту
         */
        private void sendAsk(final String message, final BufferedWriter out) throws IOException {
            out.write(message + '\n');
            out.flush();
        }

        /**
         * закрытие сервера, удаление себя из списка нитей
         */
        private void downService(CommonCommands command) {
            try {
                if (!socketOne.isClosed()) {
                    if (command == CommonCommands.MAX_ROOMS) {
                        sendAsk(CommonCommands.MAX_ROOMS.command, outPlayerOne);
                    }
                    socketOne.close();
                    inPlayerOne.close();
                    outPlayerOne.close();
                }
                if (!socketTwo.isClosed()) {
                    if (command == CommonCommands.MAX_ROOMS) {
                        sendAsk(CommonCommands.MAX_ROOMS.command, outPlayerTwo);
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
            int currentNumRoom = 1;
            new GUIThread(Deserializer.getConfig().GUI_PORT, this).start();
            while (true) {
                // Блокируется до возникновения нового соединения
                // Ждет первого игрока
                final Socket socketOne = serverSocket.accept();
                final Socket socketTwo = serverSocket.accept();
                try {
                    new Rooms(this, socketOne, socketTwo, currentNumRoom).start();
                    currentNumRoom++;
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
