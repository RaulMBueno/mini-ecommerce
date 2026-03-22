package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.enums.InterestType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InterestSignupInsertDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres.")
    private String name;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 255)
    private String email;

    @Size(max = 30, message = "O WhatsApp deve ter no máximo 30 caracteres.")
    private String whatsapp;

    @NotNull(message = "O tipo de interesse é obrigatório.")
    private InterestType interestType;

    @Size(max = 255, message = "A referência deve ter no máximo 255 caracteres.")
    private String interestReference;

    @Size(max = 1000, message = "A mensagem deve ter no máximo 1000 caracteres.")
    private String message;
}
