package org.example.order.application.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.order.application.port.in.CreateOrderUseCase.CreateOrderCommand;
import org.example.order.application.port.out.OrderEventPublisherPort;
import org.example.order.application.port.out.OrderPersistencePort;
import org.example.order.domain.Order;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class OrderServiceTest {
  @Test
  void createOrder_persists_and_publishes_event() {
    // given
    OrderPersistencePort persistencePort = mock(OrderPersistencePort.class);
    OrderEventPublisherPort eventPublisherPort = mock(OrderEventPublisherPort.class);
    OrderService service = new OrderService(persistencePort, eventPublisherPort);
    CreateOrderCommand cmd = new CreateOrderCommand("customer-123");
    ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
    when(persistencePort.save(orderCaptor.capture()))
        .thenAnswer(inv -> inv.getArgument(0));

    // when
    var response = service.createOrder(cmd);

    // then
    Order createdOrder = orderCaptor.getValue();
    assertThat(createdOrder.getCustomerId()).isEqualTo("customer-123");
    assertThat(response.customerId()).isEqualTo("customer-123");
    assertThat(response.id()).isEqualTo(createdOrder.getId().toString());

    verify(eventPublisherPort).orderCreated(createdOrder);
  }
}
