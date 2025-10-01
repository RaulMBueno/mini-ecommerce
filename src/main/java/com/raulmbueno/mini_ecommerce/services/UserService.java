package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.UserDTO;
import com.raulmbueno.mini_ecommerce.dtos.UserInsertDTO;
import com.raulmbueno.mini_ecommerce.entities.User;
import com.raulmbueno.mini_ecommerce.repositories.UserRepository;
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    // Implementação do método obrigatório pelo Spring Security
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if ("admin@ecommerce.com".equals(email)) {
             return repository.findById(1L).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
        } else if ("client@ecommerce.com".equals(email)) {
             return repository.findById(2L).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
        }
        
        throw new UsernameNotFoundException("Usuário não encontrado: " + email);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<User> list = repository.findAll();
        return list.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = repository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. ID " + id));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        entity = repository.save(entity);

        return new UserDTO(entity);
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
    }
}