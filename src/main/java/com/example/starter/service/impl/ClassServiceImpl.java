package com.example.starter.service.impl;

import com.example.starter.model.Class;
import com.example.starter.repository.ClassRepository;
import com.example.starter.service.ClassService;
import io.vertx.core.Future;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassServiceImpl implements ClassService {

  private final ClassRepository classRepository;

  @Override
  public Future<List<Class>> findAll() {
    return classRepository.findAll();
  }

  @Override
  public Future<Class> findById(String id) {
    return classRepository.findById(id);
  }

  @Override
  public Future<Class> insertOne(Class clazz) {
    return classRepository.insertOne(clazz);
  }

  @Override
  public Future<String> updateOne(String id, Class clazz) {
    return classRepository.updateOne(id, clazz);
  }

}
