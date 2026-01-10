package org.example.order.adapter.out.messaging;

import org.example.order.application.port.out.OrderEventPublisherPort;
import org.example.order.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

public class KafkaOrderEventPublisher implements OrderEventPublisherPort {

  private static final Logger log = LoggerFactory.getLogger(KafkaOrderEventPublisher.class);

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ObjectMapper objectMapper;
  private final String topic;

  public KafkaOrderEventPublisher(
      KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper,
      String topic
  ) {
    this.kafkaTemplate = kafkaTemplate;
    this.objectMapper = objectMapper;
    this.topic = topic;
  }

  @Override
  public void orderCreated(Order order) {
    var payload = new OrderCreatedEventPayload(
        order.getId().toString(),
        order.getCustomerId(),
        order.getCreatedAt()
    );

    String json;
    try {
      json = objectMapper.writeValueAsString(payload);
    } catch (JacksonException e) {
      log.error("Failed to serialize OrderCreatedEventPayload for order {}", order.getId(), e);
      throw new IllegalStateException("Could not serialize order event", e);
    }

    log.info("Publishing OrderCreatedEvent for order {} to topic {}", order.getId(), topic);

    kafkaTemplate.send(topic, payload.orderId(), json)
        .whenComplete((result, ex) -> {
          if (ex != null) {
            log.error("Error while sending OrderCreateEvent for order {}", order.getId());
          } else if (result != null) {
            log.info(
                "OrderCreateEvent for order {} written to partition {}, offset {}",
                order.getId(),
                result.getRecordMetadata().partition(),
                result.getRecordMetadata().offset()
            );
          }
        });
  }
}
