package com.raulmbueno.mini_ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ResourceServerConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // 1. Desabilita a proteção CSRF (necessário para APIs sem estado, como a sua)
    http.csrf(AbstractHttpConfigurer::disable);

    // 2. Define as regras de autorização para as rotas (endpoints)
    http.authorizeHttpRequests(authorize -> authorize
            // Rotas que exigem ADMIN (POST, PUT, DELETE em /products e /categories)
            // A regra .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN") exige que o usuário tenha o perfil "ROLE_ADMIN".
            .requestMatchers(HttpMethod.POST, "/products").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/products/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

            .requestMatchers(HttpMethod.POST, "/categories").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/categories/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
            
            // Rotas públicas (PermitAll): GET em /products, /categories e POST em /users (registro)
            .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/users").permitAll()

            // Qualquer outra requisição deve ser autenticada (ex: um GET em /users, que só admin deve poder ver)
            .anyRequest().authenticated());

    // 3. Constrói e retorna a cadeia de filtros de segurança
    return http.build();
    }

}
