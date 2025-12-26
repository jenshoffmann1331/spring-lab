package org.example.order.adapter.config;

import org.example.order.application.port.out.OrderEventPublisherPort;
import org.example.order.application.port.out.OrderPersistencePort;
import org.example.order.application.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfig {

  private static final Logger log = LoggerFactory.getLogger(OrderConfig.class);

  @Bean
  public OrderService orderService(OrderPersistencePort persistencePort,
      OrderEventPublisherPort eventPublisherPort) {
    return new OrderService(persistencePort, eventPublisherPort);
  }

  @Bean
  public OrderEventPublisherPort orderEventPublisherPort() {
    return order -> log.info("📢 Dummy OrderEventPublisherPort: Event received -> {}", order);
  }
}
