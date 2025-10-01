package com.raulmbueno.mini_ecommerce.repositories;

import com.raulmbueno.mini_ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // MUDANÇA: Spring Data JPA implementa este método automaticamente!
    User findByEmail(String email); 
}