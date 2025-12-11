package com.raulmbueno.mini_ecommerce.controllers;

import com.raulmbueno.mini_ecommerce.dtos.BrandDTO;
import com.raulmbueno.mini_ecommerce.entities.Brand;
import com.raulmbueno.mini_ecommerce.repositories.BrandRepository;
import com.raulmbueno.mini_ecommerce.services.BrandService;
import com.raulmbueno.mini_ecommerce.services.BrandLogoStorageService;
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandLogoStorageService brandLogoStorageService;

    // LISTAR TODAS AS MARCAS (usado no painel e na Home)
    @GetMapping
    public ResponseEntity<List<BrandDTO>> findAll() {
        List<BrandDTO> dto = brandService.findAll();
        return ResponseEntity.ok(dto);
    }

    // CRIAR NOVA MARCA (painel admin)
    @PostMapping
    public ResponseEntity<BrandDTO> insert(@RequestBody @Valid BrandDTO dto) {
        BrandDTO created = brandService.insert(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(uri).body(created);
    }

    // ATUALIZAR NOME DA MARCA (se precisar)
    @PutMapping("/{id}")
    public ResponseEntity<BrandDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid BrandDTO dto
    ) {
        BrandDTO updated = brandService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETAR MARCA
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // UPLOAD DA LOGO DA MARCA (agora usando Cloudinary via BrandLogoStorageService)
    @PostMapping("/{id}/logo")
    public ResponseEntity<BrandDTO> uploadLogo(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de logo é obrigatório.");
        }

        // Busca a marca
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada. Id: " + id));

        // Envia para o Cloudinary (ou outro provedor) e obtém a URL segura
        String logoUrl = brandLogoStorageService.storeBrandLogo(brand, file);

        // Atualiza a entidade com a nova URL e persiste
        brand.setLogoUrl(logoUrl);
        brand = brandRepository.save(brand);

        // Retorna DTO atualizado
        BrandDTO response = new BrandDTO(brand);
        return ResponseEntity.ok(response);
    }
}
