package com.raulmbueno.mini_ecommerce.repositories;

import com.raulmbueno.mini_ecommerce.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}