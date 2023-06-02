package com.example.starter.repository;

import com.example.starter.model.Student;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentRepository {

  private static final String COLLECTION_NAME = "students";

  private final MongoClient client;

  public Future<List<Student>> getAll() {
    Promise<List<Student>> promise = Promise.promise();
    final JsonObject query = new JsonObject();

    client.find(COLLECTION_NAME, query, result -> {
      if(result.succeeded()) {
        final List<Student> students = new ArrayList<>();
        result.result().forEach(student -> students.add(new Student(student)));
        promise.complete(students);
      } else {
        promise.fail(result.cause());
      }
    });

    return promise.future();
  }

  public Future<Student> getById(String id) {
    Promise<Student> promise = Promise.promise();
    final JsonObject query = new JsonObject().put("_id", new JsonObject().put("$oid", id));

    return client.findOne(COLLECTION_NAME, query, null)
      .flatMap(result -> {
        final Student student = new Student(result);
        promise.complete(student);
        return promise.future();
      });
  }

  public Future<Student> insert(Student student) {
    Promise<Student> promise = Promise.promise();
    return client.insert(COLLECTION_NAME, student)
      .flatMap(result -> {
        final JsonObject jsonObject = new JsonObject().put("_id", result);
      });
  }

}
