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
    public final boolean WITH_BOT;
    @JsonProperty
    public final String TYPE_OPPONENT;
    @JsonProperty
    public final int ROOM;
    @JsonProperty
    public final int FIELD;

    @JsonCreator
    public ClientsConfigs(@JsonProperty("PORT") final int port,
                          @JsonProperty("HOST") final String host,
                          @JsonProperty("GUI") final String gui,
                          @JsonProperty("TYPE_BOT") final String type_bot,
                          @JsonProperty("WITH_BOT") final boolean with_bot,
                          @JsonProperty("TYPE_OPPONENT") final String type_opponent,
                          @JsonProperty("ROOM") final int room,
                          @JsonProperty("FIELD") final int field) {
        HOST = host;
        TYPE_BOT = type_bot;
        PORT = port;
        GUI = gui;
        WITH_BOT = with_bot;
        TYPE_OPPONENT = type_opponent;
        ROOM = room;
        FIELD = field;
    }
}