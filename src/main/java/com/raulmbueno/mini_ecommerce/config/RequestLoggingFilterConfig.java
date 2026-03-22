package com.raulmbueno.mini_ecommerce.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Loga requisições para /public/interest-signups (diagnóstico).
 */
@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> requestLoggingFilterRegistration() {
        FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new OncePerRequestFilter() {
            private final Logger log = LoggerFactory.getLogger("RequestLoggingFilter");

            @Override
            protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                           @NonNull FilterChain filterChain) throws ServletException, IOException {
                String path = request.getRequestURI();
                if (path != null && path.contains("interest-signups")) {
                    log.info(">>> REQUEST: {} {} | URI={}", request.getMethod(), path, request.getRequestURI());
                }
                filterChain.doFilter(request, response);
            }
        });
        registration.addUrlPatterns("/interest-signups", "/interest-signups/*", "/interest-signups/**",
                "/public/interest-signups", "/public/interest-signups/*", "/public/interest-signups/**");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }
}
