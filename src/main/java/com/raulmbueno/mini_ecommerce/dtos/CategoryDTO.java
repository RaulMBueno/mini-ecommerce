package com.raulmbueno.mini_ecommerce.dtos;

import java.io.Serializable;

import com.raulmbueno.mini_ecommerce.entities.Category;
import jakarta.validation.constraints.NotBlank; 
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor; 
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
public class CategoryDTO implements Serializable  {
    private static final long serialVersionUID = 1L;
    
    private Long id;

    @NotBlank(message = "O nome da categoria não pode ser vazio.") 
    private String name;
    
    public CategoryDTO(Category entity) {
        this.id = entity.getId();
        this.name = entity.getName();
    }
    
}