package heroes.clientserver;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;

public class Deserializer {
    public static Data deserializeData(final String json) throws IOException {
        return new ObjectMapper().readValue(json, Data.class);
    }

    /**
     * Парсит serverConfig.json из каталога и возвращает конфиги сервера
     *
     * @return все нужные конфиги
     * @throws IOException json
     */
    public static ServersConfigs getServersConfig() throws IOException {
        final FileInputStream fileInputStream = new FileInputStream("src/main/resources/serverConfig.json");

        final ServersConfigs sc = new ObjectMapper().readValue(fileInputStream, ServersConfigs.class);
        fileInputStream.close();
        return sc;
    }

    /**
     * Парсит serverConfig.json из каталога и возвращает конфиги клиента
     *
     * @return все нужные конфиги
     * @throws IOException json
     */
    public static ClientsConfigs getClientsConfig() throws IOException {
        final FileInputStream fileInputStream = new FileInputStream("src/main/resources/clientConfig.json");

        final ClientsConfigs cc = new ObjectMapper().readValue(fileInputStream, ClientsConfigs.class);
        fileInputStream.close();
        return cc;
    }
}