package heroes.clientserver.commands;

import heroes.clientserver.Client;
import heroes.clientserver.Data;

import java.io.BufferedWriter;

public abstract class Command {
    private final BufferedWriter out;
    private final Data data;

    public BufferedWriter getOut() {
        return out;
    }

    public Data getData() {
        return data;
    }

    public Client getClient() {
        return client;
    }

    private final Client client;

    public Command(final Data data, final BufferedWriter out, final Client client) {
        this.out = out;
        this.data = data;
        this.client = client;
    }

    public abstract void execute();
}
