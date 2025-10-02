package com.raulmbueno.mini_ecommerce.repositories;

import com.raulmbueno.mini_ecommerce.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo seu endereço de e-mail, que é usado como username.
     *
     * @param email O e-mail do usuário a ser buscado.
     * @return um Optional contendo o User, se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<User> findByEmail(String email);
}