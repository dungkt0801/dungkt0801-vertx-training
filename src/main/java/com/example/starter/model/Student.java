package com.example.starter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.json.JsonObject;
import java.util.Date;
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

  private Date birthDay;

  private String classId;

  public Student(JsonObject jsonObject) {

    String idString = jsonObject.getString("_id");
    this.id = new JsonObject(idString).getString("$oid");

    String name = jsonObject.getString("name");
    if(name != null) {
      this.name = jsonObject.getString("name");
    }

    String birthDayTimestampStr = jsonObject.getString("birthDay");
    if(birthDayTimestampStr != null) {
      this.birthDay = new Date(Long.parseLong(birthDayTimestampStr));
    }

    String classId = jsonObject.getString("classId");
    if(classId != null) {
      JsonObject classIdJson = new JsonObject(classId);
      this.classId = classIdJson.getString("$oid");
    } else {
      this.classId = null;
    }
  }

}
