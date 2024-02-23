package org.ivlevks.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
public class MyApplicationContextConfiguration {
    @Value("${POSTGRES_URL}")
    String URL;
    @Value("${POSTGRES_USER}")
    String userName;
    @Value("${POSTGRES_PASSWORD}")
    String password;

    @Bean
    public ConnectionManager connectionManager() {
        ConnectionManager manager = new ConnectionManager();

        manager.setURL(URL);
        manager.setUserName(userName);
        manager.setPASSWORD(password);

        return manager;
    }
}
