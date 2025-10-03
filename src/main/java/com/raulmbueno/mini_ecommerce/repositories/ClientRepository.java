package com.raulmbueno.mini_ecommerce.repositories;

import com.raulmbueno.mini_ecommerce.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Busca um cliente pelo seu endereço de e-mail.
     * Como o e-mail é único, espera-se que retorne no máximo um cliente.
     *
     * @param email O e-mail a ser buscado.
     * @return um Optional contendo o Client, se encontrado, ou um Optional vazio caso contrário.
     */
    Optional<Client> findByEmail(String email);
}