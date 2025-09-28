package com.raulmbueno.mini_ecommerce.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data // Gera getters, setters, equals, hashCode
@NoArgsConstructor // Construtor sem argumentos
@Table(name = "tb_product") // Adicionei a anotação @Table para consistência
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // CRÍTICO: Resolve ambiguidades da PK
    private Long id;

    private String name;
    private String description;
    private BigDecimal price;
    private String imgUrl;

    @ManyToMany
    @JoinTable(name = "tb_product_category",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();
    
    // Construtor manual (mantido, mas pode ser simplificado se você usar @AllArgsConstructor)
    public Product(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    
    // NOTA: Todos os outros getters/setters são gerados pelo @Data
}