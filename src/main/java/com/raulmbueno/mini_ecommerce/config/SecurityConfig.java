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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true)
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
            // Sem CSRF (API stateless; OAuth2 usa state em cookie)
            .csrf(csrf -> csrf.disable())
            // Session IF_REQUIRED: OAuth2 precisa de session para guardar state; JWT continua para API
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            // OAuth2 Login (Google): handshake em /oauth2/** e callback em /login/oauth2/code/google
            .oauth2Login(Customizer.withDefaults())
            // AUTORIZAÇÃO
            .authorizeHttpRequests(auth -> auth
                // Pré-flight (CORS)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Login (email/senha)
                .requestMatchers("/auth/**").permitAll()

                // OAuth2: autorização e callback
                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                // Vitrine pública (GET liberado)
                .requestMatchers(HttpMethod.GET,
                        "/products/**",
                        "/categories/**",
                        "/brands/**"
                ).permitAll()

                // Health check (load balancers, Railway)
                .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()

                // Escrita: exige ROLE_ADMIN (method security reforça com @PreAuthorize)
                .requestMatchers(HttpMethod.POST,   "/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,   "/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,   "/brands/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/brands/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/brands/**").hasRole("ADMIN")

                // Usuários: apenas ADMIN pode criar/editar/excluir/listar
                .requestMatchers("/users/**").hasRole("ADMIN")

                // Orders e clients: exige autenticação (qualquer usuário logado)
                .requestMatchers("/orders/**").authenticated()
                .requestMatchers("/clients/**").authenticated()

                // Demais rotas: exigem autenticação (nenhuma rota sensível aberta)
                .anyRequest().authenticated()
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
    @SuppressWarnings("deprecation") // DaoAuthenticationProvider API ainda suportada em Spring Security 6
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
