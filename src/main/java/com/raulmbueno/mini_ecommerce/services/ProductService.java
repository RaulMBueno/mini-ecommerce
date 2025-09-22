package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}