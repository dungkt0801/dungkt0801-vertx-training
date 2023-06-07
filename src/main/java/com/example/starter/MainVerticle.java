package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() {
    vertx.deployVerticle(new StudentVerticle(), this::handleDeploymentResult);
    vertx.deployVerticle(new ClassVerticle(), this::handleDeploymentResult);
    System.out.println("Welcome to Vertx");
  }

  private void handleDeploymentResult(AsyncResult<String> result) {
    if (result.succeeded()) {
      System.out.println("Verticle deployed successfully. Deployment id is: " + result.result());
    } else {
      System.out.println("Failed to deploy verticle. Cause is: " + result.cause());
    }
  }

}
