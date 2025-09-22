package com.raulmbueno.mini_ecommerce.repositories;

import com.raulmbueno.mini_ecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}