package com.diogoazevedo.btgpactual.orderms.service;

import com.diogoazevedo.btgpactual.orderms.controller.dto.OrderResponse;
import com.diogoazevedo.btgpactual.orderms.entity.OrderEntity;
import com.diogoazevedo.btgpactual.orderms.entity.OrderItem;
import com.diogoazevedo.btgpactual.orderms.listener.dto.OrderCreatedEvent;
import com.diogoazevedo.btgpactual.orderms.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void save(OrderCreatedEvent event) {
        var entity = new OrderEntity();
        entity.setOrderId(event.orderCode());
        entity.setCustomerId(event.customerCode());
        entity.setTotal(getTotal(event));
        entity.setItems(getOrderItems(event));
        orderRepository.save(entity);
    }

    public Page<OrderResponse> findAllByCustomerId(Long customerId, PageRequest pageRequest) {
        var orders = orderRepository.findAllByCustomerId(customerId, pageRequest);
        return orders.map(OrderResponse::fromEntity);
    }

    private BigDecimal getTotal(OrderCreatedEvent event) {
        return event.items().stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private static List<OrderItem> getOrderItems(OrderCreatedEvent event) {
        return event.items().stream()
                .map(item -> new OrderItem(
                        item.product(),
                        item.quantity(),
                        item.price())
                )
                .toList();
    }
}
