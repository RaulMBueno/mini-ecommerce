package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.ProductDTO;
import com.raulmbueno.mini_ecommerce.entities.Category;
import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import com.raulmbueno.mini_ecommerce.services.exceptions.DatabaseException;
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Product product;
    private Category category;
    private ProductDTO productDTO;
    private Page<Product> page;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        category = new Category(1L, "Eletrônicos", new HashSet<>()); 
        product = new Product(existingId, "Smart TV", "Uma TV inteligente", new BigDecimal("2900.00"), "url-da-imagem", new HashSet<>(), new HashSet<>());
        product.getCategories().add(category);
        
        productDTO = new ProductDTO(product);
        page = new PageImpl<>(List.of(product));
    }

    @Test
    @DisplayName("findAllPaged deve retornar uma página de ProductDTO")
    public void findAllPagedShouldReturnPageOfProductDTO() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<ProductDTO> result = service.findAllPaged(pageable);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getSize());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("findById deve retornar ProductDTO quando o ID existe")
    public void findByIdShouldReturnProductDTOWhenIdExists() {
        // Arrange
        when(productRepository.findById(existingId)).thenReturn(Optional.of(product));

        // Act
        ProductDTO result = service.findById(existingId);

        // Assert
        Assertions.assertNotNull(result);
        verify(productRepository, times(1)).findById(existingId);
    }

    @Test
    @DisplayName("findById deve lançar ResourceNotFoundException quando o ID não existe")
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
        verify(productRepository, times(1)).findById(nonExistingId);
    }

    @Test
    @DisplayName("insert deve retornar ProductDTO")
    public void insertShouldReturnProductDTO() {
        // Arrange
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        ProductDTO result = service.insert(productDTO);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(product.getName(), result.getName());
    }

    @Test
    @DisplayName("update deve retornar ProductDTO quando o ID existe")
    public void updateShouldReturnProductDTOWhenIdExists() {
        // Arrange
        when(productRepository.getReferenceById(existingId)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(categoryRepository.getReferenceById(category.getId())).thenReturn(category);

        // Act
        ProductDTO result = service.update(existingId, productDTO);

        // Assert
        Assertions.assertNotNull(result);
        verify(productRepository, times(1)).getReferenceById(existingId);
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("update deve lançar ResourceNotFoundException quando o ID não existe")
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, productDTO);
        });
        verify(productRepository, times(1)).getReferenceById(nonExistingId);
    }

    @Test
    @DisplayName("delete deve apagar o recurso quando o ID existe")
    public void deleteShouldDoNothingWhenIdExists() {
        // Arrange
        when(productRepository.existsById(existingId)).thenReturn(true);
        doNothing().when(productRepository).deleteById(existingId);

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });
        verify(productRepository, times(1)).deleteById(existingId);
    }

    @Test
    @DisplayName("delete deve lançar ResourceNotFoundException quando o ID não existe")
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        when(productRepository.existsById(nonExistingId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
        verify(productRepository, never()).deleteById(nonExistingId);
    }

    @Test
    @DisplayName("delete deve lançar DatabaseException em caso de violação de integridade")
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        // Arrange
        when(productRepository.existsById(dependentId)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

        // Act & Assert
        assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });
        verify(productRepository, times(1)).deleteById(dependentId);
    }
}