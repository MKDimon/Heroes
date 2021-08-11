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
    @JsonCreator
    public ClientsConfigs(@JsonProperty("PORT") final int port,
                          @JsonProperty("HOST") final String host,
                          @JsonProperty("GUI") final String gui,
                          @JsonProperty("TYPE_BOT") final String type_bot) {
        HOST = host;
        TYPE_BOT = type_bot;
        PORT = port;
        GUI = gui;
    }
}