package ru.sfedu.shop;

import java.io.*;
import java.util.Properties;

public class ConfigurationUtil {

    private String defaultConfigPath;

    private Properties configuration = new Properties();


    public ConfigurationUtil(String configPath) throws IllegalArgumentException {
        if (configPath == null) {
            throw new IllegalArgumentException("configPath cannot be NULL");
        }
        this.defaultConfigPath = configPath;
    }


    private synchronized Properties getConfiguration() throws IOException {
        if (configuration.isEmpty()) {
            loadConfiguration();
        }
        return configuration;
    }


    /**
     * Loads configuration from <code>defaultConfigPath</code>
     */
    private void loadConfiguration() {
        try (InputStream in = ConfigurationUtil.class.getResourceAsStream(defaultConfigPath)) {
            configuration.load(new InputStreamReader(in, "UTF-8"));
        } catch (Exception ex) {
            try {
                configuration.load(new FileReader(new File(defaultConfigPath)));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }


    /**
     * Gets configuration entry value
     *
     * @param key Entry key
     * @return Entry value by key
     */
    public String readConfig(String key) {
        try {
            return getConfiguration().getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
