package heroes.clientserver;

import heroes.auxiliaryclasses.boardexception.BoardException;
import heroes.auxiliaryclasses.unitexception.UnitException;
import heroes.gamelogic.Army;
import heroes.gamelogic.Fields;
import heroes.gamelogic.GameLogic;
import heroes.player.Answer;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Консольный многопользовательский чат.
 * Сервер
 */
public class Server {

    static final int PORT = 8081;

    private final ConcurrentLinkedQueue<ServerSomething> serverList = new ConcurrentLinkedQueue<>();

    private class ServerSomething extends Thread {

        private final Server server;
        private final Socket socketOne;
        private final Socket socketTwo;

        private final BufferedReader inPlayerOne; // поток чтения из сокета
        private final BufferedWriter outPlayerOne; // поток завписи в сокет
        private final BufferedReader inPlayerTwo; // поток чтения из сокета
        private final BufferedWriter outPlayerTwo; // поток завписи в сокет

        private GameLogic gameLogic;
        private Map<Fields, BufferedWriter> getOuter;
        private Map<Fields, BufferedReader> getReader;

        /**
         * Для общения с клиентом необходим сокет (адресные данные)
         *
         * @param server сервер
         * @param socketOne сокет
         * @param socketTwo сокет
         */
        private ServerSomething(final Server server, final Socket socketOne, final Socket socketTwo) throws IOException {
            this.server = server;
            this.socketOne = socketOne;
            this.socketTwo = socketTwo;

            // если потоку ввода/вывода приведут к генерированию искдючения, оно проброситься дальше
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
            try {
                sendAsk(CommonCommands.FIELD_ONE.command, outPlayerOne);
                sendAsk(CommonCommands.FIELD_TWO.command, outPlayerTwo);

                sendAsk(CommonCommands.GET_ARMY.command, outPlayerOne);
                Army one = Deserializer.deserializeArmy(inPlayerOne.readLine());
                //sendDraw();

                sendAsk(CommonCommands.GET_ARMY.command, outPlayerTwo);
                Army two = Deserializer.deserializeArmy(inPlayerTwo.readLine());
                //sendDraw();

                gameLogic.gameStart(one, two);

                // весь игровой процесс
                while (gameLogic.isGameBegun()) {
                    getOuter.get(gameLogic.getBoard().getCurrentPlayer()).write(
                            Serializer.serializeBoard(gameLogic.getBoard())
                    );

                    Answer answer = Deserializer.deserializeAnswer(
                            getReader.get(gameLogic.getBoard().getCurrentPlayer()).readLine()
                    );

                    gameLogic.action(answer.getAttacker(), answer.getDefender(), answer.getActionType());

                    // sendDraw();
                    // обработка draw
                }

                // где то здесь сообщение о победе / ничьей
                // видимо смотреть в логи ;D

                this.downService();
            } catch (final IOException e) {
                this.downService();
            }//*/
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
        private void downService() {
            try {
                if (!socketOne.isClosed() || !socketTwo.isClosed()) {
                    socketOne.close();
                    inPlayerOne.close();
                    outPlayerOne.close();
                    socketTwo.close();
                    inPlayerTwo.close();
                    outPlayerTwo.close();
                    server.serverList.remove(this);
                }
            } catch (final IOException ignored) {
            }
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void startServer() throws IOException {
        System.out.println(String.format("Server started, port: %d", PORT));
        try (final ServerSocket serverSocket = new ServerSocket(PORT)) {
            // serverSocket.setSoTimeout(1000);
            while (true) { // приложение с помощью System.exit() закрывается по команде от клиента
                // Блокируется до возникновения нового соединения
                // Ждет первого игрока
                final Socket socketOne = serverSocket.accept();
                try {
                    while (true) { // Ждет второго игрока
                        final Socket socketTwo = serverSocket.accept();
                        try {
                            new ServerSomething(this, socketOne, socketTwo).start();
                        } catch (final IOException e) {
                            socketTwo.close();
                        }
                    }
                } catch (final IOException e) {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его:
                    socketOne.close();
                }
            }
        } catch (final BindException e) {
            e.printStackTrace();
        }
    }

    public static void main(final String[] args) throws IOException {
        final Server server = new Server();
        server.startServer();
    }
}
