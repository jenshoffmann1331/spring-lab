package org.example.order.adapter.out.messaging;

import java.time.Instant;

public record OrderCreatedEventPayload(
    String orderId,
    String customerId,
    Instant createdAt
) {}
