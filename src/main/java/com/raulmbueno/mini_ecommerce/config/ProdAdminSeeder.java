package com.raulmbueno.mini_ecommerce.config;

import com.raulmbueno.mini_ecommerce.entities.Role;
import com.raulmbueno.mini_ecommerce.entities.User;
import com.raulmbueno.mini_ecommerce.repositories.RoleRepository;
import com.raulmbueno.mini_ecommerce.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Em produção, cria o primeiro usuário ADMIN se não existir nenhum.
 * Usa variáveis de ambiente ADMIN_EMAIL, ADMIN_PASSWORD e ADMIN_NAME.
 * Nunca armazena senha no código — configure no painel da Railway (ou similar).
 */
@Configuration
@Profile("prod")
@Order(1)
public class ProdAdminSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ProdAdminSeeder.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL:}")
    private String adminEmail;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Value("${ADMIN_NAME:Administrador}")
    private String adminName;

    public ProdAdminSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsAnyAdmin()) {
            logger.info("ProdAdminSeeder: Já existe pelo menos um ADMIN. Nenhuma ação.");
            return;
        }

        if (adminEmail == null || adminEmail.isBlank() || adminPassword == null || adminPassword.isBlank()) {
            logger.warn("ProdAdminSeeder: Nenhum ADMIN encontrado. Configure ADMIN_EMAIL e ADMIN_PASSWORD nas variáveis de ambiente para criar o primeiro admin.");
            return;
        }

        Role roleAdmin = roleRepository.findByAuthority("ROLE_ADMIN")
                .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));

        User admin = new User();
        admin.setName(adminName);
        admin.setEmail(adminEmail.trim().toLowerCase());
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.getRoles().add(roleAdmin);

        userRepository.save(admin);
        logger.info("ProdAdminSeeder: Primeiro ADMIN criado com sucesso: {}", adminEmail);
    }
}
