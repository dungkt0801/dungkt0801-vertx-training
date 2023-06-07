package com.example.starter.handler.impl;

import com.example.starter.handler.StudentHandler;
import com.example.starter.model.Student;
import com.example.starter.service.StudentService;
import com.example.starter.util.StudentUtil;
import com.example.starter.util.Util;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@RequiredArgsConstructor
public class StudentHandlerImpl implements StudentHandler {

  private final StudentService studentService;

  @Override
  public void findAll(RoutingContext rc) {
    studentService.findAll(buildQueryParams(rc))
      .setHandler(ar -> {
        if(ar.succeeded()) {
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
      studentService.findById(id)
        .setHandler(ar -> {
          if(ar.succeeded()) {
            Util.onSuccessResponse(rc, 200, ar.result());
          } else {
            Util.onErrorResponse(rc, 400, ar.cause());
          }
        });
    } else {
      Util.onErrorResponse(rc, 400, new NoSuchElementException("Invalid student id"));
    }
  }

  @Override
  public void insertOne(RoutingContext rc) {
    if(rc.getBodyAsJson() != null) {
      final Student student = StudentUtil.studentFromJsonObject(rc.getBodyAsJson());
      studentService.insertOne(student)
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
      final Student student = StudentUtil.studentFromJsonObject(rc.getBodyAsJson());
      studentService.updateOne(id, student)
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
  public void deleteOne(RoutingContext rc) {
    final String id = rc.pathParam("id");
    studentService.deleteOne(id)
      .setHandler(ar -> {
        if(ar.succeeded()) {
          Util.onSuccessResponse(rc, 200, ar.result());
        } else {
          Util.onErrorResponse(rc, 400, ar.cause());
        }
      });
  }

  private JsonObject buildQueryParams(RoutingContext rc) {
    MultiMap queryParams = rc.request().params();
    JsonObject query = new JsonObject();

    // name
    String name = queryParams.get("name");
    if(name != null && !name.isEmpty()) {
      query.put("name", new JsonObject().put("$regex", name));
    }

    // classId
    String classId = queryParams.get("classId");
    if(classId != null && !classId.isEmpty()) {
      query.put("classId", new JsonObject().put("$oid", new ObjectId(classId).toString()));
    }

    return query;
  }

}
