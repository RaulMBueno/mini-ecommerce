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

import java.util.List;

@Service
public class BrandService {

    @Autowired
    private BrandRepository repository;

    @Transactional(readOnly = true)
    public List<BrandDTO> findAll() {
        List<Brand> list = repository.findAll(Sort.by("name").ascending());
        return list.stream().map(BrandDTO::new).toList();
    }

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
        entity.setLogoUrl(dto.getLogoUrl()); // novo campo

        entity = repository.save(entity);
        return new BrandDTO(entity);
    }

    @Transactional
    public BrandDTO update(Long id, BrandDTO dto) {
        try {
            Brand entity = repository.getReferenceById(id);
            entity.setName(dto.getName().trim());
            entity.setLogoUrl(dto.getLogoUrl()); // atualiza logo também
            entity = repository.save(entity);
            return new BrandDTO(entity);
        } catch (jakarta.persistence.EntityNotFoundException e) {
            throw new ResourceNotFoundException("Marca não encontrada. Id: " + id);
        }
    }

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
}
