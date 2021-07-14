package heroes.clientserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class ClientGUI {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private static final String IP = "127.0.0.1";
    private static final int PORT = Server.PORT;

    private final String ip;
    private final int port;

    private Socket socket = null;
    private BufferedReader in = null; // поток чтения из сокета
    private BufferedWriter out = null; // поток записи в сокет

    public static void main(String[] args) {
        ClientGUI gui = new ClientGUI(IP, PORT);
        gui.startClientGUI();
    }

    private ClientGUI(final String ip, final int port) {
        this.ip = ip;
        this.port = port;
    }

    private void startClientGUI() {
        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            logger.error("Error client starting", e);
        }
        startGUI();
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            logger.error("Error service downing", e);
        }
    }

    private void startGUI() {
        try {
            String message;
            while (true) {
                message = in.readLine();
            }
        } catch (IOException e) {
            logger.error("Error client running", e);
        }
    }
}
