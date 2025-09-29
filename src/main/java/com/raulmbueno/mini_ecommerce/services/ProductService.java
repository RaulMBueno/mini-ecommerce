package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.CategoryDTO;
import com.raulmbueno.mini_ecommerce.dtos.ProductDTO;
import com.raulmbueno.mini_ecommerce.entities.Category;
import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
public class ProductService {
    @Autowired 
    private CategoryRepository categoryRepository; 


    @Autowired
    private ProductRepository productRepository;


    @Autowired
    private ProductRepository repository; 
    
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> list = productRepository.findAll();
        return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return new ProductDTO(entity); 
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) { 
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }
    
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) { 
        try {
            Product entity = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
            
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Resource not found. ID: " + id);
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> list = repository.findAll(pageable);
    
        return list.map(ProductDTO::new);
    }
 
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found. ID: " + id);
        }
        productRepository.deleteById(id);
    }
    
    private void copyDtoToEntity(ProductDTO dto, Product entity) {
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());
    entity.setPrice(dto.getPrice());
    entity.setImgUrl(dto.getImgUrl());
    entity.getCategories().clear(); 
        for (CategoryDTO catDTO : dto.getCategories()) {
             Category category = categoryRepository.findById(catDTO.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + catDTO.getId()));   
            entity.getCategories().add(category);
    }
    }
}