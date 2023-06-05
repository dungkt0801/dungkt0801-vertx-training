package com.example.starter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Class {

  private String id;

  private String className;

  private Long numberOfStudents;

  public Class(JsonObject jsonObject) {
    // id
    JsonObject idObject = jsonObject.getJsonObject("_id");
    this.id = idObject.getString("$oid");

    // className
    String className = jsonObject.getString("className");
    if(className != null && !className.isEmpty()) {
      this.className = className;
    }

    // numberOfStudents
    Long numberOfStudents = jsonObject.getLong("numberOfStudents");
    if(numberOfStudents != null && numberOfStudents >= 0) {
      this.numberOfStudents = jsonObject.getLong("numberOfStudents");
    }
  }

}
