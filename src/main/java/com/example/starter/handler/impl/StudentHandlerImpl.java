package com.example.starter.handler.impl;

import com.example.starter.handler.StudentHandler;
import com.example.starter.model.Student;
import com.example.starter.service.StudentService;
import com.example.starter.util.StudentUtil;
import com.example.starter.util.Util;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;

@RequiredArgsConstructor
public class StudentHandlerImpl implements StudentHandler {

  private final StudentService studentService;

  @Override
  public void findAll(RoutingContext rc) {
    studentService.findAll(getQueryParams(rc))
      .setHandler(ar -> {
        if(ar.succeeded()) {
          Util.onSuccessResponse(rc, 200, ar.result());
        } else {
          Util.onErrorResponse(rc, 500, ar.cause());
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
            if(ar.cause() instanceof NoSuchElementException) {
              Util.onErrorResponse(rc, 404, ar.cause());
            } else {
              Util.onErrorResponse(rc, 500, ar.cause());
            }
          }
        });
    } else {
      Util.onErrorResponse(rc, 400, new NoSuchElementException("Invalid student id"));
    }
  }

  @Override
  public void insertOne(RoutingContext rc) {

    JsonObject body = rc.getBodyAsJson();

    String error = validateStudentJsonObject(body);
    if(!error.isEmpty()) {
      Util.onErrorResponse(rc, 400, new IllegalArgumentException(error));
      return;
    }

    final Student student = StudentUtil.studentFromJsonObject(body);
    studentService.insertOne(student)
      .setHandler(ar -> {
        if(ar.succeeded()) {
          Util.onSuccessResponse(rc, 200, ar.result());
        } else {
          Util.onErrorResponse(rc, 500, ar.cause());
        }
      });
  }

  @Override
  public void updateOne(RoutingContext rc) {

    final String id = rc.pathParam("id");
    if(!Util.isValidObjectId(id)) {
      Util.onErrorResponse(rc, 400, new IllegalArgumentException("Invalid student id"));
      return;
    }

    JsonObject body = rc.getBodyAsJson();
    String error = validateStudentJsonObject(body);
    if(!error.isEmpty()) {
      Util.onErrorResponse(rc, 400, new IllegalArgumentException(error));
      return;
    }

    final Student student = StudentUtil.studentFromJsonObject(body);
    student.setId(id);
    studentService.updateOne(student)
      .setHandler(ar -> {
        if(ar.succeeded()) {
          Util.onSuccessResponse(rc, 200, ar.result());
        } else {
          if(ar.cause() instanceof NoSuchElementException) {
            Util.onErrorResponse(rc, 404, ar.cause());
          } else {
            Util.onErrorResponse(rc, 500, ar.cause());
          }
        }
      });
  }

  @Override
  public void deleteOne(RoutingContext rc) {
    final String id = rc.pathParam("id");
    if(!Util.isValidObjectId(id)) {
      Util.onErrorResponse(rc, 400, new IllegalArgumentException("Invalid student id"));
      return;
    }

    studentService.deleteOne(id)
      .setHandler(ar -> {
        if(ar.succeeded()) {
          Util.onSuccessResponse(rc, 200, ar.result());
        } else {
          if(ar.cause() instanceof NoSuchElementException) {
            Util.onErrorResponse(rc, 404, ar.cause());
          } else {
            Util.onErrorResponse(rc, 500, ar.cause());
          }
        }
      });
  }

  private JsonObject getQueryParams(RoutingContext rc) {
    MultiMap queryParams = rc.request().params();
    JsonObject query = new JsonObject();

    // name
    String name = queryParams.get("name");
    if(name != null && !name.isEmpty()) {
      query.put("name", new JsonObject()
        .put("$regex", name.trim())
        .put("$options", "i")
      );
    }

    // classId
    String classId = queryParams.get("classId");
    if(classId != null && !classId.isEmpty()) {
      query.put("classId", new JsonObject().put("$oid", new ObjectId(classId.trim()).toString()));
    }

    // className
    String className = queryParams.get("className");
    if(className != null && !className.isEmpty()) {
      query.put("className", className.trim());
    }

    // birthDay
    String birthDay = queryParams.get("birthDay");
    if(birthDay != null && !birthDay.isEmpty()) {
      query.put("birthDay", birthDay.trim());
    }

    return query;
  }

  private String validateStudentJsonObject(JsonObject jsonObject) {

    if (jsonObject.isEmpty()) {
      return "Body is empty";
    }

    // Check if "name" field exists and is not empty
    if (!jsonObject.containsKey("name") || jsonObject.getString("name").isEmpty()) {
      return "Student name is required";
    }

    // Check if "birthDay" field exists and follows the format "yyyy-MM-dd"
    String invalidBirthFormat = "Birthday must be in the 'yyyy-MM-dd' format";
    if (!jsonObject.containsKey("birthDay")) {
      return invalidBirthFormat;
    } else {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      try {
        LocalDate.parse(jsonObject.getString("birthDay"), formatter);
      } catch (DateTimeParseException e) {
        return invalidBirthFormat;
      }
    }

    if (!jsonObject.containsKey("classId") || jsonObject.getString("classId").isEmpty()) {
      return "Class is required";
    }

    if(!Util.isValidObjectId(jsonObject.getString("classId"))) {
      return "Invalid class id";
    }

    return "";
  }

}
