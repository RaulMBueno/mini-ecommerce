package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.CategoryDTO;
import com.raulmbueno.mini_ecommerce.entities.Category;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
import com.raulmbueno.mini_ecommerce.services.exceptions.DatabaseException;
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> result = repository.findAll();
        return result.stream().map(CategoryDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria com ID " + id + " não encontrada"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        try {
            Category entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Categoria com ID " + id + " não encontrada");
        }
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria com ID " + id + " não encontrada");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade: esta categoria pode estar em uso por um produto.");
        }
    }

    private void copyDtoToEntity(CategoryDTO dto, Category entity) {
        entity.setName(dto.getName());
    }
}