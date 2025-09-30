package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.User;
import jakarta.validation.constraints.Email; // Para garantir que é um email válido
import jakarta.validation.constraints.NotBlank; // Para garantir que o campo não está vazio
import jakarta.validation.constraints.Size; // Para definir o tamanho mínimo/máximo da senha
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

// UserInsertDTO é um DTO específico para a operação de inserção (POST).
// Ele é o único que contém o campo 'password'.

@Getter
@Setter
@NoArgsConstructor
public class UserInsertDTO extends UserDTO { // Herda campos básicos de UserDTO (name, email)

    @NotBlank(message = "O campo senha é obrigatório")
    @Size(min = 8, max = 20, message = "A senha deve ter entre 8 e 20 caracteres")
    private String password;

    // Construtor (opcional, mas bom para inicializar)
    public UserInsertDTO(User entity) {
        // Chama o construtor do UserDTO (pai)
        super(entity); 
        // Não inicializamos a senha aqui porque a entidade User só armazena a senha hash.
    }
}