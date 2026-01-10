package org.example.order.adapter.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.order.adapter.out.messaging.KafkaOrderEventPublisher;
import org.example.order.application.port.out.OrderEventPublisherPort;
import org.example.order.application.port.out.OrderPersistencePort;
import org.example.order.application.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableKafka
public class OrderConfig {

  private static final Logger log = LoggerFactory.getLogger(OrderConfig.class);

  @Bean
  public OrderService orderService(OrderPersistencePort persistencePort,
      OrderEventPublisherPort eventPublisherPort) {
    return new OrderService(persistencePort, eventPublisherPort);
  }

  @Bean
  public ProducerFactory<String, String> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public NewTopic ordersCreatedTopic() {
    return TopicBuilder.name("orders.created").build();
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> producerFactory) {
    return new KafkaTemplate<>(producerFactory);
  }

  @Bean
  public OrderEventPublisherPort orderEventPublisherPort(
      KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper,
      @Value("${app.order.kafka.topic}") String topic
  ) {
    return new KafkaOrderEventPublisher(kafkaTemplate, objectMapper, topic);
  }
}
