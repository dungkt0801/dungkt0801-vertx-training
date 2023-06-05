package com.example.starter.handler;

import com.example.starter.service.ClassService;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassHandler {

  private final ClassService classService;

  public void findAll(RoutingContext rc) {
    classService.findAll()
      .onComplete(ar -> {
        if(ar.succeeded()) {
          onSuccessResponse(rc, 200, ar.result());
        } else {
          onErrorResponse(rc, 400, ar.cause());
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
