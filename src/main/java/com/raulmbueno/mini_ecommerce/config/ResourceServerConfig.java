package com.raulmbueno.mini_ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                // Permite acesso irrestrito para POST em /users (rota de registro)
                .requestMatchers("/users").permitAll()
                // Por enquanto, bloqueia todas as outras rotas.
                .anyRequest().authenticated()); 

        // 3. Constrói e retorna a cadeia de filtros de segurança
        return http.build();
    }
}