package com.raulmbueno.mini_ecommerce.services;

import com.raulmbueno.mini_ecommerce.dtos.OrderDTO;
import com.raulmbueno.mini_ecommerce.entities.Client;
import com.raulmbueno.mini_ecommerce.entities.Order;
import com.raulmbueno.mini_ecommerce.entities.enums.OrderStatus;
import com.raulmbueno.mini_ecommerce.repositories.ClientRepository;
import com.raulmbueno.mini_ecommerce.repositories.OrderRepository;
import com.raulmbueno.mini_ecommerce.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        List<Order> list = orderRepository.findAll();
        return list.stream().map(OrderDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order entity = orderRepository.findOrderWithItemsById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido com ID " + id + " não encontrado"));
        return new OrderDTO(entity);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order entity = new Order();
        entity.setMoment(Instant.now());
        entity.setOrderStatus(OrderStatus.WAITING_PAYMENT);
        Client client = clientRepository.getReferenceById(dto.getClient().getId());
        entity.setClient(client);
        entity = orderRepository.save(entity);
        return new OrderDTO(entity);
    }
}