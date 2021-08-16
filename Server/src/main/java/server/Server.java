package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.auxiliaryclasses.boardexception.BoardException;
import server.serverexcetions.ServerException;
import server.serverexcetions.ServerExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.Data;
import heroes.clientserver.Deserializer;
import heroes.clientserver.Serializer;
import heroes.clientserver.ServersConfigs;
import heroes.commands.CommandsTime;
import heroes.commands.CommonCommands;
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
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final int PORT;
    private final int maxRooms;
    private final int delay;
    private final Map<Integer, Rooms> getRoom;

    public Server(final int PORT, final int maxRooms, final int delay) {
        this.maxRooms = maxRooms;
        this.PORT = PORT;
        this.delay = delay;
        getRoom = new Hashtable<>();
    }

    /**
     * Парсит serverConfig.json из каталога и возвращает конфиги сервера
     *
     * @return все нужные конфиги
     * @throws IOException json
     */
    public static ServersConfigs getServersConfig() throws IOException {
        final FileInputStream fileInputStream = new FileInputStream("serverConfig.json");

        final ServersConfigs sc = new ObjectMapper().readValue(fileInputStream, ServersConfigs.class);
        fileInputStream.close();
        return sc;
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

        private RoomsClient(final Server server, final Socket socket) throws IOException {
            this.server = server;
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            while (true) {
                try {
                    final Data data = new Data(CommonCommands.GET_ROOM, server.maxRooms);
                    socket.setSoTimeout(CommandsTime.getTime(data.command));
                    out.write(Serializer.serializeData(data) + '\n');
                    out.flush();

                    id = Integer.parseInt(in.readLine());
                    break;
                } catch (final SocketTimeoutException e) {
                    server.clients.remove(this);
                    logger.error(ServerExceptionType.ERROR_SERVER_ROOM_CHANGED.getErrorType(), e);
                    break;
                } catch (final IOException e) {
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

        private void waitPlayers() {
            boolean playersReady = false;
            while (!playersReady) {
                for (final RoomsClient rc : server.clients) {
                    if (rc.id == id) {
                        server.clients.remove(rc);
                        if (playerOne == null || playerOne.socket.isClosed()) {
                            playerOne = rc;
                        } else if (playerTwo == null || playerTwo.socket.isClosed()) {
                            playerTwo = rc;
                        }
                        if (playerOne != null && playerTwo != null &&
                                !playerOne.socket.isClosed() && !playerTwo.socket.isClosed()) {
                            playersReady = true;
                            break;
                        }
                    }
                }
            }
        }

        private void gameStart(final StatisticsCollector collector) throws IOException, ServerException,
                UnitException, BoardException {
            // выдача полей
            getPlayer.put(Fields.PLAYER_ONE, playerOne);
            getPlayer.put(Fields.PLAYER_TWO, playerTwo);

            sendAsk(new Data(CommonCommands.FIELD_ONE), playerOne);
            sendAsk(new Data(CommonCommands.FIELD_TWO), playerTwo);

            // Первая армия
            sendAsk(new Data(CommonCommands.GET_ARMY), playerOne);
            final Army one = Deserializer.deserializeData(playerOne.in.readLine()).army;

            // Отрисовка
            final Data data = new Data(CommonCommands.DRAW, new Board(one, Fields.PLAYER_ONE));
            sendDraw(data, playerOne);
            sendDraw(data, playerTwo);

            // Вторая армия
            sendAsk(new Data(CommonCommands.GET_ARMY, one), playerTwo);
            final Army two = Deserializer.deserializeData(playerTwo.in.readLine()).army;

            gameLogic.gameStart(one, two);

            //статистика
            collector.recordMessageToCSV("GAME START\n");
            collector.recordArmyToCSV(Fields.PLAYER_ONE, one);
            collector.recordArmyToCSV(Fields.PLAYER_TWO, two);

            // Отрисовка
            final Data drawingData = new Data(CommonCommands.DRAW, gameLogic.getBoard());
            sendDraw(drawingData, playerOne);
            sendDraw(drawingData, playerTwo);
        }

        private void gameRun(final StatisticsCollector collector) throws IOException, UnitException,
                ServerException {
            // Ожидание ответа
            final Data data = new Data(CommonCommands.GET_ANSWER, gameLogic.getBoard());
            sendAsk(data, getPlayer.get(gameLogic.getBoard().getCurrentPlayer()));

            final String str = getPlayer.get(gameLogic.getBoard().getCurrentPlayer()).in.readLine();
            final Answer answer = Deserializer.deserializeData(str).answer;

            //для статистики
            final int defenderHP = gameLogic.getBoard().getUnitByCoordinate(answer.getDefender()).getCurrentHP();

            // логика
            final boolean isActionSuccess = gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());

            // статистика
            if (isActionSuccess) {
                collector.recordActionToCSV(answer.getAttacker(), answer.getDefender(), answer.getActionType(),
                        gameLogic.getBoard().getUnitByCoordinate(answer.getAttacker()),
                        gameLogic.getBoard().getUnitByCoordinate(answer.getDefender()), Math.abs(
                                gameLogic.getBoard().getUnitByCoordinate(answer.getDefender()).getCurrentHP()
                                        - defenderHP));
            }
            // Отрисовка
            final Data gameData = new Data(CommonCommands.DRAW, gameLogic.getBoard(), answer);
            sendDraw(gameData, playerOne);
            sendDraw(gameData, playerTwo);
        }

        private void gameEnding(final StatisticsCollector collector) throws IOException {
            final Data data = new Data(CommonCommands.END_GAME, gameLogic.getBoard());
            sendAsk(data, playerOne);
            sendAsk(data, playerTwo);

            final GameStatus status = gameLogic.getBoard().getStatus();
            // статистика
            collector.recordMessageToCSV(new StringBuffer().append("\n").append(gameLogic.getBoard().getCurNumRound()).
                    append(",").toString());
            if (status == GameStatus.PLAYER_ONE_WINS) {
                collector.recordMessageToCSV(Fields.PLAYER_ONE.toString());
            } else if (status == GameStatus.PLAYER_TWO_WINS) {
                collector.recordMessageToCSV(Fields.PLAYER_TWO.toString());
            } else if (status == GameStatus.NO_WINNERS) {
                collector.recordMessageToCSV("DEAD HEAT");
            }
            collector.recordMessageToCSV("\nGAME OVER\n");

            this.endGame();
        }

        @Override
        public void run() {
                while (findPlayers) {
                    try {
                        waitPlayers();

                        final StatisticsCollector collector = new StatisticsCollector(id);

                        gameStart(collector);

                        // весь игровой процесс
                        while (gameLogic.isGameBegun()) {
                            gameRun(collector);
                            //noinspection BusyWait
                            Thread.sleep(server.delay);
                        }

                        gameEnding(collector);
                    } catch (final ServerException | UnitException
                            | BoardException | InterruptedException | IOException e) {
                        logger.error(ServerExceptionType.ERROR_GAME_RUNNING.getErrorType(), e);
                        gameLogic.getBoard().setStatus(GameStatus.NO_WINNERS);
                        this.endGame();
                    }
                }
        }

        /**
         * Отправляет сериализованное сообщение клиенту
         * <p>
         * МЕНЯЕТ ВРЕМЯ ОЖИДАНИЯ В СОКЕТЕ ИГРОКА
         */
        private void sendAsk(final Data data, final RoomsClient player) throws IOException {
            player.socket.setSoTimeout(CommandsTime.getTime(data.command));
            player.out.write(Serializer.serializeData(data) + '\n');
            player.out.flush();
        }

        /**
         * Отправляет отрисовку и ждет ответ,
         * <p>
         * МЕНЯЕТ ВРЕМЯ ОЖИДАНИЯ В СОКЕТЕ ИГРОКА
         */
        public void sendDraw(final Data data, final RoomsClient player) throws ServerException, IOException {
            sendAsk(data, player);
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

        private void closePlayer(final RoomsClient player) throws IOException {
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
                final Rooms room = new Rooms(this, i);
                getRoom.put(i, room);
                room.start();
            }
            while (true) {
                try {
                    final Socket socket = serverSocket.accept();
                    final RoomsClient client = new RoomsClient(this, socket);
                    client.start();
                    clients.add(client);
                } catch (final SocketTimeoutException e) {
                    logger.debug("Socket is not accept((", e);
                }
            }
        } catch (final BindException e) {
            logger.error(ServerExceptionType.ERROR_SERVER_RUNNING.getErrorType(), e);
        }
    }

    public static void main(final String[] args) throws IOException {
        final ServersConfigs sc = getServersConfig();
        final Server server = new Server(sc.PORT, sc.MAX_ROOMS, sc.DELAY);
        server.startServer();
    }
}
