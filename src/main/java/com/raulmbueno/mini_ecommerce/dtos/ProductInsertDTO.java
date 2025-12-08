package com.raulmbueno.mini_ecommerce.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductInsertDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank(message = "O campo 'nome' é obrigatório e não pode ser vazio.")
    @Size(min = 5, max = 60, message = "O nome deve ter entre 5 e 60 caracteres.")
    private String name;
    @NotNull(message = "O campo 'descrição' é obrigatório.")
    private String description;
    @NotNull(message = "O campo 'preço' é obrigatório.")
    @Positive(message = "O preço deve ser um valor positivo.")
    private BigDecimal price; 
    private String imgUrl;
    @Size(max = 255, message = "A marca deve ter no máximo 255 caracteres.")
    private String brand;


    private Set<Long> categoryIds = new HashSet<>();

}