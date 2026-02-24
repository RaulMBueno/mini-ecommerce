package com.raulmbueno.mini_ecommerce.security.oauth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Em caso de falha no login OAuth2 (Google), redireciona para o frontend em /login?error=oauth2.
 */
@Component
@ConditionalOnExpression("T(org.springframework.util.StringUtils).hasText('${spring.security.oauth2.client.registration.google.client-id:}')")
public class OAuth2FailureHandler implements AuthenticationFailureHandler {

    @Value("${FRONTEND_URL:http://localhost:5173}")
    private String frontendUrl;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        String baseUrl = (frontendUrl == null || frontendUrl.isBlank()) ? "http://localhost:5173" : frontendUrl.trim().replaceAll("/$", "");
        response.sendRedirect(baseUrl + "/login?error=oauth2");
    }
}
