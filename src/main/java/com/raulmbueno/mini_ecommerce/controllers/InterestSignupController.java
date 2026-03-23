package com.raulmbueno.mini_ecommerce.controllers;

import com.raulmbueno.mini_ecommerce.dtos.InterestSignupDTO;
import com.raulmbueno.mini_ecommerce.dtos.InterestSignupInsertDTO;
import com.raulmbueno.mini_ecommerce.services.InterestSignupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Endpoint público para pré-cadastro de interesse.
 * Não requer autenticação.
 * Atende /interest-signups e /public/interest-signups.
 */
@RestController
@RequestMapping(value = {"/interest-signups", "/public/interest-signups"})
public class InterestSignupController {

    private final InterestSignupService service;

    public InterestSignupController(InterestSignupService service) {
        this.service = service;
    }

    /**
     * Diagnóstico: GET retorna 200 se o path estiver acessível.
     * Use para testar se requisições chegam ao controller.
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("OK");
    }

    /**
     * Listagem administrativa — mais recentes primeiro. Requer ROLE_ADMIN.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<InterestSignupDTO>> findAll() {
        return ResponseEntity.ok(service.findAllOrderedByNewest());
    }

    @PostMapping
    public ResponseEntity<InterestSignupDTO> insert(@Valid @RequestBody InterestSignupInsertDTO dto) {
        InterestSignupDTO created = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }
}
