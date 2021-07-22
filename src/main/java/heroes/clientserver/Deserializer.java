package heroes.clientserver;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;

public class Deserializer {
    public static Data deserializeData(final String json) throws IOException {
        return new ObjectMapper().readValue(json, Data.class);
    }

    /**
     * Парсит config.json из каталога и возвращает конфиги
     *
     * @return все нужные конфиги
     * @throws IOException json
     */
    public static ServersConfigs getConfig() throws IOException {
        final FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.json");

        final ServersConfigs sc = new ObjectMapper().readValue(fileInputStream, ServersConfigs.class);
        fileInputStream.close();
        return sc;
    }
}