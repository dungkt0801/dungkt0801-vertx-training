package com.example.starter.repository;

import com.example.starter.model.Class;
import com.example.starter.util.ClassUtil;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@RequiredArgsConstructor
public class ClassRepository {

  private static final String COLLECTION_NAME = "classes";

  private final MongoClient mongoClient;

  public Future<List<Class>> findAll() {
    Future<List<Class>> future = Future.future();
    final JsonObject query = new JsonObject();

    mongoClient.find(COLLECTION_NAME, query, result -> {
      if (result.succeeded()) {
        final List<Class> classes = new ArrayList<>();
        result.result().forEach(clazz -> classes.add(new Class(clazz)));

        future.complete(classes);
      } else {
        future.fail(result.cause());
      }
    });

    return future;
  }

  public Future<Class> findById(String id) {
    Future<Class> future = Future.future();
    JsonObject query = new JsonObject().put("_id", new JsonObject().put("$oid", id));

    mongoClient.findOne(COLLECTION_NAME, query, null, res -> {
      if(res.result() != null) {
        future.complete(new Class(res.result()));
      } else {
        future.fail(new NoSuchElementException("No class was found with the id " + id));
      }
    });

    return future;
  }

  public Future<Class> insertOne(Class clazz) {
    Future<Class> future = Future.future();

    JsonObject classJson = ClassUtil.jsonObjectFromClass(clazz)
      .put("_id", new JsonObject().put("$oid", new ObjectId().toString()));

    mongoClient.insert(COLLECTION_NAME, classJson , result -> {
      if(result.succeeded()) {
        future.complete(new Class(classJson));
      } else {
        future.fail(result.cause());
      }
    });

    return future;
  }

  public Future<String> updateOne(String id, Class clazz) {
    Future<String> future = Future.future();
    final JsonObject query = new JsonObject()
      .put("_id", new JsonObject().put("$oid", id));

    mongoClient.replaceDocuments(COLLECTION_NAME, query, ClassUtil.jsonObjectFromClass(clazz), result -> {
      if(result.result().getDocModified() == 1) {
        future.complete("Update successfully");
      } else {
        future.fail(new NoSuchElementException("No class was found with the id " + id));
      }
    });

    return future;
  }

}
