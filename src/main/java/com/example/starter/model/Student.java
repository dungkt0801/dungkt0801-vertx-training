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

    JsonObject idString = jsonObject.getJsonObject("_id");
    this.id = idString.getString("$oid");

    String name = jsonObject.getString("name");
    if(name != null) {
      this.name = jsonObject.getString("name");
    }

    Long birthDayTimestampStr = jsonObject.getLong("birthDay");
    if(birthDayTimestampStr != null) {
      this.birthDay = new Date(birthDayTimestampStr);
    }

    JsonObject classId = jsonObject.getJsonObject("classId");
    if(classId != null) {
      this.classId = classId.getString("$oid");
    } else {
      this.classId = null;
    }
  }

}
