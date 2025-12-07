package com.example.interview.core;

import org.springframework.stereotype.Component;

@Component
public class HelloMessageService implements MessageService {
  @Override
  public String getMessage() {
    return "Hello from loosely coupled service!";
  }
}
