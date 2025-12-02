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

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    public SecurityFilter(TokenService tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        
        if (token != null) {
            System.out.println("--- FILTRO: TOKEN ENCONTRADO ---");
            var login = tokenService.validateToken(token);

            if (login != null && !login.isEmpty()) {
                System.out.println("--- FILTRO: TOKEN VÁLIDO PARA: " + login);
                UserDetails user = userRepository.findByEmail(login).orElse(null);

                if (user != null) {
                    System.out.println("--- FILTRO: USUÁRIO ENCONTRADO NO BANCO. LIBERANDO... ---");
                    var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("--- FILTRO: USUÁRIO NÃO ENCONTRADO NO BANCO ---");
                }
            } else {
                System.out.println("--- FILTRO: TOKEN INVÁLIDO OU EXPIRADO ---");
            }
        } else {
            // Apenas imprime se não for rota pública (pra não poluir muito)
            if (!request.getRequestURI().contains("/auth") && !request.getRequestURI().contains("/products")) {
                 System.out.println("--- FILTRO: NENHUM TOKEN NO CABEÇALHO ---");
            }
        }
        
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}