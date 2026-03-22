package com.raulmbueno.mini_ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS dedicado para /interest-signups.
 * Necessário porque WebSecurityCustomizer.ignoring() bypassa a chain de segurança,
 * incluindo o CorsFilter do Spring Security.
 */
@Configuration
public class InterestSignupCorsConfig {

    @Bean
    public CorsFilter interestSignupCorsFilter() {
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

        return new CorsFilter(source);
    }
}
