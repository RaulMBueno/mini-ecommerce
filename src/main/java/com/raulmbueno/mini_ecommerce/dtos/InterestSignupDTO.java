package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.InterestSignup;
import com.raulmbueno.mini_ecommerce.entities.enums.InterestType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InterestSignupDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String email;
    private String whatsapp;
    private InterestType interestType;
    private String interestReference;
    private String message;
    private Instant createdAt;

    public InterestSignupDTO(InterestSignup entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.whatsapp = entity.getWhatsapp();
        this.interestType = entity.getInterestType();
        this.interestReference = entity.getInterestReference();
        this.message = entity.getMessage();
        this.createdAt = entity.getCreatedAt();
    }
}
