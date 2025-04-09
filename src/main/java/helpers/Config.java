package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration helper class for loading application settings from a properties file.
 *
 * @author Oliwia Daszczynska
 * @author Genadij Vorontsov
 */
public class Config {
    private final String Config_Path = "src/main/resources/config/access.properties";
    private final String Javalin_Config_Path = "src/main/resources/config/javalin.properties";
    private Properties properties;
    private static Config instance;

    /**
     * Private constructor that loads configuration properties from the specified file.
     * Ensures that only one instance of the configuration is created.
     *
     * @throws RuntimeException if the configuration file is missing or cannot be loaded.
     * @author Genadij Vorontsov
     * @author Oliwia Daszczynska
     */
    private Config() {
        properties = new Properties();
        File configFile = new File(Config_Path);
        File javalinConfigFile = new File(Javalin_Config_Path);

        if (!configFile.exists()) {
            throw new RuntimeException("Mongo access properties missing!" +
                    "Please add access properties to resources/config!");
        }
        if (!javalinConfigFile.exists()) {
            throw new RuntimeException("Javalin configurations missing.");
        }

        try {
            properties.load(new FileInputStream(configFile));
            properties.load(new FileInputStream(javalinConfigFile));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    /**
     * Retrieves the singleton instance of the Config class.
     *
     * @return the single instance of the Config class.
     * @author Genadij Vorontsov
     */
    public static Config get() {
        if (instance == null) {
            instance = new Config();
        }
        return instance;
    }

    /***
     * Use properties stored in resource folder to connect to uni server
     *
     * @return mongo connection string.
     * @author Oliwia Daszczynska
     * @author Genadij Vorontsov
     */
    public String MongoConnectionString() {
        String uri = "mongodb://" + properties.getProperty("remote_user") +
                ":" + properties.getProperty("remote_password") +
                "@" + properties.getProperty("remote_host") +
                ":" + properties.getProperty("remote_port") +
                "/?authSource=" + properties.getProperty("remote_database");
        return uri;
    }

    /**
     * Get the database name from the properties file
     *
     * @return the database name
     * @author Genadij Vorontsov
     */
    public String getDatabaseName() {
        return properties.getProperty("remote_database");
    }

    /**
     * Get the collection name from the properties file
     *
     * @return the collection name
     * @author Genadij Vorontsov
     */
    public String getCollectionName() {
        return properties.getProperty("remote_collection");
    }

    /**
     * @author Ishak Bouaziz
     */
    public int getPort() {
        return Integer.parseInt(properties.getProperty("remote_port"));
    }

    /**
     * gets static files path from configuration.
     * @author Oliwia Daszczynska
     */
    public String getStaticFilesPath() {
        return properties.getProperty("static_files_path");
    }
}
