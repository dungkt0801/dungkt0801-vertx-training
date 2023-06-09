package com.example.starter.util;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.bson.types.ObjectId;

public class Util {
  public static void onSuccessResponse(RoutingContext rc, int status, Object object) {
    rc.response()
      .setStatusCode(status)
      .putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(object));
  }

  public static void onErrorResponse(RoutingContext rc, int status, Throwable throwable) {
    JsonObject error = new JsonObject().put("error", throwable.getMessage());

    rc.response()
      .setStatusCode(status)
      .putHeader("Content-Type", "application/json")
      .end(Json.encodePrettily(error));
  }

  public static boolean isValidObjectId(String idString) {
    try {
      new ObjectId(idString);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }

  public static JsonObject convertStringIdToObjectId(String id) {
    return new JsonObject().put("_id", new JsonObject().put("$oid", id));
  }

}
