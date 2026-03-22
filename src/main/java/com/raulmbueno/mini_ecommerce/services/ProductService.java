package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.CategoryDTO;
import com.raulmbueno.mini_ecommerce.dtos.ProductDTO;
import com.raulmbueno.mini_ecommerce.entities.Category; 
import com.raulmbueno.mini_ecommerce.entities.Product;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
import com.raulmbueno.mini_ecommerce.repositories.ProductRepository;
import com.raulmbueno.mini_ecommerce.entities.enums.ProductType;
import com.raulmbueno.mini_ecommerce.services.exceptions.DatabaseException;
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("null")
public class ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> list = productRepository.findAll();
        return list.stream().map(ProductDTO::new).collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(String search, Pageable pageable) {
        Page<Product> list;
        Sort baseSort = Sort.by(Sort.Direction.DESC, "homePriority");
        Sort dateSort = Sort.by(Sort.Direction.DESC, "createdAt");
        Sort idSort = Sort.by(Sort.Direction.DESC, "id");
        Sort requestedSort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.unsorted();
        Sort finalSort = baseSort.and(requestedSort).and(dateSort).and(idSort);
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                finalSort
        );
        if (search == null || search.trim().isEmpty()) {
            list = productRepository.findAll(sortedPageable);
        } else {
            list = productRepository.findByNameContainingIgnoreCase(search.trim(), sortedPageable);
        }
        return list.map(ProductDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " não encontrado"));
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        validatePriceForType(dto);
        validateAffiliateUrlWhenAffiliate(dto);
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {
        validatePriceForType(dto);
        validateAffiliateUrlWhenAffiliate(dto);
        Product entity = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto com ID " + id + " não encontrado"));
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto com ID " + id + " não encontrado");
        }
        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade: este produto pode estar em um pedido.");
        }
    }

    /**
     * Para tipo AFFILIATE, preço é opcional (pode ser null).
     * Para PHYSICAL e DIGITAL, preço é obrigatório e deve ser positivo.
     */
    private void validatePriceForType(ProductDTO dto) {
        if (dto.getType() == null || dto.getType() == ProductType.AFFILIATE) {
            return;
        }
        BigDecimal price = dto.getPrice();
        if (price == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Preço é obrigatório para produtos Físico ou Digital.");
        }
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Preço deve ser positivo para produtos Físico ou Digital.");
        }
    }

    /**
     * Quando tipo é AFFILIATE, exige affiliateUrl preenchido.
     */
    private void validateAffiliateUrlWhenAffiliate(ProductDTO dto) {
        if (dto.getType() != ProductType.AFFILIATE) {
            return;
        }
        String url = dto.getAffiliateUrl();
        if (url == null || url.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Produto afiliado deve informar o link do parceiro (affiliateUrl).");
        }
    }

    /**
     * Produto afiliado não possui preço: ignorar valor recebido e persistir null.
     */
    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        if (dto.getType() == ProductType.AFFILIATE) {
            entity.setPrice(null);
        } else {
            entity.setPrice(dto.getPrice());
        }
        entity.setImgUrl(dto.getImgUrl());
        entity.setAffiliateUrl(dto.getAffiliateUrl());
        entity.setType(dto.getType());
        entity.setIsFeatured(dto.getIsFeatured());
        entity.setBrand(dto.getBrand());
        entity.setHomePriority(dto.getHomePriority() == null ? 0 : dto.getHomePriority());

        entity.getCategories().clear();
        for (CategoryDTO catDto : dto.getCategories()) {
            
            Category category = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(category);
        }
    }
}