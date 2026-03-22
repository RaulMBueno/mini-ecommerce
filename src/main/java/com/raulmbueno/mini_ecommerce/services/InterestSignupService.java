package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.InterestSignupDTO;
import com.raulmbueno.mini_ecommerce.dtos.InterestSignupInsertDTO;
import com.raulmbueno.mini_ecommerce.entities.InterestSignup;
import com.raulmbueno.mini_ecommerce.repositories.InterestSignupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@SuppressWarnings("null")
public class InterestSignupService {

    private final InterestSignupRepository repository;

    public InterestSignupService(InterestSignupRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public InterestSignupDTO insert(InterestSignupInsertDTO dto) {
        InterestSignup entity = new InterestSignup();
        copyInsertDtoToEntity(dto, entity);
        entity = repository.save(entity);
        return new InterestSignupDTO(entity);
    }

    private void copyInsertDtoToEntity(InterestSignupInsertDTO dto, InterestSignup entity) {
        entity.setName(dto.getName().trim());
        entity.setEmail(dto.getEmail().trim().toLowerCase());
        entity.setWhatsapp(dto.getWhatsapp() != null ? dto.getWhatsapp().trim() : null);
        entity.setInterestType(dto.getInterestType());
        entity.setInterestReference(dto.getInterestReference() != null ? dto.getInterestReference().trim() : null);
        entity.setMessage(dto.getMessage() != null ? dto.getMessage().trim() : null);
    }
}
