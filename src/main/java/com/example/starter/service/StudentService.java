package com.example.starter.service;

import com.example.starter.model.Student;
import com.example.starter.repository.StudentRepository;

import io.vertx.core.Future;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentService {

  private final StudentRepository studentRepository;

  public Future<List<Student>> getAll() {
    return studentRepository.getAll();
  }

  public Future<Student> getById(String id) {
    return studentRepository.getById(id);
  }

}
