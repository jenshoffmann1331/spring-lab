package com.example.consumingrest;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class QuoteClient {

  private final RestClient restClient;

  public QuoteClient(RestClient restClient) {
    this.restClient = restClient;
  }

  public Quote fetchRandomQuote() {
    return restClient.get().uri("/api/random").retrieve().body(Quote.class);
  }
}
