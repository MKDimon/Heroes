package heroes.clientserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Serializer {
    public static String serializeData(final Data data) throws IOException {
        final StringWriter writer = new StringWriter();
        new ObjectMapper().writeValue(writer, data);
        return writer.toString();
    }
}