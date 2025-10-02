package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.entities.Category;
import com.raulmbueno.mini_ecommerce.entities.Role;
import com.raulmbueno.mini_ecommerce.entities.User;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
import com.raulmbueno.mini_ecommerce.repositories.RoleRepository;
import com.raulmbueno.mini_ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Component
public class DatabaseSeeder implements CommandLineRunner { 

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) { 
            Category cat1 = new Category();
            cat1.setName("Eletrônicos");

            Category cat2 = new Category();
            cat2.setName("Livros");

            Category cat3 = new Category();
            cat3.setName("Computadores");

            Category cat4 = new Category();
            cat4.setName("Celulares");

            Category cat5 = new Category();
            cat5.setName("Casa");

            categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4, cat5));
            System.out.println(">>> DatabaseSeeder: 5 Categorias inseridas com sucesso.");
        }
        
        if (roleRepository.count() == 0) {
            Role roleAdmin = new Role(null, "ROLE_ADMIN");
            Role roleClient = new Role(null, "ROLE_CLIENT");
            
            roleRepository.saveAll(Arrays.asList(roleAdmin, roleClient));
            System.out.println(">>> DatabaseSeeder: 2 Perfis (Roles) inseridos com sucesso.");
        }
        
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.getReferenceById(1L); 
            Role clientRole = roleRepository.getReferenceById(2L); 
            User admin = new User();
            admin.setName("Admin da Silva");
            admin.setEmail("admin@ecommerce.com");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.getRoles().add(adminRole); 
            
            // Usuário Cliente
            User client = new User();
            client.setName("Cliente Souza");
            client.setEmail("client@ecommerce.com");
            client.setPassword(passwordEncoder.encode("123456"));
            client.getRoles().add(clientRole); 
            
            userRepository.saveAll(Arrays.asList(admin, client));
            System.out.println(">>> DatabaseSeeder: 2 Usuários (ADMIN e CLIENTE) inseridos com sucesso.");
        }
    }
}