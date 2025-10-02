package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String authority;

    public RoleDTO(Role entity) {
        this.id = entity.getId();
        this.authority = entity.getAuthority();
    }
}