package com.example.starter.service;

import com.example.starter.model.Student;
import com.example.starter.repository.StudentRepository;

import io.vertx.core.Future;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentService {

  private final StudentRepository studentRepository;

  public Future<List<Student>> findAll() {
    return studentRepository.findAll();
  }

  public Future<Student> findById(String id) {
    return studentRepository.findById(id);
  }

  public Future<Student> insertOne(Student student) {
    return studentRepository.insert(student);
  }

  public Future<String> updateOne(String id, Student student) {
    return studentRepository.update(id, student);
  }

  public Future<String> deleteOne(String id) {
    return studentRepository.delete(id);
  }

}
