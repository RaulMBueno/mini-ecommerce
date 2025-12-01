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
    private ProductType type;
    private String affiliateUrl;

    private final List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.affiliateUrl = entity.getAffiliateUrl();
        this.type = entity.getType();
        this.affiliateUrl = entity.getAffiliateUrl();
        this.type = entity.getType();
        
        this.isFeatured = entity.getIsFeatured(); // NOVO

        entity.getCategories().forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }

    
    public ProductType getType() {
        return type;
    }

    public void setType(ProductType type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    // ... outros campos
    private Boolean isFeatured;

}