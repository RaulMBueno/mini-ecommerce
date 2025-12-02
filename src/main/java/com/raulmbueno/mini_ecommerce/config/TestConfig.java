package com.raulmbueno.mini_ecommerce.config;

import com.raulmbueno.mini_ecommerce.entities.Category;
import com.raulmbueno.mini_ecommerce.entities.Role; // Import Novo
import com.raulmbueno.mini_ecommerce.entities.User;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import com.raulmbueno.mini_ecommerce.repositories.RoleRepository; // Import Novo
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
    @Autowired private RoleRepository roleRepository; // <--- INJEÇÃO NOVA
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // --- ATENÇÃO: COMENTEI A TRAVA PARA FORÇAR A ATUALIZAÇÃO DOS CARGOS ---
        // Se deixarmos isso ativo agora, o Java vai ver o usuário antigo (sem cargo)
        // e não vai corrigir. Precisamos rodar sem isso uma vez.
        
        if (userRepository.count() > 0) {
             return; 
        }
        
        // ---------------------------------------------------------------------

        // 1. Limpeza (Ordem importa para não dar erro de chave estrangeira)
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll(); // Limpa roles antigas

        // 2. Categorias
        Category catMake = new Category(null, "Maquiagem", new HashSet<>());
        Category catSkin = new Category(null, "Skincare", new HashSet<>());
        Category catLips = new Category(null, "Lábios", new HashSet<>()); 
        Category catCursos = new Category(null, "Cursos", new HashSet<>());
        categoryRepository.saveAll(Arrays.asList(catMake, catSkin, catLips, catCursos));

        // 3. Cargos (Roles) - ESSENCIAL PARA SEGURANÇA
        Role roleAdmin = new Role(null, "ROLE_ADMIN");
        Role roleClient = new Role(null, "ROLE_CLIENT");
        roleRepository.saveAll(Arrays.asList(roleAdmin, roleClient));

        // 4. Usuários com Poderes
        User u1 = new User();
        u1.setName("Raul Admin");
        u1.setEmail("raul@gmail.com"); 
        u1.setPassword(passwordEncoder.encode("123456"));
        u1.getRoles().add(roleAdmin); // <--- O PULO DO GATO: Dando poder de Admin
        
        User u2 = new User();
        u2.setName("Maria Cliente");
        u2.setEmail("maria@gmail.com");
        u2.setPassword(passwordEncoder.encode("123456"));
        u2.getRoles().add(roleClient);

        userRepository.saveAll(Arrays.asList(u1, u2));
        
        System.out.println("LOG: AMBIENTE RESETADO COM SUCESSO (ROLES APLICADAS)!");
    }
}