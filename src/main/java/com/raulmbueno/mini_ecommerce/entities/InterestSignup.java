package com.raulmbueno.mini_ecommerce.entities;

import com.raulmbueno.mini_ecommerce.entities.enums.InterestType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Pré-cadastro de interesse.
 * Independente de autenticação e pedidos.
 * Reutilizável para cursos, produtos próprios, novidades, lançamentos.
 */
@Entity
@Table(name = "tb_interest_signup")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class InterestSignup implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 255)
    private String email;

    @Column(length = 30)
    private String whatsapp;

    @Enumerated(EnumType.STRING)
    @Column(name = "interest_type", nullable = false, length = 20)
    private InterestType interestType;

    @Column(name = "interest_reference", length = 255)
    private String interestReference;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    public void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}
