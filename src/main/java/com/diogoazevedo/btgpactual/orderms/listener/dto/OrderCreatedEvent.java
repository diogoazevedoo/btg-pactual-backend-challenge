package com.diogoazevedo.btgpactual.orderms.listener.dto;

import java.util.List;

public record OrderCreatedEvent(
   Long orderCode,
   Long customerCode,
   List<OrderItemEvent> items
) {}
