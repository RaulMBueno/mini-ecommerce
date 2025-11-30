package com.raulmbueno.mini_ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.raulmbueno.mini_ecommerce.entities.User;


import com.raulmbueno.mini_ecommerce.dtos.LoginRequestDTO;
import com.raulmbueno.mini_ecommerce.dtos.LoginResponseDTO;
import com.raulmbueno.mini_ecommerce.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        
    }
    
@PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginRequestDTO data) {
        System.out.println("--- TENTATIVA DE LOGIN ---");
        System.out.println("Email recebido: " + data.getEmail());
        
        try {
            // 1. Cria o token com os dados recebidos (ainda não autenticado)
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword());
            
            // 2. Tenta bater no banco e verificar a senha
            var auth = this.authenticationManager.authenticate(usernamePassword);
            
            System.out.println("Login com sucesso! Gerando token...");

            // 3. Gera o Token (Pulseira)
            var token = tokenService.generateToken((User) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
            
        } catch (Exception e) {
            // --- AQUI ESTÁ O DIAGNÓSTICO ---
            System.out.println("ERRO GRAVE NO LOGIN:");
            e.printStackTrace(); // Imprime o erro detalhado no terminal
            return ResponseEntity.internalServerError().body("Erro no servidor: " + e.getMessage());
        }
    }


}