package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.ClientDTO;
import com.raulmbueno.mini_ecommerce.entities.Client;
import com.raulmbueno.mini_ecommerce.repositories.ClientRepository;
import com.raulmbueno.mini_ecommerce.services.exceptions.DatabaseException;
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        List<Client> list = repository.findAll();
        return list.stream().map(ClientDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Client entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente com ID " + id + " não encontrado"));
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO insert(ClientDTO dto) {
        Client entity = new Client();
        copyDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new ClientDTO(entity);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO dto) {
        try {
            Client entity = repository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = repository.save(entity);
            return new ClientDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Cliente com ID " + id + " não encontrado");
        }
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente com ID " + id + " não encontrado");
        }
        try {
            repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Violação de integridade: este cliente pode ter pedidos associados.");
        }
    }

    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
    }
}