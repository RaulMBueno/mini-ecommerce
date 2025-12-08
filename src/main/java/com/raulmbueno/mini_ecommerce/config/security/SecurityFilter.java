package com.raulmbueno.mini_ecommerce.config.security;

import com.raulmbueno.mini_ecommerce.repositories.UserRepository;
import com.raulmbueno.mini_ecommerce.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "").trim();

            try {
                String login = tokenService.validateToken(token); // retorna o e-mail ou null

                if (login != null && !login.isBlank()
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

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
                }
            } catch (Exception e) {
                logger.warn("Filtro de segurança: erro ao validar token", e);
                // NÃO seta status aqui. Deixa o fluxo continuar sem usuário autenticado.
            }

        } else {
            // Só loga se quiser, mas não bloqueia nada
            logger.debug("Filtro de segurança: requisição sem token para {}", request.getRequestURI());
        }

        // MUITO IMPORTANTE: nunca devolve 403 aqui. Sempre deixa a requisição seguir.
        filterChain.doFilter(request, response);
    }
}
