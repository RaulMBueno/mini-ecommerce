package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.CategoryDTO;
import com.raulmbueno.mini_ecommerce.entities.Category;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Category category;
    private List<Category> categoryList;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        category = new Category();
        category.setId(existingId);
        category.setName("Eletrônicos");
        
        categoryDTO = new CategoryDTO(category);

        categoryList = List.of(category);
    }

    @Test
    @DisplayName("findAll deve retornar uma lista de CategoryDTO")
    public void findAllShouldReturnListOfCategoryDTO() {
        // Arrange
        when(repository.findAll()).thenReturn(categoryList);

        // Act
        List<CategoryDTO> result = service.findAll();

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(category.getName(), result.get(0).getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("findById deve retornar CategoryDTO quando o ID existe")
    public void findByIdShouldReturnCategoryDTOWhenIdExists() {
        // Arrange
        when(repository.findById(existingId)).thenReturn(Optional.of(category));

        // Act
        CategoryDTO result = service.findById(existingId);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingId, result.getId());
        Assertions.assertEquals(category.getName(), result.getName());
        verify(repository, times(1)).findById(existingId);
    }

    @Test
    @DisplayName("findById deve lançar ResourceNotFoundException quando o ID não existe")
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });

        verify(repository, times(1)).findById(nonExistingId);
    }

    @Test
    @DisplayName("insert deve retornar um CategoryDTO ao salvar uma nova categoria")
    public void insertShouldReturnCategoryDTO() {
        // Arrange
        when(repository.save(any(Category.class))).thenReturn(category);

        // Act
        CategoryDTO result = service.insert(categoryDTO);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(category.getName(), result.getName());
    }

    @Test
    @DisplayName("update deve retornar CategoryDTO quando o ID existe")
    public void updateShouldReturnCategoryDTOWhenIdExists() {
        // Arrange
        when(repository.getReferenceById(existingId)).thenReturn(category);
        when(repository.save(any(Category.class))).thenReturn(category);
        
        // Act
        CategoryDTO result = service.update(existingId, categoryDTO);

        // Assert
        Assertions.assertNotNull(result);
        verify(repository, times(1)).getReferenceById(existingId);
        verify(repository, times(1)).save(category);
    }

    @Test
    @DisplayName("update deve lançar ResourceNotFoundException quando o ID não existe")
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, categoryDTO);
        });

        verify(repository, times(1)).getReferenceById(nonExistingId);
        verify(repository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("delete deve apagar o recurso quando o ID existe")
    public void deleteShouldDoNothingWhenIdExists() {
        // Arrange
        when(repository.existsById(existingId)).thenReturn(true);
        doNothing().when(repository).deleteById(existingId);

        // Act & Assert
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingId);
        });

        verify(repository, times(1)).deleteById(existingId);
    }

    @Test
    @DisplayName("delete deve lançar ResourceNotFoundException quando o ID não existe")
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        // Arrange
        when(repository.existsById(nonExistingId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
});

        verify(repository, never()).deleteById(nonExistingId);
    }

    @Test
    @DisplayName("delete deve lançar DatabaseException em caso de violação de integridade")
    public void deleteShouldThrowDatabaseExceptionWhenIntegrityViolation() {
        // Arrange
        when(repository.existsById(dependentId)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

        // Act & Assert
        assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });

        verify(repository, times(1)).deleteById(dependentId);
    }
}