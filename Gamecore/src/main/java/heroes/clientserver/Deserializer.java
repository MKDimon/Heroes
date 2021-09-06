package heroes.clientserver;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Deserializer {
    public static Data deserializeData(final String json) throws IOException {
        return new ObjectMapper().readValue(json, Data.class);
    }
}