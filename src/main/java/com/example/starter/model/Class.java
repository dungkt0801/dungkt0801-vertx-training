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

  private Long numberOfStudents;

  public Class(JsonObject jsonObject) {

    String idString = jsonObject.getString("_id");
    this.id = new JsonObject(idString).getString("$oid");

    this.numberOfStudents = jsonObject.getLong("numberOfStudents");

  }

}
