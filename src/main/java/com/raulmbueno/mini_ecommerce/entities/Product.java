package com.raulmbueno.mini_ecommerce.entities;

import com.raulmbueno.mini_ecommerce.entities.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * Representa um produto no catálogo do e-commerce.
 * Um produto pode pertencer a múltiplas categorias.
 */
@Entity
@Table(name = "tb_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    private String imgUrl;

    // 👉 NOVO CAMPO: marca do produto
    @Column(length = 255)
    private String brand;

    @Column(columnDefinition = "TEXT")
    private String affiliateUrl;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    private Boolean isFeatured;

    /** Prioridade na home (maior = aparece primeiro). Default 0. */
    @Column(name = "home_priority", nullable = false)
    private Integer homePriority = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    @ManyToMany
    @JoinTable(
            name = "tb_product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "id.product")
    private Set<OrderItem> items = new HashSet<>();

    /**
     * 🔁 Construtor de compatibilidade usado pelos testes antigos.
     * Ele NÃO recebe brand, igual era antes de criarmos o campo.
     */
    public Product(
            Long id,
            String name,
            String description,
            BigDecimal price,
            String imgUrl,
            String affiliateUrl,
            ProductType type,
            Boolean isFeatured,
            Set<Category> categories,
            Set<OrderItem> items
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;

        // testes antigos não sabem de brand → deixamos null por padrão
        this.brand = null;

        this.affiliateUrl = affiliateUrl;
        this.type = type;
        this.isFeatured = isFeatured;
        this.homePriority = 0;
        this.createdAt = Instant.now();

        if (categories != null) {
            this.categories = categories;
        }

        if (items != null) {
            this.items = items;
        }
    }
}
