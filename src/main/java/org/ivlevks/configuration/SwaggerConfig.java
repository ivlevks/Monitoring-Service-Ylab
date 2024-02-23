package org.ivlevks.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import java.util.Collections;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .produces(Collections.singleton("application/json"))
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.ivlevks.adapter.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Monitiring Service")
                .description("Storage your indications.")
                .version("1.0.4")
                .contact(new Contact(
                        "Ivlev Kostik",
                        "https://github.com/ivlevks/",
                        "ivlevks@yandex.ru"))
                .build();
    }
}
