package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.enums.ProductType;
import com.raulmbueno.mini_ecommerce.entities.Category; 
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

    @Size(min = 3, max = 80, message = "O nome precisa ter entre 3 e 80 caracteres")
    @NotBlank(message = "O nome do produto não pode ser vazio.")
    private String name;
    
    @NotBlank(message = "A descrição não pode ser vazia.")
    private String description;
    
    @Positive(message = "O preço deve ser um valor positivo.")
    private BigDecimal price;
    
    private String imgUrl;

    private ProductType type;

    private final List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        this.type = entity.getType();

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
}