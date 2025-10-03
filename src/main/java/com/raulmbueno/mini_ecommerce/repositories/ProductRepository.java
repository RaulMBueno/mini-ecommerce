package com.raulmbueno.mini_ecommerce.repositories;

import com.raulmbueno.mini_ecommerce.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Busca produtos cujo nome contenha a string fornecida, ignorando maiúsculas/minúsculas.
     * Exemplo: uma busca por "phone" encontrará "iPhone" e "telefone".
     *
     * @param name O texto a ser buscado no nome do produto.
     * @return Uma lista de produtos que correspondem ao critério de busca.
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}