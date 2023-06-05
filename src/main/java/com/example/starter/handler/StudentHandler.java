package com.example.starter.handler;

import com.example.starter.model.Student;
import com.example.starter.service.StudentService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentHandler {

  private final StudentService studentService;

  public void findAll(RoutingContext routingContext) {
    studentService.findAll()
      .onComplete(ar -> {
        if(ar.succeeded()) {
          onSuccessResponse(routingContext, 200, ar.result());
        } else {
          onErrorResponse(routingContext, 400, ar.cause());
        }
      });
  }

  public void findById(RoutingContext routingContext) {
    final String id = routingContext.pathParam("id");
    studentService.findById(id)
      .onComplete(ar -> {
        if(ar.succeeded()) {
          onSuccessResponse(routingContext, 200, ar.result());
        } else {
          onErrorResponse(routingContext, 400, ar.cause());
        }
      });
  }

  public void insertOne(RoutingContext routingContext) {
    if(routingContext.body() != null) {
      final Student student = routingContext.body().asJsonObject().mapTo(Student.class);
      studentService.insertOne(student)
        .onComplete(ar -> {
          if(ar.succeeded()) {
            onSuccessResponse(routingContext, 200, ar.result());
          } else {
            onErrorResponse(routingContext, 400, ar.cause());
          }
        });
    }
  }

  public void updateOne(RoutingContext routingContext) {
    if(routingContext.body() != null) {
      final  String id = routingContext.pathParam("id");
      final Student student = routingContext.body().asJsonObject().mapTo(Student.class);
      studentService.updateOne(id, student)
        .onComplete(ar -> {
          if(ar.succeeded()) {
            onSuccessResponse(routingContext, 200, ar.result());
          } else {
            onErrorResponse(routingContext, 400, ar.cause());
          }
        });
    }
  }

  public void deleteOne(RoutingContext routingContext) {
    final String id = routingContext.pathParam("id");
    studentService.deleteOne(id)
      .onComplete(ar -> {
        if(ar.succeeded()) {
          onSuccessResponse(routingContext, 200, ar.result());
        } else {
          onErrorResponse(routingContext, 400, ar.cause());
        }
      });
  }

  private void onSuccessResponse(RoutingContext rc, int status, Object object) {
    rc.response()
      .setStatusCode(status)
      .putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(object));
  }

  private void onErrorResponse(RoutingContext rc, int status, Throwable throwable) {
    JsonObject error = new JsonObject().put("error", throwable.getMessage());

    rc.response()
      .setStatusCode(status)
      .putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(error));
  }

}
