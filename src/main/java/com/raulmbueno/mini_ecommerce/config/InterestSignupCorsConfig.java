package com.raulmbueno.mini_ecommerce.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS dedicado para lista de interesse - evita 403 em requisições cross-origin.
 * Ordem máxima para rodar ANTES do Spring Security e responder OPTIONS corretamente.
 */
@Configuration
public class InterestSignupCorsConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> interestSignupCorsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("OPTIONS");
        config.addAllowedMethod("HEAD");
        config.addAllowedHeader("*");
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/interest-signups", config);
        source.registerCorsConfiguration("/interest-signups/**", config);
        source.registerCorsConfiguration("/public/interest-signups", config);
        source.registerCorsConfiguration("/public/interest-signups/**", config);

        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>(new CorsFilter(source));
        registration.addUrlPatterns("/interest-signups", "/interest-signups/*", "/interest-signups/**",
                "/public/interest-signups", "/public/interest-signups/*", "/public/interest-signups/**");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
