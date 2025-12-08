package com.raulmbueno.mini_ecommerce.controllers;

import com.raulmbueno.mini_ecommerce.dtos.BrandDTO;
import com.raulmbueno.mini_ecommerce.services.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    // LISTAR TODAS AS MARCAS (Home + painel admin)
    @GetMapping
    public ResponseEntity<List<BrandDTO>> findAll() {
        List<BrandDTO> list = brandService.findAll();
        return ResponseEntity.ok(list);
    }

    // CRIAR NOVA MARCA
    @PostMapping
    public ResponseEntity<BrandDTO> insert(@RequestBody @Valid BrandDTO dto) {
        BrandDTO saved = brandService.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(uri).body(saved);
    }

    // ATUALIZAR MARCA (se quiser usar depois)
    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> update(@PathVariable Long id,
                                           @RequestBody @Valid BrandDTO dto) {
        BrandDTO updated = brandService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETAR MARCA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
