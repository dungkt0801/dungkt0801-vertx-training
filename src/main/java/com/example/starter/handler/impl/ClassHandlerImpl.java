package com.example.starter.handler.impl;

import com.example.starter.handler.ClassHandler;
import com.example.starter.model.Class;
import com.example.starter.service.ClassService;
import com.example.starter.util.ClassUtil;
import com.example.starter.util.Util;
import io.vertx.ext.web.RoutingContext;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClassHandlerImpl implements ClassHandler {

  private final ClassService classService;

  @Override
  public void findAll(RoutingContext rc) {
    classService.findAll().setHandler(ar -> {
        if (ar.succeeded()) {
          Util.onSuccessResponse(rc, 200, ar.result());
        } else {
          Util.onErrorResponse(rc, 400, ar.cause());
        }
    });
  }

  @Override
  public void findById(RoutingContext rc) {
    final String id = rc.pathParam("id");
    if(Util.isValidObjectId(id)) {
      classService.findById(id)
        .setHandler(ar -> {
          if (ar.succeeded()) {
            Util.onSuccessResponse(rc, 200, ar.result());
          } else {
            Util.onErrorResponse(rc, 400, ar.cause());
          }
        });
    } else {
      Util.onErrorResponse(rc, 400, new NoSuchElementException("Invalid class id"));
    }
  }

  @Override
  public void insertOne(RoutingContext rc) {
    if(rc.getBodyAsJson() != null) {
      final Class clazz = ClassUtil.classFromJsonObject(rc.getBodyAsJson());
      classService.insertOne(clazz)
        .setHandler(ar -> {
          if(ar.succeeded()) {
            Util.onSuccessResponse(rc, 200, ar.result());
          } else {
            Util.onErrorResponse(rc, 400, ar.cause());
          }
        });
    } else {
      Util.onErrorResponse(rc, 400, new IllegalArgumentException("Request body is empty"));
    }
  }

  @Override
  public void updateOne(RoutingContext rc) {
    if(rc.getBodyAsJson() != null) {
      final String id = rc.pathParam("id");
      final Class clazz = ClassUtil.classFromJsonObject(rc.getBodyAsJson());
      classService.updateOne(id, clazz)
        .setHandler(ar -> {
          if(ar.succeeded()) {
            Util.onSuccessResponse(rc, 200, ar.result());
          } else {
            Util.onErrorResponse(rc, 400, ar.cause());
          }
        });
    } else {
      Util.onErrorResponse(rc, 400, new IllegalArgumentException("Request body is empty"));
    }
  }

  // todo: implement this function
  @Override
  public void deleteOne(RoutingContext rc) {}

}
