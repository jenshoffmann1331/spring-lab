package org.example.jdbc.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.example.jdbc.model.Student;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
    System.out.println(rows + " affected");
  }


  public List<Student> findAll() {
    String sql = "select * from student";
    RowMapper<Student> mapper = (rs, _) -> {
      Student s = new Student();
      s.setRollNo(rs.getInt("rollno"));
      s.setName(rs.getString("name"));
      s.setMarks(rs.getInt("marks"));
      return s;
    };

    return jdbcTemplate.query(sql, mapper);
  }
}
