package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    vertx.deployVerticle(new StudentVerticle());
    vertx.deployVerticle(new ClassVerticle());
    System.out.println("Welcome to Vertx");
  }

}
