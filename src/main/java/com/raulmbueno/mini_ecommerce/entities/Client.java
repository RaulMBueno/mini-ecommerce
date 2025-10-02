package com.raulmbueno.mini_ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um cliente no sistema.
 * Um cliente pode ter múltiplos pedidos associados a ele.
 */
@Entity
@Table(name = "tb_client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "orders") // Gera o toString(), mas EXCLUI o campo 'orders'.
@EqualsAndHashCode(of = "id")
public class Client implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();
}