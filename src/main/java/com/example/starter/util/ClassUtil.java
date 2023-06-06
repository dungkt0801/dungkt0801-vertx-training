package com.example.starter.util;

import com.example.starter.model.Class;
import io.vertx.core.json.JsonObject;

public class ClassUtil {

  public static Class classFromJsonObject (JsonObject jsonObject) {
    Class clazz = new Class();

    String className = jsonObject.getString("className");
    if(className != null && !className.isEmpty()) {
      clazz.setClassName(className);
    }

    Long numberOfStudents = jsonObject.getLong("numberOfStudents");
    if(numberOfStudents != null && numberOfStudents > 0) {
      clazz.setNumberOfStudents(numberOfStudents);
    }

    return clazz;
  }

  public static JsonObject jsonObjectFromClass(Class clazz) {

    JsonObject jsonObject = new JsonObject();

    String className = clazz.getClassName();
    if(className != null && !className.isEmpty()) {
      jsonObject.put("className", className);
    }

    Long numberOfStudents = clazz.getNumberOfStudents();
    if(numberOfStudents != null) {
      jsonObject.put("numberOfStudents", numberOfStudents);
    }

    return jsonObject;
  }

}
