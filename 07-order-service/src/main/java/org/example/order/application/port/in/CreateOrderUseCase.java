package org.example.order.application.port.in;

public interface CreateOrderUseCase {
  OrderResponse createOrder(CreateOrderCommand cmd);

  record CreateOrderCommand(String customerId) {}
  record OrderResponse(String id, String customerId) {}
}
