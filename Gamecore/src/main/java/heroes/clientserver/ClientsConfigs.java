package heroes.clientserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientsConfigs {
    @JsonProperty
    public final int PORT;
    @JsonProperty
    public final String HOST;
    @JsonProperty
    public final String GUI;
    @JsonProperty
    public final String TYPE_BOT;
    @JsonProperty
    public final String SIMULATION_TREE;
    @JsonProperty
    public final String UTILITY_FUNC;
    @JsonProperty
    public final String NEURON;
    @JsonProperty
    public final String ARMY;
    @JsonProperty
    public final int HEIGHT;
    @JsonProperty
    public final boolean CLUSTERING;
    @JsonProperty
    public final int ROOM;
    @JsonProperty
    public final int FIELD;

    @JsonCreator
    public ClientsConfigs(@JsonProperty("PORT") final int port,
                          @JsonProperty("HOST") final String host,
                          @JsonProperty("GUI") final String gui,
                          @JsonProperty("UTILITY_FUNC") final String func,
                          @JsonProperty("HEIGHT") final int height,
                          @JsonProperty("CLUSTERING") final boolean clustering,
                          @JsonProperty("TYPE_BOT") final String type_bot,
                          @JsonProperty("WITH_BOT") final boolean with_bot,
                          @JsonProperty("TYPE_OPPONENT") final String type_opponent,
                          @JsonProperty("ROOM") final int room,
                          @JsonProperty("FIELD") final int field,
                          @JsonProperty("NEURON") final String neuron,
                          @JsonProperty("ARMY") final String army,
                          @JsonProperty("SIMULATION_TREE") final String tree) {
        HOST = host;
        TYPE_BOT = type_bot;
        PORT = port;
        UTILITY_FUNC = func;
        HEIGHT = height;
        CLUSTERING = clustering;
        GUI = gui;
        WITH_BOT = with_bot;
        TYPE_OPPONENT = type_opponent;
        ROOM = room;
        FIELD = field;
        ARMY = army;
        NEURON = neuron;
        SIMULATION_TREE = tree;
    }
}