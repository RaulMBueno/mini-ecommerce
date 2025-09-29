package com.raulmbueno.mini_ecommerce.dtos;
import com.raulmbueno.mini_ecommerce.entities.Category;
import lombok.Getter;
import lombok.NoArgsConstructor; 
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class CategoryDTO {

    private Long id;
    private String name;

    public CategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryDTO(Category entity) {
        id = entity.getId();
        name = entity.getName();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}