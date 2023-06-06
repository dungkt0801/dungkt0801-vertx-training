package com.example.starter.util;

import com.example.starter.model.Student;
import io.vertx.core.json.JsonObject;
import java.util.Date;
import org.bson.types.ObjectId;

public class StudentUtil {

  public static Student studentFromJsonObject (JsonObject jsonObject) {
    Student student = new Student();

    String name = jsonObject.getString("name");
    if(name != null && !name.isEmpty()) {
      student.setName(name);
    }

    Long birthDayTimestamp = jsonObject.getLong("birthDay");
    if (birthDayTimestamp != null) {
      student.setBirthDay(new Date(birthDayTimestamp));
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

    Date birthDay = student.getBirthDay();
    if(birthDay != null) {
      jsonObject.put("birthDay", birthDay.getTime());
    }

    String classId = student.getClassId();
    if(classId != null) {
      jsonObject.put("classId", new JsonObject().put("$oid", new ObjectId(classId).toString()));
    }

    return jsonObject;
  }
}
