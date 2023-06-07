package com.example.starter.service;

import com.example.starter.dto.StudentDto;
import com.example.starter.model.Student;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import java.util.List;

public interface StudentService {

  Future<List<StudentDto>> findAll(JsonObject query);

  Future<StudentDto> findById(String id);

  Future<StudentDto> insertOne(Student student);

  Future<String> updateOne(Student student);

  Future<String> deleteOne(String id);

}
