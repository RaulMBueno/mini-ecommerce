package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    public Product save(Product product) {
        return productRepository.save(product);
    
    }
    public Optional<Product> findById(Long id) {
    return productRepository.findById(id);
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
