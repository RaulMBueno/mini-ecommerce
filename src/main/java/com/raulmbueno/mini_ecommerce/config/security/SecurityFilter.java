package com.raulmbueno.mini_ecommerce.config.security;

import com.raulmbueno.mini_ecommerce.repositories.UserRepository;
import com.raulmbueno.mini_ecommerce.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String token = recoverToken(request);

        if (token != null) {
            logger.debug("Filtro de segurança: token encontrado no cabeçalho Authorization");

            String login = tokenService.validateToken(token);

            if (login != null && !login.isEmpty()) {
                logger.debug("Filtro de segurança: token válido para o usuário {}", login);

                UserDetails user = userRepository.findByEmail(login).orElse(null);

                if (user != null) {
                    logger.info("Filtro de segurança: usuário {} autenticado com sucesso", login);

                    var authentication = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warn("Filtro de segurança: usuário {} não encontrado no banco de dados", login);
                }

            } else {
                logger.warn("Filtro de segurança: token inválido ou expirado");
            }

        } else {
            String path = request.getRequestURI();
            // Só loga ausência de token em rotas que deveriam ser protegidas
            if (!path.startsWith("/auth") && !path.startsWith("/products")) {
                logger.debug("Filtro de segurança: nenhuma credencial enviada para a rota protegida {}", path);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank()) {
            return null;
        }
        return authHeader.replace("Bearer ", "").trim();
    }
}
