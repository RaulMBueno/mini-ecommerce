package com.raulmbueno.mini_ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class ResourceServerConfig {

    private static final String[] PUBLIC_ROUTES_GET = { "/products/**", "/categories/**" };
    private static final String[] ADMIN_ROUTES_POST = { "/products", "/categories" };
    private static final String[] ADMIN_ROUTES_GENERAL = { "/products/**", "/categories/**" };


    @Bean
    @Profile("dev")
    public SecurityFilterChain devSecurityFilterChain(HttpSecurity http) throws Exception {
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
            .requestMatchers("/h2-console/**").permitAll() // <-- Linha corrigida!
            .requestMatchers(HttpMethod.GET, PUBLIC_ROUTES_GET).permitAll()
            .requestMatchers(HttpMethod.POST, ADMIN_ROUTES_POST).hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, ADMIN_ROUTES_GENERAL).hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, ADMIN_ROUTES_GENERAL).hasRole("ADMIN")
            .anyRequest().authenticated()
        );

        return http.build();
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain prodSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.GET, PUBLIC_ROUTES_GET).permitAll()
            .requestMatchers(HttpMethod.POST, ADMIN_ROUTES_POST).hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, ADMIN_ROUTES_GENERAL).hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, ADMIN_ROUTES_GENERAL).hasRole("ADMIN")
            .anyRequest().authenticated()
        );

        return http.build();
    }
}