package com.raulmbueno.mini_ecommerce.controllers;

import com.raulmbueno.mini_ecommerce.entities.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Endpoint simples para validar autenticação JWT: retorna email e roles do usuário logado.
 */
@RestController
@RequestMapping("/me")
public class MeController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }
        Object principal = authentication.getPrincipal();
        Map<String, Object> body = new HashMap<>();
        if (principal instanceof User user) {
            body.put("email", user.getEmail());
            body.put("roles", user.getRoles().stream()
                    .map(r -> r.getAuthority())
                    .collect(Collectors.toList()));
        } else {
            body.put("email", authentication.getName());
            body.put("roles", authentication.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.toList()));
        }
        return ResponseEntity.ok(body);
    }
}
