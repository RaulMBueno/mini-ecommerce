package com.raulmbueno.mini_ecommerce.config;

import com.raulmbueno.mini_ecommerce.config.security.SecurityFilter;
import com.raulmbueno.mini_ecommerce.services.AuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthorizationService authorizationService;
    private final SecurityFilter securityFilter;

    public SecurityConfig(AuthorizationService authorizationService, SecurityFilter securityFilter) {
        this.authorizationService = authorizationService;
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Libera uso em iframe (H2, etc, se usar)
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            // CORS
            .cors(Customizer.withDefaults())
            // Sem CSRF (API stateless)
            .csrf(csrf -> csrf.disable())
            // JWT = stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // AUTORIZAÇÃO
            .authorizeHttpRequests(auth -> auth
                // Pré-flight (CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Login / refresh / etc
                .requestMatchers("/auth/**").permitAll()

                // Vitrine pública (GET liberado)
                .requestMatchers(HttpMethod.GET,
                        "/products/**",
                        "/categories/**",
                        "/brands/**"
                ).permitAll()

                // ---- AQUI ESTÁ A MUDANÇA: brands protegidas para escrita ----
                .requestMatchers(HttpMethod.POST, "/brands/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/brands/**").authenticated()
                // -------------------------------------------------------------

                // Endpoints protegidos (cadastros, deletes etc. de product/category)
                .requestMatchers(HttpMethod.POST, "/products/**").authenticated()
                .requestMatchers(HttpMethod.PUT,  "/products/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/products/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/categories/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/categories/**").authenticated()

                // Todo resto por enquanto fica liberado (se quiser depois trocamos pra authenticated)
                .anyRequest().permitAll()
            )
            // Filtro JWT antes do UsernamePasswordAuthenticationFilter
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // CORS liberado pra localhost:5173 (e outros)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Em dev, pode deixar tudo liberado
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(authorizationService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
