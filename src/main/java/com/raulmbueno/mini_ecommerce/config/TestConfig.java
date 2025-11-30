package com.raulmbueno.mini_ecommerce.config;

import com.raulmbueno.mini_ecommerce.entities.User;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import com.raulmbueno.mini_ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@Profile("dev") 
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // 1. Limpa tudo para começar zerado
        userRepository.deleteAll();
        productRepository.deleteAll();

        // 2. Cria APENAS os Usuários (Para você conseguir logar)
        
        User u1 = new User();
        u1.setName("Raul Admin");
        u1.setEmail("raul@gmail.com"); 
        u1.setPassword(passwordEncoder.encode("123456")); 
        
        User u2 = new User();
        u2.setName("Maria Cliente");
        u2.setEmail("maria@gmail.com");
        u2.setPassword(passwordEncoder.encode("123456"));

        userRepository.saveAll(Arrays.asList(u1, u2));
        
        System.out.println("--------------------------------------------");
        System.out.println("LOG: USUÁRIOS CRIADOS. LOJA VAZIA PRONTA PARA CADASTRO!");
        System.out.println("--------------------------------------------");
    }
}