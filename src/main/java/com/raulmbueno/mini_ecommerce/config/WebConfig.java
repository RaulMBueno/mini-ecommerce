package com.raulmbueno.mini_ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Libera para todas as rotas (/products, /users, etc)
                .allowedOrigins("*") // Libera para qualquer site (React, Angular, Mobile)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS"); // Libera esses métodos
    }
}