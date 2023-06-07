package com.example.starter.util;

import com.example.starter.model.Student;
import io.vertx.core.json.JsonObject;
import org.bson.types.ObjectId;

public class StudentUtil {

  public static Student studentFromJsonObject (JsonObject jsonObject) {
    Student student = new Student();

    String name = jsonObject.getString("name");
    if(name != null && !name.isEmpty()) {
      student.setName(name);
    }

    String birthDay= jsonObject.getString("birthDay");
    if (birthDay != null && !birthDay.isEmpty()) {
      student.setBirthDay(birthDay);
    }

    student.setClassId(jsonObject.getString("classId"));

    return student;
  }

  public static JsonObject jsonObjectFromStudent(Student student) {

    JsonObject jsonObject = new JsonObject();

    String name = student.getName();
    if(name != null) {
      jsonObject.put("name", name);
    }

    String birthDay = student.getBirthDay();
    if(birthDay != null) {
      jsonObject.put("birthDay", birthDay);
    }

    String classId = student.getClassId();
    if(classId != null) {
      jsonObject.put("classId", new JsonObject().put("$oid", new ObjectId(classId).toString()));
    }

    return jsonObject;
  }
}
