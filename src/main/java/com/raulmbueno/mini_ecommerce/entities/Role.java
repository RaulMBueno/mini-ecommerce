package com.raulmbueno.mini_ecommerce.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Entity
@Table(name = "tb_role")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Role implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String authority; // Ex: "ROLE_ADMIN", "ROLE_CLIENT"
    // Padrão Spring Security para nomes de roles: prefixo "ROLE_"
}