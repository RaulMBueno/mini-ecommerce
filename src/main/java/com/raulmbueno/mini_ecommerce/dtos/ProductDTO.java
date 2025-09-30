package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.entities.Category; 
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
public class ProductDTO implements Serializable {

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

    private Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
        
        categories = entity.getCategories().stream().map(CategoryDTO::new).collect(Collectors.toSet());
    }
    
    public ProductDTO(Product entity, Set<Category> categories) {
        this(entity);
        categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
    }
}