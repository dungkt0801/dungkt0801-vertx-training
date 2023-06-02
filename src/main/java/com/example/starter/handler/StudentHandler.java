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

  public void getAll(RoutingContext routingContext) {
    studentService.getAll()
      .onComplete(ar -> {
        if(ar.succeeded()) {
          onSuccessResponse(routingContext, 200, ar.result());
        } else {
          onErrorResponse(routingContext, 400, ar.cause());
        }
      });
  }

  public void getById(RoutingContext routingContext) {
    String id = routingContext.pathParam("id");
    studentService.getById(id)
      .onComplete(ar -> {
        if(ar.succeeded()) {
          onSuccessResponse(routingContext, 200, ar.result());
        } else {
          onErrorResponse(routingContext, 400, ar.cause());
        }
      });
  }

  public void insert(RoutingContext routingContext) {
    if(routingContext.body() != null) {
      Student student = routingContext.body().asJsonObject().mapTo(Student.class);
      studentService.insert(student)
        .onComplete(ar -> {
          if(ar.succeeded()) {
            onSuccessResponse(routingContext, 200, ar.result());
          } else {
            onErrorResponse(routingContext, 400, ar.cause());
          }
        });
    }
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
