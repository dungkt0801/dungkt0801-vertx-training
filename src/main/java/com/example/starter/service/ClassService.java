package com.example.starter.service;

import com.example.starter.model.Class;
import com.example.starter.repository.ClassRepository;
import io.vertx.core.Future;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassService {

  private final ClassRepository classRepository;

  public Future<List<Class>> findAll() {
    return classRepository.findAll();
  }

}
