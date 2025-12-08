package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.Brand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandDTO {

    private Long id;

    @NotBlank(message = "Nome da marca é obrigatório")
    @Size(min = 2, max = 255, message = "Nome da marca deve ter entre 2 e 255 caracteres")
    private String name;

    // Novo campo para o logo
    private String logoUrl;

    public BrandDTO(Brand entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.logoUrl = entity.getLogoUrl();
    }
}
