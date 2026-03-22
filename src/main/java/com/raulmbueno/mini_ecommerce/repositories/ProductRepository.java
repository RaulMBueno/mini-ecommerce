package com.raulmbueno.mini_ecommerce.repositories;

import com.raulmbueno.mini_ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    @EntityGraph(attributePaths = "categories")
    List<Product> findAll();

    @Override
    @EntityGraph(attributePaths = "categories")
    Page<Product> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "categories")
    Optional<Product> findById(Long id);

    /**
     * Busca produtos cujo nome contenha a string fornecida, ignorando maiúsculas/minúsculas.
     * Exemplo: uma busca por "phone" encontrará "iPhone" e "telefone".
     *
     * @param name O texto a ser buscado no nome do produto.
     * @return Uma lista de produtos que correspondem ao critério de busca.
     */
    @EntityGraph(attributePaths = "categories")
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}