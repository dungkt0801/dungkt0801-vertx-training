package com.example.starter.repository;

import com.example.starter.model.Class;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassRepository {

  private static final String COLLECTION_NAME = "classes";

  private final MongoClient mongoClient;

  public Future<List<Class>> findAll() {
    Promise<List<Class>> promise = Promise.promise();
    final JsonObject query = new JsonObject();

    mongoClient.find(COLLECTION_NAME, query, result -> {
      if(result.succeeded()) {
        final List<Class> classes = new ArrayList<>();
        result.result().forEach(clazz -> classes.add(new Class(clazz)));

        promise.complete(classes);
      } else {
        promise.fail(result.cause());
      }
    });

    return promise.future();
  }

}
