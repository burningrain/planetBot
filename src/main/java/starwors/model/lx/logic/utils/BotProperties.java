package starwors.model.lx.logic.utils;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BotProperties {

    private String server = "hardinv.ru";
    private int port = 10040;
    private String selfName = "";
    private String token = "";



    public void loadProperties(String filename) {
        FileInputStream file = null;
        try {
            // открыть файл bot.prop
            file = new FileInputStream(filename);
            Properties props = new Properties();
            // загрузить настройки из файла
            props.load(file);

            selfName = props.getProperty("name");
            token = props.getProperty("token");
            server = props.getProperty("server");
            port = Integer.parseInt(props.getProperty("port"));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException ex) {
                    // do nothing
                }
            }
            PlanetUtils.MY_PLANET_OWNER_NAME = selfName; // FIXME UGLY HUCK
        }
    }

    public String getToken() {
        return token;
    }

    public String getServer() {
        return server;
    }

    public int getPort() {
        return port;
    }

    public String getSelfName() {
        return selfName;
    }

}
