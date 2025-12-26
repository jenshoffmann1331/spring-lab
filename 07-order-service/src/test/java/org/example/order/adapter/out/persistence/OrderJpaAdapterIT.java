package org.example.order.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.example.order.application.port.out.OrderPersistencePort;
import org.example.order.domain.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Testcontainers
@Import(OrderJpaAdapter.class)
class OrderJpaAdapterIT {

  @Container
  static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
      .withDatabaseName("testdb")
      .withUsername("test")
      .withPassword("test");

  @DynamicPropertySource
  static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
  }

  @Autowired
  private OrderPersistencePort persistencePort;

  @Autowired
  private OrderJpaRepository repository;

  @Test
  void save_persists_order_and_returns_domain_object() {
    // given
    Order order = Order.create("customer-123");

    // when
    Order saved =  persistencePort.save(order);

    // then
    assertThat(saved.getId()).isEqualTo(order.getId());
    assertThat(saved.getCustomerId()).isEqualTo("customer-123");
    assertThat(saved.getCreatedAt()).isNotNull();

    assertThat(repository.findById(saved.getId()))
        .isPresent()
        .get()
        .extracting(OrderEntity::getCustomerId)
        .isEqualTo("customer-123");
  }
}
