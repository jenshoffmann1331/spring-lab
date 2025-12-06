package com.example.quickstart;

import com.example.quickstart.todo.TodoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

    @Bean
    public TodoService todoService() {
        return new TodoService();
    }
}
