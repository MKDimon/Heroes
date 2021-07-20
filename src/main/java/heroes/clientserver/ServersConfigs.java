package heroes.clientserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServersConfigs {
    @JsonProperty
    public final int PORT;
    @JsonProperty
    public final int MAX_ROOMS;
    @JsonCreator
    public ServersConfigs(@JsonProperty("PORT") final int PORT,
                          @JsonProperty("MAX_ROOMS") final int MAX_ROOMS) {
        this.MAX_ROOMS = MAX_ROOMS;
        this.PORT = PORT;
    }
}