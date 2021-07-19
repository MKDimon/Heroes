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

    private final ConcurrentLinkedQueue<RoomsClient> clients = new ConcurrentLinkedQueue<>();

    private class RoomsClient extends Thread {
        private int id;
        public final Server server;
        public final Socket socket;
        public final BufferedWriter out;
        public final BufferedReader in;

        private RoomsClient(Server server, Socket socket) throws IOException {
            this.server = server;
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            while (true) {
                try {
                    out.write(Serializer.serializeData(new Data(CommonCommands.GET_ROOM)) + '\n');
                    out.flush();

                    id = Integer.parseInt(in.readLine());
                    break;
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * Комната с игрой
     */
    private class Rooms extends Thread {
        private final int id;
        private boolean findPlayers = true;

        private final Server server;

        private RoomsClient playerOne;
        private RoomsClient playerTwo;

        private final GameLogic gameLogic;
        private final Map<Fields, RoomsClient> getPlayer = new HashMap<>();

        private Rooms() {
            id = 0;
            server = null;
            gameLogic = null;
        }

        private Rooms(final Server server, final int id) {
            this.server = server;
            gameLogic = new GameLogic();
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while(findPlayers) {
                    boolean playersReady = false;
                    while (!playersReady) {
                        for (RoomsClient rc : server.clients) {
                            if (rc.id == id) {
                                server.clients.remove(rc);
                                if (playerOne == null) {
                                    playerOne = rc;
                                } else {
                                    playerTwo = rc;
                                    playersReady = true;
                                    break;
                                }
                            }
                        }
                    }

                    getPlayer.put(Fields.PLAYER_ONE, playerOne);
                    getPlayer.put(Fields.PLAYER_TWO, playerTwo);

                    Data data;
                    sendAsk(Serializer.serializeData(new Data(CommonCommands.FIELD_ONE)), playerOne.out);
                    sendAsk(Serializer.serializeData(new Data(CommonCommands.FIELD_TWO)), playerTwo.out);

                    // Выдача армий и отрисовка их на поле
                    sendAsk(Serializer.serializeData(new Data(CommonCommands.GET_ARMY)), playerOne.out);
                    Army one = Deserializer.deserializeData(playerOne.in.readLine()).army;

                    data = new Data(CommonCommands.DRAW, new Board(one, Fields.PLAYER_ONE));
                    sendDraw(Serializer.serializeData(data), playerOne.out, playerOne.in);
                    sendDraw(Serializer.serializeData(data), playerTwo.out, playerTwo.in);

                    sendAsk(Serializer.serializeData(new Data(CommonCommands.GET_ARMY, one)), playerTwo.out);
                    Army two = Deserializer.deserializeData(playerTwo.in.readLine()).army;

                    gameLogic.gameStart(one, two);
                    data = new Data(CommonCommands.DRAW, gameLogic.getBoard());
                    sendDraw(Serializer.serializeData(data), playerOne.out, playerOne.in);
                    sendDraw(Serializer.serializeData(data), playerTwo.out, playerTwo.in);

                    // весь игровой процесс
                    Answer answer;
                    while (gameLogic.isGameBegun()) {
                        data = new Data(CommonCommands.GET_ANSWER, gameLogic.getBoard());
                        sendAsk(Serializer.serializeData(data),
                                getPlayer.get(gameLogic.getBoard().getCurrentPlayer()).out
                        );

                        String str = getPlayer.get(gameLogic.getBoard().getCurrentPlayer()).in.readLine();
                        answer = Deserializer.deserializeData(str).answer;

                        gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());
                        data = new Data(CommonCommands.DRAW, one, gameLogic.getBoard(), answer);
                        sendDraw(Serializer.serializeData(data), playerOne.out, playerOne.in);
                        sendDraw(Serializer.serializeData(data), playerTwo.out, playerTwo.in);
                    }

                    sendAsk(Serializer.serializeData(new Data(CommonCommands.END_GAME)), playerOne.out);
                    sendAsk(Serializer.serializeData(new Data(CommonCommands.END_GAME)), playerTwo.out);

                    this.endGame(CommonCommands.END_GAME);
                }
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
         * Закрытие комнаты
         */
        private void downService(final CommonCommands command) {
            findPlayers = false;
            endGame(command);
        }

        private void endGame(final CommonCommands command) {
            try {
                closePlayer(command, playerOne);
                closePlayer(command, playerTwo);
                playerOne = null;
                playerTwo = null;
            } catch (IOException e) {
            }
        }

        private void closePlayer(CommonCommands command, RoomsClient player) throws IOException {
            if (!player.socket   .isClosed()) {
                if (command == CommonCommands.MAX_ROOMS) {
                    sendAsk(Serializer.serializeData(new Data(CommonCommands.MAX_ROOMS)), player.out);
                }
                player.socket.close();
                player.in.close();
                player.out.close();
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public void startServer() throws IOException {
        System.out.println(String.format("Server started, port: %d", PORT));
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            for (int i = 1; i <= maxRooms; i++) {
                Rooms room = new Rooms(this, i);
                getRoom.put(i, room);
                room.start();
            }
            while (true) {
                final Socket socket = serverSocket.accept();
                RoomsClient client = new RoomsClient(this, socket);
                client.start();
                clients.add(client);
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
