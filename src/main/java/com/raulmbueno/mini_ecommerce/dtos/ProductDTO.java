package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.Product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imgUrl;
    
    // O DTO do produto deve incluir a lista de categorias no formato DTO
    private Set<CategoryDTO> categories = new HashSet<>();

    // Construtor padrão (vazio)
    public ProductDTO() {
    }

    // Construtor com todos os campos básicos (sem categorias)
    public ProductDTO(Long id, String name, String description, BigDecimal price, String imgUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
    }

    // CONSTRUTOR DE CONVERSÃO: Converte a entidade Product para ProductDTO
    public ProductDTO(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        
        // Converte o Set<Category> (Entidade) para Set<CategoryDTO>
        categories = entity.getCategories().stream().map(x -> new CategoryDTO(x)).collect(Collectors.toSet());
    }

    // --- GETTERS & SETTERS (Opcionais, mas mantidos para DTOs) ---
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }
}