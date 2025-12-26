package org.example.order.application.port.out;

import org.example.order.domain.Order;

public interface OrderEventPublisherPort {
  void orderCreated(Order order);
}
