package org.example.jdbc;

import java.util.List;
import org.example.jdbc.model.Student;
import org.example.jdbc.service.StudentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

  public static void main(String[] args) {

    ApplicationContext context = SpringApplication.run(Application.class, args);

    Student s = new Student();
    s.setRollNo(1);
    s.setName("Narvin");
    s.setMarks(78);

    StudentService service = context.getBean(StudentService.class);
    service.addStudent(s);

    List<Student> students = service.getStudents();
    System.out.println(students);
  }
}
