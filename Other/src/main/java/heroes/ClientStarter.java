package heroes;

import heroes.clientserver.Client;

public class ClientStarter {

    private static class PlayClient extends Thread {
        @Override
        public void run() {
            Client.main(new String[]{});
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new PlayClient().start();
        new PlayClient().start();
    }
}
