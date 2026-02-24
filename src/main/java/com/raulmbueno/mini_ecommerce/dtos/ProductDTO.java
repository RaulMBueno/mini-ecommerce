package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.entities.enums.ProductType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO {

    private Long id;

    @Size(min = 3, max = 255, message = "Nome deve ter entre 3 e 255 caracteres")
    @NotBlank(message = "Campo requerido")
    private String name;

    @Size(min = 10, message = "Descrição precisa ter pelo menos 10 letras")
    private String description;

    @Positive(message = "O preço deve ser um valor positivo.")
    private BigDecimal price;

    private String imgUrl;

    // 👉 NOVO CAMPO: marca do produto
    @Size(max = 255, message = "Marca deve ter no máximo 255 caracteres")
    private String brand;

    private ProductType type;

    private String affiliateUrl;

    // Destaque na vitrine
    private Boolean isFeatured;

    /** Prioridade na home (0–999). Maior = aparece antes na vitrine. */
    @jakarta.validation.constraints.Min(value = 0, message = "Prioridade deve ser >= 0")
    private Integer homePriority = 0;

    // Categorias associadas
    private final List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.brand = entity.getBrand();
        this.affiliateUrl = entity.getAffiliateUrl();
        this.type = entity.getType();
        this.isFeatured = entity.getIsFeatured();
        this.homePriority = entity.getHomePriority() != null ? entity.getHomePriority() : 0;

        entity.getCategories().forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }
}
