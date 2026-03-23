package com.raulmbueno.mini_ecommerce.security.oauth;

import com.raulmbueno.mini_ecommerce.entities.User;
import com.raulmbueno.mini_ecommerce.services.TokenService;
import com.raulmbueno.mini_ecommerce.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Após login Google (OAuth2):
 * - E-mail == ADMIN_EMAIL: JWT admin (ROLE_ADMIN), redirect ?token= → front salva e vai para /admin.
 * - Demais e-mails: usuário comum (ROLE_CLIENT), JWT em ?customerToken= → front salva e vai para a loja.
 */
@Component
@ConditionalOnExpression("T(org.springframework.util.StringUtils).hasText('${spring.security.oauth2.client.registration.google.client-id:}')")
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;
    private final TokenService tokenService;

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${FRONTEND_URL:http://localhost:5173}")
    private String frontendUrl;

    public OAuth2SuccessHandler(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");
        if (email == null || email.isBlank()) {
            redirectUnauthorized(response);
            return;
        }

        String name = oauth2User.getAttribute("name");
        String adminEmailTrimmed = adminEmail != null ? adminEmail.trim() : "";
        boolean isAdmin = !adminEmailTrimmed.isEmpty() && email.equalsIgnoreCase(adminEmailTrimmed);

        User user = userService.findOrCreateForOAuth2(email, name, isAdmin);
        String jwt = tokenService.generateToken(user);

        String baseUrl = getBaseUrl();
        String param = isAdmin ? "token" : "customerToken";
        String redirectUrl = baseUrl + "/oauth2/redirect?" + param + "=" + URLEncoder.encode(jwt, StandardCharsets.UTF_8);
        response.sendRedirect(redirectUrl);
    }

    private void redirectUnauthorized(HttpServletResponse response) throws IOException {
        response.sendRedirect(getBaseUrl() + "/oauth2/redirect?error=unauthorized");
    }

    private String getBaseUrl() {
        return (frontendUrl == null || frontendUrl.isBlank()) ? "http://localhost:5173" : frontendUrl.trim().replaceAll("/$", "");
    }
}
