package com.raulmbueno.mini_ecommerce.repositories;

import com.raulmbueno.mini_ecommerce.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT obj FROM Order obj JOIN FETCH obj.items item JOIN FETCH item.product WHERE obj.id = :id")
    Optional<Order> findOrderWithItemsById(Long id);
}