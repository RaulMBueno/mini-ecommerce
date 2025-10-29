package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.UserDTO;
import com.raulmbueno.mini_ecommerce.dtos.UserInsertDTO;
import com.raulmbueno.mini_ecommerce.entities.Role;
import com.raulmbueno.mini_ecommerce.entities.User;
import com.raulmbueno.mini_ecommerce.repositories.RoleRepository;
import com.raulmbueno.mini_ecommerce.repositories.UserRepository;
import com.raulmbueno.mini_ecommerce.services.exceptions.DatabaseException;
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));
    }

    @Transactional(readOnly = true)
    public List<UserDTO> findAll() {
        List<User> list = userRepository.findAll();
        return list.stream().map(UserDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário com ID " + id + " não encontrado"));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);

        entity.getRoles().clear();
        Role clientRole = roleRepository.findById(2L)
                .orElseThrow(() -> new ResourceNotFoundException("Role de cliente não encontrada"));
        entity.getRoles().add(clientRole);

        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        try {
            User entity = userRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = userRepository.save(entity);
            return new UserDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Usuário com ID " + id + " não encontrado");
        }
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário com ID " + id + " não encontrado");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade: este usuário pode ter pedidos associados.");
        }
    }
}