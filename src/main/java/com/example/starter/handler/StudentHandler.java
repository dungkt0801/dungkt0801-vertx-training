package com.example.starter.handler;

import com.example.starter.model.Student;
import com.example.starter.service.StudentService;
import com.example.starter.util.StudentUtil;
import com.example.starter.util.Util;
import io.vertx.ext.web.RoutingContext;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentHandler {

  private final StudentService studentService;

  public void findAll(RoutingContext rc) {
    studentService.findAll()
      .setHandler(ar -> {
        if(ar.succeeded()) {
          Util.onSuccessResponse(rc, 200, ar.result());
        } else {
          Util.onErrorResponse(rc, 400, ar.cause());
        }
      });
  }

  public void findById(RoutingContext routingContext) {
    final String id = routingContext.pathParam("id");
    if(Util.isValidObjectId(id)) {
      studentService.findById(id)
        .setHandler(ar -> {
          if(ar.succeeded()) {
            Util.onSuccessResponse(routingContext, 200, ar.result());
          } else {
            Util.onErrorResponse(routingContext, 400, ar.cause());
          }
        });
    } else {
      Util.onErrorResponse(routingContext, 400, new NoSuchElementException("Invalid id"));
    }
  }

  public void insertOne(RoutingContext routingContext) {
    if(routingContext.getBodyAsJson() != null) {
      final Student student = StudentUtil.studentFromJsonObject(routingContext.getBodyAsJson());
      studentService.insertOne(student)
        .setHandler(ar -> {
          if(ar.succeeded()) {
            Util.onSuccessResponse(routingContext, 200, ar.result());
          } else {
            Util.onErrorResponse(routingContext, 400, ar.cause());
          }
        });
    } else {
      Util.onErrorResponse(routingContext, 400, new IllegalArgumentException("Request body is empty"));
    }
  }

  public void updateOne(RoutingContext routingContext) {
    if(routingContext.getBodyAsJson() != null) {
      final  String id = routingContext.pathParam("id");
      final Student student = StudentUtil.studentFromJsonObject(routingContext.getBodyAsJson());
      studentService.updateOne(id, student)
        .setHandler(ar -> {
          if(ar.succeeded()) {
            Util.onSuccessResponse(routingContext, 200, ar.result());
          } else {
            Util.onErrorResponse(routingContext, 400, ar.cause());
          }
        });
    }
  }

  public void deleteOne(RoutingContext routingContext) {
    final String id = routingContext.pathParam("id");
    studentService.deleteOne(id)
      .setHandler(ar -> {
        if(ar.succeeded()) {
          Util.onSuccessResponse(routingContext, 200, ar.result());
        } else {
          Util.onErrorResponse(routingContext, 400, ar.cause());
        }
      });
  }

}
