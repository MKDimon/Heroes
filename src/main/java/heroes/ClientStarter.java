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
        Thread.sleep(2000);
        System.out.println("1\n1\n");
        Thread.sleep(2000);
        System.out.println("Random\nRandom\n");
    }
}
