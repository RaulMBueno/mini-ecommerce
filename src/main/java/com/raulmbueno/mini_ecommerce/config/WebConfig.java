package com.raulmbueno.mini_ecommerce.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração para expor arquivos estáticos de upload (logos, etc.).
 * Tudo que estiver na pasta física "uploads" será acessível via URL /uploads/**.
 *
 * Exemplo:
 *  - arquivo salvo em: uploads/logos/brand-12-123456.png
 *  - URL: http://localhost:8080/uploads/logos/brand-12-123456.png
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // "file:uploads/"  -> pasta física "uploads" na raiz do projeto / container
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}
