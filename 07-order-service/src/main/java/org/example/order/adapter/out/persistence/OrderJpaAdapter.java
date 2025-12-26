package org.example.order.adapter.out.persistence;

import org.example.order.application.port.out.OrderPersistencePort;
import org.example.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderJpaAdapter implements OrderPersistencePort {

  private final OrderJpaRepository repository;

  public OrderJpaAdapter(OrderJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Order save(Order order) {
    // Domain -> Entity
    OrderEntity entity =
        new OrderEntity(order.getId(), order.getCustomerId(), order.getCreatedAt());

    OrderEntity persisted = repository.save(entity);

    // Entity -> Domain
    return new Order(persisted.getId(), persisted.getCustomerId(), persisted.getCreatedAt());
  }
}
