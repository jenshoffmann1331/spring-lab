package com.example.consumingrest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
@RestClientTest(QuoteClient.class)
class QuoteClientRestTest {

    @Autowired
    private QuoteClient quoteClient;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void fetchRandomQuote_callsApi() {
        String json = """
            {
              "type": "success",
              "value": {
                "id": 42,
                "quote": "Really loving Spring Boot, makes stand alone Spring apps easy."
              }
            }
            """;

        server.expect(requestTo("http://localhost:8080/api/random"))
            .andRespond(withSuccess(json, MediaType.APPLICATION_JSON));

        Quote quote = quoteClient.fetchRandomQuote();

        assertThat(quote.type()).isEqualTo("success");
        assertThat(quote.value()).isNotNull();
        assertThat(quote.value().id()).isEqualTo(42);
        assertThat(quote.value().quote()).startsWith("Really loving Spring Boot,");

        server.verify();
    }

    @TestConfiguration
    static class TestRestClientConfig {
        @Bean
        RestClient quoteRestClient(RestClient.Builder builder) {
            return builder.baseUrl("http://localhost:8080").build();
        }
    }
}
