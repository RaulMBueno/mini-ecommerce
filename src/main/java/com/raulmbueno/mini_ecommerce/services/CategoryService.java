package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.entities.Category;
import com.raulmbueno.mini_ecommerce.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<Category> findAll() {
        List<Category> result = repository.findAll();
        return result;
    }
}