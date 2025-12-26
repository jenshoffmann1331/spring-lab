package org.example.order.adapter.in.web;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.example.order.application.port.out.OrderEventPublisherPort;
import org.example.order.application.port.out.OrderPersistencePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIT {

  @Autowired
  MockMvc mockMvc;

  @TestConfiguration
  static class TestAdaptersConfig {
    @Bean
    OrderPersistencePort persistencePort() {
      return order -> order;
    }

    @Bean
    OrderEventPublisherPort eventPublisherPort() {
      return mock(OrderEventPublisherPort.class);
    }
  }

  @Test
  void createOrder_returns_201_and_body() throws Exception {
    mockMvc.perform(
        post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
              {"customerId":"customer-123"}
            """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.customerId").value("customer-123"))
        .andExpect(jsonPath("$.id").exists());
  }
}
