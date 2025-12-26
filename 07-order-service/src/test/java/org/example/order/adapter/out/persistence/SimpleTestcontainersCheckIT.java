package org.example.order.adapter.out.persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;

public class SimpleTestcontainersCheckIT {

  @Test
  void testcontainers_can_start_container() {
    try(GenericContainer<?> alpine = new GenericContainer<>("alpine:3.20")
        .withCommand("sh", "-c", "while true; do sleep 1; done")) {
      alpine.start();
      assertThat(alpine.isRunning()).isTrue();
    }
  }
}
