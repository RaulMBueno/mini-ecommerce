package com.raulmbueno.mini_ecommerce.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.raulmbueno.mini_ecommerce.config.security.SecurityProperties;
import com.raulmbueno.mini_ecommerce.entities.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private final SecurityProperties securityProperties;

    public TokenService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public String generateToken(User user) {
        try {
            // Usa a chave secreta das propriedades
            Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecret());
            
            return JWT.create()
                    .withIssuer("mini-ecommerce")
                    .withSubject(user.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(securityProperties.getSecret());
            return JWT.require(algorithm)
                    .withIssuer("mini-ecommerce")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now()
                .plusHours(securityProperties.getExpirationHours())
                .toInstant(ZoneOffset.of("-03:00"));
    }
}