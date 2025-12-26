package org.example.order.adapter.in.web;

import org.example.order.application.port.in.CreateOrderUseCase;
import org.example.order.application.port.in.CreateOrderUseCase.CreateOrderCommand;
import org.example.order.application.port.in.CreateOrderUseCase.OrderResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
  private final CreateOrderUseCase createOrderUseCase;

  public OrderController(CreateOrderUseCase createOrderUseCase) {
    this.createOrderUseCase = createOrderUseCase;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse create(@RequestBody CreateOrderRequest request) {
    return createOrderUseCase.createOrder(new CreateOrderCommand(request.customerId));
  }

  public record CreateOrderRequest(String customerId) {}
}
