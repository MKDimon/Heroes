package heroes;

import heroes.auxiliaryclasses.GameLogicException;
import heroes.clientserver.Client;
import heroes.clientserver.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SelfPlay10k {
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

    public static void main(final String[] args) throws GameLogicException, IOException, InterruptedException {
        new PlayServer().start();
        Thread.sleep(300); // Чтобы сервер успел запуститься
        for(int i = 0; i < 10000; i++) {
            new PlayClient().start();
            new PlayClient().start();
        }
    }
}
