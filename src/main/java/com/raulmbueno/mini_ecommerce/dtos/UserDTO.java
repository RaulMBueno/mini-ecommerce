package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.User;
import jakarta.validation.constraints.Email; // Para garantir que o formato é um email
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// UserDTO é o DTO padrão para enviar e receber dados, exceto a senha.

@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "O campo nome é obrigatório")
    @Size(min = 3, max = 60, message = "O nome deve ter entre 3 e 60 caracteres")
    private String name;

    @NotBlank(message = "O campo email é obrigatório")
    @Email(message = "Por favor, digite um email válido") // Valida o formato "xxx@yyy.com"
    private String email;

    // Construtor que converte uma Entidade (User) para um DTO (UserDTO)
    public UserDTO(User entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        // NOTA: A senha NUNCA é copiada para o DTO de Saída
    }
}