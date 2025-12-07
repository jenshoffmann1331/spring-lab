package com.example.consumingrest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuoteClientUnitTest {

  @Mock(answer = Answers.RETURNS_DEEP_STUBS)
  RestClient restClient;

  @Test
  void fetchRandomQuote_returnsResponseFromRestClient() {
    // given
    String baseUrl = "http://localhost:8000";
    Quote expected = new Quote("success", new Value(42L, "Hello"));
    when(restClient.get().uri("/api/random").retrieve().body(Quote.class)).thenReturn(expected);

    // testee
    QuoteClient client = new QuoteClient(restClient);

    // when
    Quote result = client.fetchRandomQuote();

    // then
    assertThat(result).isEqualTo(expected);
  }
}
