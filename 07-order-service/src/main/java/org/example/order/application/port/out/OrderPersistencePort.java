package org.example.order.application.port.out;

import org.example.order.domain.Order;

public interface OrderPersistencePort {
  Order save(Order order);
}
