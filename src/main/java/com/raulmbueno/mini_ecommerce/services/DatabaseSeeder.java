package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.entities.Category;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments; 
import org.springframework.boot.ApplicationRunner;   
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DatabaseSeeder implements ApplicationRunner { 

    @Autowired 
    private CategoryRepository categoryRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        categoryRepository.deleteAll(); 

        Category cat1 = new Category(null, "Eletrônicos");
        Category cat2 = new Category(null, "Livros");
        Category cat3 = new Category(null, "Computadores");
        Category cat4 = new Category(null, "Moda");
        Category cat5 = new Category(null, "Decoração");

        categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5));
        
        System.out.println(">>> DatabaseSeeder: 5 Categorias inseridas com sucesso.");
    }
}