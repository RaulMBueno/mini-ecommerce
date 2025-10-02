package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.Order;
import com.raulmbueno.mini_ecommerce.entities.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Instant moment;
    private OrderStatus orderStatus; // Corrigido de Integer para OrderStatus
    private ClientDTO client;

    public OrderDTO(Order entity) {
        this.id = entity.getId();
        this.moment = entity.getMoment();
        this.orderStatus = entity.getOrderStatus(); // Agora os tipos batem perfeitamente
        this.client = new ClientDTO(entity.getClient());
    }
}