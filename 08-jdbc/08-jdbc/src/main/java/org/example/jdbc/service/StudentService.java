package org.example.jdbc.service;

import java.util.List;
import org.example.jdbc.dao.StudentRepository;
import org.example.jdbc.model.Student;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

  private final StudentRepository studentRepository;

  public StudentService(StudentRepository studentRepository) {
    this.studentRepository = studentRepository;
  }

  public void addStudent(Student s) {
    studentRepository.save(s);
  }

  public List<Student> getStudents() {
    return studentRepository.findAll();
  }
}
