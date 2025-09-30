package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size; 
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
@Getter
@Setter
@NoArgsConstructor
public class UserInsertDTO extends UserDTO { 

    @NotBlank(message = "O campo senha é obrigatório")
    @Size(min = 8, max = 20, message = "A senha deve ter entre 8 e 20 caracteres")
    private String password;
    public UserInsertDTO(User entity) {
        super(entity); 
    }
}