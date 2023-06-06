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

  public Future<Class> findById(String id) {
    return classRepository.findById(id);
  }

  public Future<Class> insertOne(Class clazz) {
    return classRepository.insertOne(clazz);
  }

  public Future<String> updateOne(String id, Class clazz) {
    return classRepository.updateOne(id, clazz);
  }

}
