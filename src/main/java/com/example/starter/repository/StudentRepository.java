package com.example.starter.repository;

import com.example.starter.model.Student;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.util.List;

public interface StudentRepository {

  Future<List<Student>> findAll(JsonObject query);

  Future<Student> findById(String id);

  Future<Student> insert(Student student);

  Future<String> update(String id, Student student);

  Future<String> delete(String id);

}
