package org.example.order.domain;

import java.time.Instant;
import java.util.UUID;

public class Order {
  private final UUID id;
  private final String customerId;
  private final Instant createdAt;

  public Order(UUID id, String customerId, Instant createdAt) {
    this.id = id;
    this.customerId = customerId;
    this.createdAt = createdAt;
  }

  public static Order create(String customerId) {
    return new Order(UUID.randomUUID(), customerId, Instant.now());
  }

  public UUID getId() {
    return id;
  }

  public String getCustomerId() {
    return customerId;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
