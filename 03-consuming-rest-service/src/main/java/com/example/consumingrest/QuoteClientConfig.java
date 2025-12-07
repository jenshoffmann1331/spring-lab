package com.example.consumingrest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class QuoteClientConfig {

  @Bean
  RestClient quoteRestClient(
      RestClient.Builder builder, @Value("${quote.base-url}") String baseUrl) {
    return builder.baseUrl(baseUrl).build();
  }
}
