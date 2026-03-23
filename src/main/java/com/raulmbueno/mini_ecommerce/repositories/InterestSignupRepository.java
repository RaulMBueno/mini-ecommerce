package com.raulmbueno.mini_ecommerce.repositories;

import com.raulmbueno.mini_ecommerce.entities.InterestSignup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestSignupRepository extends JpaRepository<InterestSignup, Long> {

    List<InterestSignup> findAllByOrderByCreatedAtDesc();
}
