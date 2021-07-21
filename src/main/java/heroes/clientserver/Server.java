package heroes.clientserver;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.serverexcetions.ServerException;
import heroes.auxiliaryclasses.serverexcetions.ServerExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.commands.CommandsTime;
import heroes.clientserver.commands.CommonCommands;
import heroes.gamelogic.*;
import heroes.player.Answer;
import heroes.statistics.StatisticsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
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
    private final Map<Integer, Rooms> getRoom;

    public Server(final int PORT,final  int maxRooms) {
        this.maxRooms = maxRooms;
        this.PORT = PORT;
        getRoom = new Hashtable<>();
    }

    private final ConcurrentLinkedQueue<RoomsClient> clients = new ConcurrentLinkedQueue<>();

    /**
     * Клиент комнаты
     */
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
                    Data data = new Data(CommonCommands.GET_ROOM);
                    socket.setSoTimeout(CommandsTime.getTime(data.command));
                    out.write(Serializer.serializeData(data) + '\n');
                    out.flush();

                    id = Integer.parseInt(in.readLine());
                    break;
                } catch (final SocketTimeoutException e) {
                    server.clients.remove(this);
                    logger.error(ServerExceptionType.ERROR_SERVER_ROOM_CHANGED.getErrorType(), e);
                } catch (IOException e) {
                    logger.error(ServerExceptionType.ERROR_SERVER_ROOM_CHANGED.getErrorType(), e);
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

        private Rooms(final Server server, final int id) {
            this.server = server;
            gameLogic = new GameLogic();
            this.id = id;
        }

        @Override
        public void run() {
            try {
                while(findPlayers) {
                    try {
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

                        StatisticsCollector collector = new StatisticsCollector(id);

                        // выдача полей
                        getPlayer.put(Fields.PLAYER_ONE, playerOne);
                        getPlayer.put(Fields.PLAYER_TWO, playerTwo);

                        Data data;
                        sendAsk(new Data(CommonCommands.FIELD_ONE), playerOne);
                        sendAsk(new Data(CommonCommands.FIELD_TWO), playerTwo);

                        // Выдача армий и отрисовка их на поле
                        // Первая армия
                        sendAsk(new Data(CommonCommands.GET_ARMY), playerOne);
                        Army one = Deserializer.deserializeData(playerOne.in.readLine()).army;

                        // Отрисовка
                        data = new Data(CommonCommands.DRAW, new Board(one, Fields.PLAYER_ONE));
                        sendDraw(data, playerOne);
                        sendDraw(data, playerTwo);

                        // Вторая армия
                        sendAsk(new Data(CommonCommands.GET_ARMY, one), playerTwo);
                        Army two = Deserializer.deserializeData(playerTwo.in.readLine()).army;

                        gameLogic.gameStart(one, two);

                        collector.recordMessageToCSV("GAME START\n");
                        collector.recordArmyToCSV(Fields.PLAYER_ONE, one);
                        collector.recordArmyToCSV(Fields.PLAYER_TWO, two);

                        // Отрисовка
                        data = new Data(CommonCommands.DRAW, gameLogic.getBoard());
                        sendDraw(data, playerOne);
                        sendDraw(data, playerTwo);

                        // весь игровой процесс
                        Answer answer;
                        while (gameLogic.isGameBegun()) {
                            // Ожидание ответа
                            data = new Data(CommonCommands.GET_ANSWER, gameLogic.getBoard());
                            sendAsk(data,getPlayer.get(gameLogic.getBoard().getCurrentPlayer()));

                            String str = getPlayer.get(gameLogic.getBoard().getCurrentPlayer()).in.readLine();
                            answer = Deserializer.deserializeData(str).answer;

                            //для статистики
                            int defenderHP = gameLogic.getBoard().getUnitByCoordinate(answer.getDefender()).getCurrentHP();

                            // логика
                            gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());

                            // статистика
                            collector.recordActionToCSV(answer.getAttacker(), answer.getDefender(), answer.getActionType(),
                                    gameLogic.getBoard().getUnitByCoordinate(answer.getAttacker()),
                                    gameLogic.getBoard().getUnitByCoordinate(answer.getDefender()), Math.abs(
                                            gameLogic.getBoard().getUnitByCoordinate(answer.getDefender()).getCurrentHP()
                                                    - defenderHP));
                            // Отрисовка
                            data = new Data(CommonCommands.DRAW, one, gameLogic.getBoard(), answer);
                            sendDraw(data, playerOne);
                            sendDraw(data, playerTwo);
                        }

                        data = new Data(CommonCommands.END_GAME, gameLogic.getBoard());
                        sendAsk(data, playerOne);
                        sendAsk(data, playerTwo);

                        GameStatus status = gameLogic.getBoard().getStatus();
                        // статистика
                        collector.recordMessageToCSV(new StringBuffer().append("\n").append(gameLogic.getBoard().getCurNumRound()).
                                append(",").toString());
                        switch (status) {
                            case PLAYER_ONE_WINS -> collector.recordMessageToCSV(Fields.PLAYER_ONE.toString());
                            case PLAYER_TWO_WINS -> collector.recordMessageToCSV(Fields.PLAYER_TWO.toString());
                            case NO_WINNERS -> collector.recordMessageToCSV("DEAD HEAT");
                        }
                        collector.recordMessageToCSV("\nGAME OVER\n");

                        this.endGame();
                    }
                    catch ( final ServerException | SocketTimeoutException | UnitException | BoardException e) {
                        logger.error(ServerExceptionType.ERROR_GAME_RUNNING.getErrorType(), e);
                        gameLogic.getBoard().setStatus(GameStatus.NO_WINNERS);
                        this.endGame();
                    }
                }
            }catch (final IOException e) {
                logger.error(ServerExceptionType.ERROR_ROOM_RUNNING.getErrorType(), e);
                if (gameLogic != null) {
                    gameLogic.getBoard().setStatus(GameStatus.NO_WINNERS);
                }
                this.downService();
            }
        }

        /**
         *  Отправляет сериализованное сообщение клиенту
         *
         *  МЕНЯЕТ ВРЕМЯ ОЖИДАНИЯ В СОКЕТЕ ИГРОКА
         */
        private void sendAsk(final Data data, final RoomsClient player) throws IOException {
            player.socket.setSoTimeout(CommandsTime.getTime(data.command));
            player.out.write(Serializer.serializeData(data) + '\n');
            player.out.flush();
        }

        /**
         * Отправляет отрисовку и ждет ответ,
         *
         * МЕНЯЕТ ВРЕМЯ ОЖИДАНИЯ В СОКЕТЕ ИГРОКА
         */
        public void sendDraw(final Data data, final RoomsClient player) throws ServerException, IOException {
            player.socket.setSoTimeout(CommandsTime.getTime(CommonCommands.DRAW));
            player.out.write(Serializer.serializeData(data) + '\n');
            player.out.flush();
            if (CommonCommands.DRAW_UNSUCCESSFUL.command.equals(player.in.readLine())) {
                throw new ServerException(ServerExceptionType.ERROR_DRAWING);
            }
        }

        /**
         * Закрытие комнаты
         */
        private void downService() {
            findPlayers = false;
            endGame();
        }

        private void endGame() {
            try {
                closePlayer(playerOne);
                closePlayer(playerTwo);
                playerOne = null;
                playerTwo = null;
            } catch (IOException e) {
                logger.error(ServerExceptionType.ERROR_SEND_ENDGAME.getErrorType(), e);
            }
        }

        private void closePlayer(RoomsClient player) throws IOException {
            if (!player.socket.isClosed()) {
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
            serverSocket.setSoTimeout(1000);
            for (int i = 1; i <= maxRooms; i++) {
                Rooms room = new Rooms(this, i);
                getRoom.put(i, room);
                room.start();
            }
            while (true) {
                try {
                    final Socket socket = serverSocket.accept();
                    RoomsClient client = new RoomsClient(this, socket);
                    client.start();
                    clients.add(client);
                }
                catch (final SocketTimeoutException e) {
                    logger.debug("Socket is not accept((", e);
                }
            }
        } catch (final BindException e) {
            logger.error(ServerExceptionType.ERROR_SERVER_RUNNING.getErrorType(), e);
        }
    }

    public static void main(final String[] args) throws IOException {
        ServersConfigs sc = Deserializer.getConfig();
        final Server server = new Server(sc.PORT, sc.MAX_ROOMS);
        server.startServer();
    }
}
