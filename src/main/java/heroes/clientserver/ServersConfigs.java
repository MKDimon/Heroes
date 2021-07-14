package heroes.clientserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServersConfigs {
    @JsonProperty
    public final int PORT;
    @JsonProperty
    public final int GUI_PORT;
    @JsonProperty
    public final int MAX_ROOMS;
    @JsonCreator
    public ServersConfigs(@JsonProperty("PORT") int PORT,
                          @JsonProperty("GUI_PORT") int GUI_PORT,
                          @JsonProperty("MAX_ROOMS") int MAX_ROOMS) {
        this.MAX_ROOMS = MAX_ROOMS;
        this.PORT = PORT;
        this.GUI_PORT = GUI_PORT;
    }
}