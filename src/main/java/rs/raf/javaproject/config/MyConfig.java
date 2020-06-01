package rs.raf.javaproject.config;

import rs.raf.javaproject.model.INode;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MyConfig {
    private Properties properties = new Properties();
    private String propertiesFile = "application.properties";

    private static MyConfig instance = new MyConfig();

    private MyConfig(){
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFile);
        try {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String bootstrap(){
        return getInstance().properties.getProperty("bootstrap").trim();
    }

    public static long port(){
        return Long.parseLong(getInstance().properties.getProperty("server.port"));
    }

    public static String ip(){
        return getInstance().properties.getProperty("public.ip").trim();
    }

    public static String id(){
        return  ip() + INode.DELIMITER + port();
    }
    private static MyConfig getInstance(){
        return instance;
    }
}
