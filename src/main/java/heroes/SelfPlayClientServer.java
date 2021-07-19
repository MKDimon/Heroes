package heroes;

import heroes.clientserver.Client;
import heroes.clientserver.ClientGUI;
import heroes.clientserver.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SelfPlayClientServer {
    static Logger logger = LoggerFactory.getLogger(SelfPlayClientServer.class);

    private static class PlayServer extends Thread {
        @Override
        public void run() {
            try {
                Server.main(new String[]{});
            } catch (IOException e) {
                logger.error("Сервер упал(", e);
            }
        }
    }

    private static class PlayClient extends Thread {
        @Override
        public void run() {
            Client.main(new String[]{});
        }
    }

    public static void main(final String[] args) throws InterruptedException {
        new PlayServer().start();
        Thread.sleep(300); // Чтобы сервер успел запуститься
        new PlayClient().start();
        new PlayClient().start();
    }
}
