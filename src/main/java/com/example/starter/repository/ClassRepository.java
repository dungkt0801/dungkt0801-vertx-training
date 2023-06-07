package com.example.starter.repository;

import com.example.starter.model.Class;
import io.vertx.core.Future;
import java.util.List;

public interface ClassRepository {

  Future<List<Class>> findAll();

  Future<Class> findById(String id);

  Future<Class> insertOne(Class clazz);

  Future<String> updateOne(String id, Class clazz);

}
