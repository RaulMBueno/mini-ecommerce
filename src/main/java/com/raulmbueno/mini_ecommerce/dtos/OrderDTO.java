package com.raulmbueno.mini_ecommerce.dtos;

import com.raulmbueno.mini_ecommerce.entities.Order;
import java.io.Serializable;
import java.time.Instant;

public class OrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Instant moment;
    private Integer orderStatus;
    private ClientDTO client;

    public OrderDTO() {
    }

    public OrderDTO(Long id, Instant moment, Integer orderStatus, ClientDTO client) {
        this.id = id;
        this.moment = moment;
        this.orderStatus = orderStatus;
        this.client = client;
    }

    public OrderDTO(Order entity) {
        this.id = entity.getId();
        this.moment = entity.getMoment();
        this.orderStatus = entity.getOrderStatus();
        this.client = new ClientDTO(entity.getClient());
    }

    public Long getId() {
        return id;
    }

    public Instant getMoment() {
        return moment;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public ClientDTO getClient() {
        return client;
    }
}