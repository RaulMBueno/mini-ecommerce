package com.raulmbueno.mini_ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO para criação de usuário. Ignora "roles" e "id" no JSON — ROLE_ADMIN
 * não pode ser atribuído via API, apenas via ProdAdminSeeder ou banco.
 */
@Getter
@Setter
@JsonIgnoreProperties(value = {"roles", "id"})
public class UserInsertDTO extends UserDTO {

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    private String password;

    public UserInsertDTO() {
        super();
    }
}