package com.raulmbueno.mini_ecommerce.entities;

import jakarta.persistence.*; // Imports do JPA para mapeamento de banco
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode; // Para comparar objetos de forma única

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity // Diz ao JPA que esta classe é uma tabela no banco de dados
@Table(name = "tb_user") // Define o nome da tabela no banco (boa prática)
@Getter // Gera todos os métodos getters do Lombok
@Setter // Gera todos os métodos setters do Lombok
@NoArgsConstructor // Gera um construtor sem argumentos do Lombok
@EqualsAndHashCode(of = "id") // Gera os métodos equals e hashCode baseados no 'id' (importante para comparações)
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id // Marca este campo como a chave primária da tabela
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Configura o banco para gerar o ID automaticamente (auto-incremento)
    private Long id;

    private String name; // Nome do usuário
    
    @Column(unique = true) // Garante que não haverá dois usuários com o mesmo email no banco
    private String email; // Email do usuário (usado para login)

    private String password; // Senha do usuário (ATENÇÃO: precisa ser criptografada!)

    // Relacionamento com Roles (perfis de usuário, ex: ADMIN, CLIENTE)
    // Muitos usuários podem ter muitas roles, e uma role pode pertencer a muitos usuários
    @ManyToMany // Relacionamento muitos-para-muitos
    @JoinTable(name = "tb_user_role", // Nome da tabela intermediária que conecta User e Role
               joinColumns = @JoinColumn(name = "user_id"), // Coluna que referencia o ID do User
               inverseJoinColumns = @JoinColumn(name = "role_id")) // Coluna que referencia o ID da Role
    private Set<Role> roles = new HashSet<>(); // Usa Set para evitar roles duplicadas
}