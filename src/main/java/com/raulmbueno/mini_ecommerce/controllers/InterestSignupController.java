package com.raulmbueno.mini_ecommerce.controllers;

import com.raulmbueno.mini_ecommerce.dtos.InterestSignupDTO;
import com.raulmbueno.mini_ecommerce.dtos.InterestSignupInsertDTO;
import com.raulmbueno.mini_ecommerce.services.InterestSignupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Endpoint público para pré-cadastro de interesse.
 * Não requer autenticação.
 */
@RestController
@RequestMapping(value = "/interest-signups")
public class InterestSignupController {

    private final InterestSignupService service;

    public InterestSignupController(InterestSignupService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InterestSignupDTO> insert(@Valid @RequestBody InterestSignupInsertDTO dto) {
        InterestSignupDTO created = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uri).body(created);
    }
}
