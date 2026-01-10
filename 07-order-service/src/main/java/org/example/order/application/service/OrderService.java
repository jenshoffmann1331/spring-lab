package org.example.order.application.service;

import jakarta.transaction.Transactional;
import org.example.order.application.port.in.CreateOrderUseCase;
import org.example.order.application.port.out.OrderEventPublisherPort;
import org.example.order.application.port.out.OrderPersistencePort;
import org.example.order.domain.Order;

public class OrderService implements CreateOrderUseCase {

  private final OrderPersistencePort persistencePort;
  private final OrderEventPublisherPort eventPublisherPort;

  public OrderService(
      OrderPersistencePort persistencePort, OrderEventPublisherPort eventPublisherPort) {
    this.persistencePort = persistencePort;
    this.eventPublisherPort = eventPublisherPort;
  }

  @Override
  @Transactional
  public OrderResponse createOrder(CreateOrderCommand cmd) {
    Order order = Order.create(cmd.customerId());
    Order saved = persistencePort.save(order);
    eventPublisherPort.orderCreated(saved);
    return new OrderResponse(saved.getId().toString(), saved.getCustomerId());
  }
}
