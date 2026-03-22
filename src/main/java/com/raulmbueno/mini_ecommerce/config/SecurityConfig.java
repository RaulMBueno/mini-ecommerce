package com.raulmbueno.mini_ecommerce.config;

import com.raulmbueno.mini_ecommerce.config.security.SecurityFilter;
import com.raulmbueno.mini_ecommerce.security.oauth.OAuth2FailureHandler;
import com.raulmbueno.mini_ecommerce.security.oauth.OAuth2SuccessHandler;
import com.raulmbueno.mini_ecommerce.services.AuthorizationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    private final AuthorizationService authorizationService;
    private final SecurityFilter securityFilter;

    @Value("${FRONTEND_URL:}")
    private String frontendUrl;

    public SecurityConfig(AuthorizationService authorizationService, SecurityFilter securityFilter) {
        this.authorizationService = authorizationService;
        this.securityFilter = securityFilter;
    }

    /**
     * Chain dedicada para lista de interesse - público, sem autenticação.
     * Atende /interest-signups e /public/interest-signups (evita 401 quando frontend usa path sem /public).
     */
    @Bean
    @Order(0)
    public SecurityFilterChain interestSignupChain(HttpSecurity http) throws Exception {
        return http
            .securityMatcher("/interest-signups", "/interest-signups/**", "/interest-signups/*",
                    "/public/interest-signups", "/public/interest-signups/**", "/public/interest-signups/*")
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .exceptionHandling(ex -> ex
                .accessDeniedHandler((req, res, accessDeniedEx) -> {
                    log.warn(">>> 403 INTEREST CHAIN: {} {}", req.getMethod(), req.getRequestURI());
                    res.setStatus(403);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\":\"AccessDenied\",\"chain\":\"interestSignup\",\"path\":\"" + req.getRequestURI() + "\",\"method\":\"" + req.getMethod() + "\"}");
                }))
            .build();
    }

    /**
     * Chain apenas para OAuth2 (Google). Ativa só quando
     * spring.security.oauth2.client.registration.google.client-id estiver configurado e não vazio (ex.: perfil prod).
     * Evita "No qualifying bean of type 'ClientRegistrationRepository'" em dev/Railway sem OAuth2.
     */
    @Bean
    @Order(1)
    @ConditionalOnExpression("T(org.springframework.util.StringUtils).hasText('${spring.security.oauth2.client.registration.google.client-id:}')")
    public SecurityFilterChain oauth2FilterChain(HttpSecurity http, OAuth2SuccessHandler oAuth2SuccessHandler,
                                                  OAuth2FailureHandler oAuth2FailureHandler) throws Exception {
        http
            .securityMatcher("/oauth2/**", "/login/oauth2/**")
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .oauth2Login(o -> o.successHandler(oAuth2SuccessHandler).failureHandler(oAuth2FailureHandler))
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    /**
     * Chain principal: JWT, permissões públicas (GET), ADMIN, etc. Sem oauth2Login para não depender de ClientRegistrationRepository.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/**")
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/public/**", "/interest-signups", "/interest-signups/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/products/**", "/categories/**", "/brands/**").permitAll()
                .requestMatchers("/actuator/health", "/actuator/health/**").permitAll()
                .requestMatchers(HttpMethod.POST,   "/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,   "/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/categories/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,   "/brands/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/brands/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/brands/**").hasRole("ADMIN")
                .requestMatchers("/users/**").hasRole("ADMIN")
                .requestMatchers("/orders/**").authenticated()
                .requestMatchers("/clients/**").authenticated()
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedHandler((req, res, accessDeniedEx) -> {
                    log.warn(">>> 403 MAIN CHAIN: {} {}", req.getMethod(), req.getRequestURI());
                    res.setStatus(403);
                    res.setContentType("application/json");
                    res.getWriter().write("{\"error\":\"AccessDenied\",\"chain\":\"main\",\"path\":\"" + req.getRequestURI() + "\",\"method\":\"" + req.getMethod() + "\"}");
                }))
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    // CORS: * em dev; inclui FRONTEND_URL quando definido (ex.: https://remakeup.com.br)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        List<String> origins = new ArrayList<>(Arrays.asList("*"));
        if (frontendUrl != null && !frontendUrl.isBlank()) {
            String origin = frontendUrl.trim().replaceAll("/$", "");
            origins.add(0, origin);
        }
        configuration.setAllowedOriginPatterns(origins);
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
