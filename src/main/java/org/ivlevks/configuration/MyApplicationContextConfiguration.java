package org.ivlevks.configuration;

import org.ivlevks.configuration.migration.MigrationHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
public class MyApplicationContextConfiguration {
    @Value("${POSTGRES_URL}")
    private String URL;
    @Value("${POSTGRES_USER}")
    private String userName;
    @Value("${POSTGRES_PASSWORD}")
    private String password;
    @Value("${default-schema-name}")
    private String DEFAULT_SCHEMA_NAME;
    @Value("${changeLogFile}")
    private String CHANGE_LOG_FILE;

    @Bean
    public ConnectionManager connectionManager() {
        ConnectionManager manager = new ConnectionManager();

        manager.setURL(URL);
        manager.setUserName(userName);
        manager.setPASSWORD(password);

        return manager;
    }

    @Bean
    public MigrationHelper migrationHelper() {
        MigrationHelper migrationHelper = new MigrationHelper(connectionManager());

        migrationHelper.setCHANGE_LOG_FILE(CHANGE_LOG_FILE);
        migrationHelper.setDEFAULT_SCHEMA_NAME(DEFAULT_SCHEMA_NAME);
        migrationHelper.migrate();

        return migrationHelper;
    }
}
