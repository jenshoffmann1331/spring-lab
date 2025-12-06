package com.example.schedulingtask;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


import java.time.Duration;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@SpringBootTest
class ScheduledTaskTest {

    @MockitoSpyBean
    ScheduledTask task;

    @Test
    public void reportCurrentTime() {
        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            verify(task, atLeast(2)).reportCurrentTime();
        });
    }
}
