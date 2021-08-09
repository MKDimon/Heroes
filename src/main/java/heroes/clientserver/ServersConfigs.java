package heroes.clientserver;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServersConfigs {
    @JsonProperty
    public final int PORT;
    @JsonProperty
    public final int MAX_ROOMS;
    @JsonProperty
    public final int DELAY;
    @JsonProperty
    public final int THREADS;
    @JsonProperty
    public final int GAMES_COUNT;
    @JsonProperty
    public final String PATH_LOG;
    @JsonProperty
    public final String LOGBACK;
    @JsonCreator
    public ServersConfigs(@JsonProperty("PORT") final int port,
                          @JsonProperty("MAX_ROOMS") final int max_rooms,
                          @JsonProperty("DELAY") final int delay,
                          @JsonProperty("THREADS") final int threads,
                          @JsonProperty("GAMES_COUNT") final int games_count,
                          @JsonProperty("PATH_LOG") final String path_log,
                          @JsonProperty("LOGBACK") final String logback) {
        MAX_ROOMS = max_rooms;
        PORT = port;
        DELAY = delay;
        THREADS = threads;
        GAMES_COUNT = games_count;
        PATH_LOG = path_log;
        LOGBACK = logback;
    }
}