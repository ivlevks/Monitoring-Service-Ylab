package org.ivlevks.configuration;

import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class YAMLHandler {
    private final Yaml yaml;
    private static Map<String, Object> properties = new HashMap<>();

    public YAMLHandler() {
        yaml = new Yaml();
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("application.yml");
        properties = yaml.load(inputStream);
    }

    public static Map<String, Object> getProperties() {
        return properties;
    }
}
