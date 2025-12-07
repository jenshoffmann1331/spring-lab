package com.example.interview;

import com.example.interview.core.MessagePrinter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class InterviewGuideApplication {

  public static void main(String[] args) {
    ApplicationContext ctx = SpringApplication.run(InterviewGuideApplication.class, args);

    MessagePrinter printer = ctx.getBean(MessagePrinter.class);
    printer.print();
  }
}
