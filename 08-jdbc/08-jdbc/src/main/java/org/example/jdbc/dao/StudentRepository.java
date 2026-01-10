package org.example.jdbc.dao;

import java.util.ArrayList;
import java.util.List;
import org.example.jdbc.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepository {

  private JdbcTemplate jdbcTemplate;

  public StudentRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void save(Student s) {
    String sql = "insert into student (rollno, name, marks) values (?, ?, ?)";
    int rows = jdbcTemplate.update(sql, s.getRollNo(), s.getName(), s.getMarks());
    System.out.println(rows + " effected");
  }

  public List<Student> findAll() {
    List<Student> students = new ArrayList<>();
    return students;
  }
}
