package com.raulmbueno.mini_ecommerce.config;

import com.raulmbueno.mini_ecommerce.entities.Category;
import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.entities.User;
import com.raulmbueno.mini_ecommerce.entities.enums.ProductType;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import com.raulmbueno.mini_ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
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
        
        // Se quiser resetar tudo para testar os destaques, deixe descomentado:
        // Se quiser manter o que cadastrou no Admin, comente as linhas abaixo.
        // MAS ATENÇÃO: Se não rodar isso uma vez, os produtos antigos terão 'isFeatured = null'.
        userRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // 1. Categorias
        Category catMake = new Category(null, "Maquiagem", new HashSet<>());
        Category catSkin = new Category(null, "Skincare", new HashSet<>());
        Category catLips = new Category(null, "Lábios", new HashSet<>()); 
        Category catCursos = new Category(null, "Cursos", new HashSet<>());

        categoryRepository.saveAll(Arrays.asList(catMake, catSkin, catLips, catCursos));

        // 2. Produtos (Usando Setters para não errar a ordem)
        
        // Produto 1: Batom
        Product p1 = new Product();
        p1.setName("Batom Matte Pink");
        p1.setDescription("Alta cobertura e longa duração.");
        p1.setPrice(new BigDecimal("45.90"));
        p1.setImgUrl("https://images.unsplash.com/photo-158649577744-4413f21062fa?q=80&w=2030");
        p1.setType(ProductType.PHYSICAL);
        p1.setIsFeatured(true); // <--- ESTE É DESTAQUE!
        p1.getCategories().add(catLips); // Adiciona na categoria certa

        // Produto 2: Kit Pincéis
        Product p2 = new Product();
        p2.setName("Kit Pincéis Profissionais");
        p2.setDescription("Kit completo Amazon.");
        p2.setPrice(new BigDecimal("120.00"));
        p2.setImgUrl("https://images.pexels.com/photos/3018845/pexels-photo-3018845.jpeg");
        p2.setAffiliateUrl("https://www.amazon.com.br");
        p2.setType(ProductType.AFFILIATE);
        p2.setIsFeatured(false); // Não é destaque
        p2.getCategories().add(catMake);

        // Produto 3: Curso
        Product p3 = new Product();
        p3.setName("Curso Auto-Maquiagem");
        p3.setDescription("Aprenda em casa.");
        p3.setPrice(new BigDecimal("97.00"));
        p3.setImgUrl("https://images.unsplash.com/photo-1522337660859-02fbefca4702");
        p3.setType(ProductType.DIGITAL);
        p3.setIsFeatured(true); // <--- ESTE TAMBÉM É DESTAQUE!
        p3.getCategories().add(catCursos);

        productRepository.saveAll(Arrays.asList(p1, p2, p3));

        // 3. Usuários
        User u1 = new User();
        u1.setName("Raul Admin");
        u1.setEmail("raul@gmail.com");
        u1.setPassword(passwordEncoder.encode("123456"));

        User u2 = new User();
        u2.setName("Maria Cliente");
        u2.setEmail("maria@gmail.com");
        u2.setPassword(passwordEncoder.encode("123456"));

        userRepository.saveAll(Arrays.asList(u1, u2));

        System.out.println("LOG: DESTAQUES, CATEGORIAS E PRODUTOS ATUALIZADOS!");
    }
}