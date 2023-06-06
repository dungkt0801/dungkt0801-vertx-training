package com.example.starter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {

  private String id;

  private String name;

  private String birthDay;

  private String classId;

  public Student(JsonObject jsonObject) {

    JsonObject idString = jsonObject.getJsonObject("_id");
    this.id = idString.getString("$oid");

    String name = jsonObject.getString("name");
    if(name != null) {
      this.name = jsonObject.getString("name");
    }

    this.birthDay = jsonObject.getJsonObject("birthDay").getString("$date");

    JsonObject classId = jsonObject.getJsonObject("classId");
    if(classId != null) {
      this.classId = classId.getString("$oid");
    } else {
      this.classId = null;
    }
  }

}
