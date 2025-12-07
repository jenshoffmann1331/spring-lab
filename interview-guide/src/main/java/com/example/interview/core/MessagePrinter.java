package com.example.interview.core;

import org.springframework.stereotype.Component;

@Component
public class MessagePrinter {

  private final MessageService messageService;

  public MessagePrinter(MessageService messageService) {
    this.messageService = messageService;
  }

  public void print() {
    System.out.println(messageService.getMessage());
  }
}
