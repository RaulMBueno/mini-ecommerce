package com.raulmbueno.mini_ecommerce.controllers;

import com.raulmbueno.mini_ecommerce.dtos.ProductDTO;
import com.raulmbueno.mini_ecommerce.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductService service;
    @GetMapping
    
    public ResponseEntity<Page<ProductDTO>> findAllPaged(Pageable pageable) {
        
        // Se este método for chamado, ele usa a Paginação.
        Page<ProductDTO> list = service.findAllPaged(pageable);
        
        return ResponseEntity.ok().body(list);
    }
    
    // VERIFIQUE SE NÃO EXISTE NENHUM OUTRO MÉTODO GET /products SEM PARÂMETROS
    // public ResponseEntity<List<ProductDTO>> findAll() { ... }
    
    // ... (restante do CRUD: findById, insert, update, delete)


    public ResponseEntity<List<ProductDTO>> findAll() {
        List<ProductDTO> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO dto = service.findById(id);
        return ResponseEntity.ok().body(dto);
    }
    
    @PostMapping
    public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto) { 
        dto = service.insert(dto);
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }
    
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto) { 
        dto = service.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}