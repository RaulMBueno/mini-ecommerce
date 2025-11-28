package com.raulmbueno.mini_ecommerce.config;

import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.entities.enums.ProductType;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

@Configuration
@Profile("dev") 
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        
       
        productRepository.deleteAll();

        
        Product p1 = new Product(
            null, 
            "Batom Matte Pink", 
            "Alta cobertura e longa duração.", 
            new BigDecimal("45.90"), 
            "https://images.unsplash.com/photo-1586495777744-4413f21062fa?q=80&w=2030&auto=format&fit=crop", 
            null, 
            ProductType.PHYSICAL, 
            new HashSet<>(), 
            new HashSet<>()
        );

        
        Product p2 = new Product(
            null, 
            "Kit Pincéis Profissionais", 
            "Kit completo vendido pela Amazon.", 
            new BigDecimal("120.00"), 
            "https://images.pexels.com/photos/3018845/pexels-photo-3018845.jpeg?auto=compress&cs=tinysrgb&w=800",
            "https://www.google.com", 
            ProductType.AFFILIATE, 
            new HashSet<>(), 
            new HashSet<>()
        );

        
        productRepository.saveAll(Arrays.asList(p1, p2));
        
        System.out.println("--------------------------------------------");
        System.out.println("LOG: BANCO DE DADOS POPULADO COM SUCESSO!");
        System.out.println("--------------------------------------------");
    }
}