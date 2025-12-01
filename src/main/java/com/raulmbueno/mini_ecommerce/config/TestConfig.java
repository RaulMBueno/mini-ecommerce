package com.raulmbueno.mini_ecommerce.config;

import com.raulmbueno.mini_ecommerce.entities.Category;
import com.raulmbueno.mini_ecommerce.entities.User;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import com.raulmbueno.mini_ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@Profile("dev") 
public class TestConfig implements CommandLineRunner {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. TRAVA DE SEGURANÇA: Se já tem usuários, NÃO FAZ NADA.
        // Isso protege seus dados reais de serem apagados.
        if (userRepository.count() > 0) {
            return; 
        }

        // 2. Se chegou aqui, o banco está VAZIO. Vamos preparar o terreno.
        
        // Limpeza de garantia
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();

        // 3. Cria apenas as CATEGORIAS BÁSICAS (Para o menu não ficar vazio)
        Category catMake = new Category(null, "Maquiagem", new HashSet<>());
        Category catSkin = new Category(null, "Skincare", new HashSet<>());
        Category catLips = new Category(null, "Lábios", new HashSet<>()); 
        Category catCursos = new Category(null, "Cursos", new HashSet<>());

        categoryRepository.saveAll(Arrays.asList(catMake, catSkin, catLips, catCursos));

        // --- SEM PRODUTOS DE TESTE AQUI ---
        // A loja começará vazia.

        // 4. Cria o seu USUÁRIO ADMIN (Para você conseguir entrar)
        User u1 = new User();
        u1.setName("Raul Admin");
        u1.setEmail("raul@gmail.com"); 
        u1.setPassword(passwordEncoder.encode("123456")); 
        
        userRepository.saveAll(Arrays.asList(u1));
        
        System.out.println("LOG: AMBIENTE INICIADO! BANCO PRONTO (SEM PRODUTOS DE TESTE).");
    }
}