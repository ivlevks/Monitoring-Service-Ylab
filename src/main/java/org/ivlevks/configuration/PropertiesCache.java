package org.ivlevks.configuration;

import org.ivlevks.configuration.annotations.Loggable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

@Loggable
@Component
public class PropertiesCache {
    private final Properties configProp = new Properties();
    private static final PropertiesCache INSTANCE = new PropertiesCache();

    private PropertiesCache()
    {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        try {
            configProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PropertiesCache getInstance()
    {
        return INSTANCE;
    }

    public String getProperty(String key){
        return configProp.getProperty(key);
    }

    public Set<String> getAllPropertyNames(){
        return configProp.stringPropertyNames();
    }

    public boolean containsKey(String key){
        return configProp.containsKey(key);
    }
}