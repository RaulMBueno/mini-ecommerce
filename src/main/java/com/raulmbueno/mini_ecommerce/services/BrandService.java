package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.BrandDTO;
import com.raulmbueno.mini_ecommerce.entities.Brand;
import com.raulmbueno.mini_ecommerce.repositories.BrandRepository;
import com.raulmbueno.mini_ecommerce.services.exceptions.DatabaseException;
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandRepository repository;

    // -------- LISTAR TODAS --------
    @Transactional(readOnly = true)
    public List<BrandDTO> findAll() {
        List<Brand> list = repository.findAll(Sort.by("name").ascending());
        return list.stream().map(BrandDTO::new).toList();
    }

    // -------- INSERIR --------
    @Transactional
    public BrandDTO insert(BrandDTO dto) {
        String normalizedName = dto.getName().trim();

        // Evita cadastrar a mesma marca 2x (case-insensitive)
        repository.findByNameIgnoreCase(normalizedName)
                .ifPresent(b -> {
                    throw new DatabaseException("Marca já cadastrada: " + normalizedName);
                });

        Brand entity = new Brand();
        entity.setName(normalizedName);
        entity = repository.save(entity);
        return new BrandDTO(entity);
    }

    // -------- ATUALIZAR NOME --------
    @Transactional
    public BrandDTO update(Long id, BrandDTO dto) {
        try {
            Brand entity = repository.getReferenceById(id);
            entity.setName(dto.getName().trim());
            entity = repository.save(entity);
            return new BrandDTO(entity);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new ResourceNotFoundException("Marca não encontrada. Id: " + id);
        }
    }

    // -------- DELETAR --------
    @Transactional
    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Marca não encontrada. Id: " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade ao apagar marca");
        }
    }

    // -------- ATUALIZAR LOGO (UPLOAD) --------
    @Transactional
    public BrandDTO updateLogo(Long brandId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new DatabaseException("Arquivo de logo vazio ou ausente");
        }

        // Busca a marca
        Brand brand = repository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Marca não encontrada. Id: " + brandId));

        try {
            // Diretório onde os arquivos serão salvos (pasta relativa ao projeto)
            String rootFolder = "uploads/brand-logos";
            Path uploadDir = Paths.get(rootFolder).toAbsolutePath().normalize();

            // Garante que o diretório existe
            Files.createDirectories(uploadDir);

            // Descobre a extensão do arquivo (ex.: .png, .jpg)
            String originalName = file.getOriginalFilename();
            String extension = ".png"; // padrão

            if (originalName != null && originalName.contains(".")) {
                extension = originalName.substring(originalName.lastIndexOf("."));
            }

            // Nome final do arquivo, ex.: brand-12.png
            String fileName = "brand-" + brandId + extension;

            // Caminho completo no disco
            Path targetPath = uploadDir.resolve(fileName);

            // Copia/substitui o arquivo
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Caminho público que vamos guardar no banco (depois configuramos para servir isso)
            String publicUrl = "/uploads/brand-logos/" + fileName;

            brand.setLogoUrl(publicUrl);
            brand = repository.save(brand);

            return new BrandDTO(brand);

        } catch (IOException e) {
            throw new DatabaseException("Erro ao salvar arquivo de logo: " + e.getMessage());
        }
    }
}
