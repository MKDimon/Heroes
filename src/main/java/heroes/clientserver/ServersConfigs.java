package heroes.clientserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServersConfigs {
    @JsonProperty
    public final int PORT;
    @JsonProperty
    public final int MAX_ROOMS;
    @JsonProperty
    public final int DELAY;
    @JsonCreator
    public ServersConfigs(@JsonProperty("PORT") final int PORT,
                          @JsonProperty("MAX_ROOMS") final int MAX_ROOMS,
                          @JsonProperty("DELAY") final int DELAY) {
        this.MAX_ROOMS = MAX_ROOMS;
        this.PORT = PORT;
        this.DELAY = DELAY;
    }
}