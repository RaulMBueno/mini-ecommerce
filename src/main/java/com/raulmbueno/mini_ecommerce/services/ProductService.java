package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.ProductDTO;
import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import com.raulmbueno.mini_ecommerce.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> findAll() {
    List<Product> list = productRepository.findAll();
    return list.stream().map(x -> new ProductDTO(x)).collect(Collectors.toList());
    }    
    
    public Product save(Product product) {
        return productRepository.save(product);
    
    }
    public Product findById(Long id) {
    Optional<Product> obj = productRepository.findById(id);
    return obj.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
    }
    public void delete(Long id) {
    productRepository.deleteById(id);
    }
    public Product update(Long id, Product product) {
    try {
    Product entity = productRepository.getReferenceById(id);
        entity.setName(product.getName());
        entity.setPrice(product.getPrice());
        return productRepository.save(entity);
    } catch (EntityNotFoundException e) {
        throw new RuntimeException("Resource not found");
    }
    }
}
