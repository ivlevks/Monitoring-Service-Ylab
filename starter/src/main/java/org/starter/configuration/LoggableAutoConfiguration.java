package org.starter.configuration;

import org.springframework.context.annotation.Bean;
import org.starter.aspect.LoggableAspect;

public class LoggableAutoConfiguration {

    @Bean
    public LoggableAspect loggableAspect() {
        return new LoggableAspect();
    }
}
