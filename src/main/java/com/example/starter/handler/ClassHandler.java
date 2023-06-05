package com.example.starter.handler;

import com.example.starter.service.ClassService;
import com.example.starter.util.Util;
import io.vertx.ext.web.RoutingContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassHandler {

  private final ClassService classService;

  public void findAll(RoutingContext rc) {
    classService.findAll().setHandler(ar -> {
        if (ar.succeeded()) {
          Util.onSuccessResponse(rc, 200, ar.result());
        } else {
          Util.onErrorResponse(rc, 400, ar.cause());
        }
    });
  }

}
