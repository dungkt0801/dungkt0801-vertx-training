package com.example.starter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

  private String _id;

  private String name;

  public Student(JsonObject jsonObject) {
    // _id
    String idString = jsonObject.getString("_id");
    JsonObject idJson = new JsonObject(idString);
    this._id = idJson.getString("$oid");

    // name
    this.name = jsonObject.getString("name");
  }

}
