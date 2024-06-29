package com.diogoazevedo.btgpactual.orderms.repository;

import com.diogoazevedo.btgpactual.orderms.entity.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<OrderEntity, Long> {}
