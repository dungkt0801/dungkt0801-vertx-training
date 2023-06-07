package com.example.starter.service;

import com.example.starter.model.Class;
import io.vertx.core.Future;
import java.util.List;

public interface ClassService {

  Future<List<Class>> findAll();

  Future<Class> findById(String id);

  Future<Class> insertOne(Class clazz);

  Future<String> updateOne(String id, Class clazz);

  Future<List<String>> findClassIdsByName(String name);

}
