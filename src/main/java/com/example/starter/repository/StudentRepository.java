package com.example.starter.repository;

import com.example.starter.model.Student;
import com.example.starter.util.StudentUtil;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@RequiredArgsConstructor
public class StudentRepository {

  private static final String COLLECTION_NAME = "students";

  private final MongoClient mongoClient;

  public Future<List<Student>> findAll(JsonObject query) {
    Future<List<Student>> future = Future.future();

    mongoClient.find("students", query, res -> {
      if (res.succeeded()) {
        future.complete(
          res.result().stream()
            .map(Student::new)
            .collect(Collectors.toList())
        );
      } else {
        future.fail(res.cause());
      }
    });

    return future;
  }

  public Future<Student> findById(String id) {
    Future<Student> future = Future.future();
    final JsonObject query = new JsonObject().put("_id", new JsonObject().put("$oid", id));

    mongoClient.findOne(COLLECTION_NAME, query, null, result -> {
      if(result.result() != null) {
        future.complete(new Student(result.result()));
      } else {
        future.fail(new NoSuchElementException("No student with id " + id));
      }
    });

    return future;
  }

  public Future<Student> insert(Student student) {
    Future<Student> future = Future.future();

    JsonObject studentJson = StudentUtil.jsonObjectFromStudent(student);
    String id = new ObjectId().toString();
    studentJson.put("_id", new JsonObject().put("$oid", id));
    student.setId(id);

    mongoClient.insert(COLLECTION_NAME, studentJson , result -> {
      if(result.succeeded()) {
        future.complete(student);
      } else {
        future.fail(result.cause());
      }
    });

    return future;
  }

  public Future<String> update(String id, Student student) {
    Future<String> future = Future.future();
    final JsonObject query = new JsonObject()
      .put("_id", new JsonObject().put("$oid", id));

    mongoClient.replaceDocuments(COLLECTION_NAME, query, StudentUtil.jsonObjectFromStudent(student), result -> {
      if(result.result() != null && result.result().getDocModified() == 1) {
        future.complete("Update successfully");
      } else {
        future.fail(new NoSuchElementException("No student with id " + id));
      }
    });

    return future;
  }

  public Future<String> delete(String id) {
    Future<String> future = Future.future();
    final JsonObject query = new JsonObject().put("_id", new JsonObject().put("$oid", id));

    mongoClient.removeDocument(COLLECTION_NAME, query, result -> {
      if(result.result().getRemovedCount() == 1) {
        future.complete("Delete successfully");
      } else {
        future.fail(new NoSuchElementException("No student with id " + id));
      }
    });

    return future;
  }

}
