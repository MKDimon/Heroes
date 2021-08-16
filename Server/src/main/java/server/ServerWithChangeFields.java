package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import heroes.auxiliaryclasses.boardexception.BoardException;
import server.serverexcetions.ServerException;
import server.serverexcetions.ServerExceptionType;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.clientserver.Data;
import heroes.clientserver.Deserializer;
import heroes.clientserver.Serializer;
import heroes.commands.CommandsTime;
import heroes.commands.CommonCommands;
import heroes.gamelogic.*;
import heroes.player.Answer;
import heroes.statistics.StatisticsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Сервер
 */
public class ServerWithChangeFields {
    private static final Logger logger = LoggerFactory.getLogger(ServerWithChangeFields.class);

    private final int PORT;
    private final int maxRooms;
    private final int delay;
    private final int gamesCount;
    private final int treads;

    private final String pathLog;
    private final String logBack;

    private final Map<Integer, Rooms> getRoom;

    public ServerWithChangeFields(final ServersConfigs serversConfigs) {
        maxRooms = serversConfigs.MAX_ROOMS;
        PORT = serversConfigs.PORT;
        delay = serversConfigs.DELAY;
        gamesCount = serversConfigs.GAMES_COUNT;
        treads = serversConfigs.THREADS;
        pathLog = serversConfigs.PATH_LOG;
        logBack = serversConfigs.LOGBACK;
        getRoom = new Hashtable<>();
    }

    private final ConcurrentLinkedQueue<RoomsClient> clients = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<RoomsClient> fieldOne = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<RoomsClient> fieldTwo = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<RoomsClient> bots = new ConcurrentLinkedQueue<>();

    /**
     * Парсит serverConfig.json из каталога и возвращает конфиги сервера
     *
     * @return все нужные конфиги
     * @throws IOException json
     */
    public static ServersConfigs getServersConfig() throws IOException {
        final FileInputStream fileInputStream = new FileInputStream("src/main/resources/serverConfig.json");

        final ServersConfigs sc = new ObjectMapper().readValue(fileInputStream, ServersConfigs.class);
        fileInputStream.close();
        return sc;
    }

    /**
     * Клиент комнаты
     */
    private class RoomsClient extends Thread {
        public boolean isUse = false;
        public final ServerWithChangeFields server;
        public final Socket socket;
        public final BufferedWriter out;
        public final BufferedReader in;
        public boolean withBot = false;
        public boolean isBot = false;

        private RoomsClient(final ServerWithChangeFields server, final Socket socket) throws IOException {
            this.server = server;
            this.socket = socket;
            this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        private void setOpponent() throws IOException {
            final Data opponent = new Data(CommonCommands.GET_OPPONENT);
            socket.setSoTimeout(CommandsTime.getTime(opponent.command));
            out.write(Serializer.serializeData(opponent) + '\n');
            out.flush();

            withBot = Deserializer.deserializeData(in.readLine()).info == 2;
        }

        private void setField() throws IOException {
            final Data data = new Data(CommonCommands.GET_FIELD);
            socket.setSoTimeout(CommandsTime.getTime(data.command));
            out.write(Serializer.serializeData(data) + '\n');
            out.flush();

            final int field = Deserializer.deserializeData(in.readLine()).info;
            if (field == 1) {
                server.fieldOne.add(this);
            } else if (field == 2)  {
                server.fieldTwo.add(this);
            } else {
                server.fieldOne.add(this);
                server.fieldTwo.add(this);
            }
        }

        @Override
        public void run() {
            while (true) {
                try {
                    setOpponent();
                    setField();
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

        private final ServerWithChangeFields server;

        private RoomsClient playerOne;
        private RoomsClient playerTwo;

        private final GameLogic gameLogic;
        private final Map<Fields, RoomsClient> getPlayer = new HashMap<>();

        private Rooms(final ServerWithChangeFields server, final int id) {
            this.server = server;
            gameLogic = new GameLogic();
            this.id = id;
        }

        private void waitPlayers() throws IOException {
            boolean playersReady = false;
            while (!playersReady) {
                for (final RoomsClient rc : server.fieldOne) {
                    if ((playerOne == null || playerOne.socket.isClosed()) && !rc.isUse) {
                        playerOne = rc;
                        playerOne.isUse = true;
                        server.fieldOne.remove(rc);
                        server.fieldTwo.remove(rc);
                        break;
                    }
                }
                for (final RoomsClient rc : server.fieldTwo) {
                    if ((playerTwo == null || playerTwo.socket.isClosed()) && !rc.isUse) {
                        playerTwo = rc;
                        playerTwo.isUse = true;
                        server.fieldOne.remove(rc);
                        server.fieldTwo.remove(rc);
                        break;
                    }
                }
                playersReady = playerOne != null && playerTwo != null &&
                        !playerOne.socket.isClosed() && !playerTwo.socket.isClosed();
            }
            // выдача полей
            getPlayer.put(Fields.PLAYER_ONE, playerOne);
            getPlayer.put(Fields.PLAYER_TWO, playerTwo);

            sendAsk(new Data(CommonCommands.FIELD_ONE), playerOne);
            sendAsk(new Data(CommonCommands.FIELD_TWO), playerTwo);
        }

        private void gameStart(final StatisticsCollector collector) throws IOException, ServerException,
                UnitException, BoardException {
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
            final Data data = new Data(CommonCommands.CONTINUE_GAME, gameLogic.getBoard());
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
        }

        @Override
        public void run() {
                while (findPlayers) {
                    try {
                        waitPlayers();
                        for (int i = 0; i < server.gamesCount; i++) {
                            final StatisticsCollector collector = new StatisticsCollector(id);

                            gameStart(collector);

                            // весь игровой процесс
                            while (gameLogic.isGameBegun()) {
                                gameRun(collector);
                                //noinspection BusyWait
                                Thread.sleep(server.delay);
                            }

                            gameEnding(collector);
                        }
                        final Data data = new Data(CommonCommands.END_GAME, gameLogic.getBoard());
                        sendAsk(data, playerOne);
                        sendAsk(data, playerTwo);
                    } catch (final ServerException | UnitException
                            | BoardException | InterruptedException | IOException e) {
                        logger.error(ServerExceptionType.ERROR_GAME_RUNNING.getErrorType(), e);
                        gameLogic.getBoard().setStatus(GameStatus.NO_WINNERS);
                    }
                    finally {
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
        final ServerWithChangeFields server = new ServerWithChangeFields(sc);
        server.startServer();
    }
}
