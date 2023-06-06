package com.example.starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startPromise) {
    vertx.deployVerticle(new StudentVerticle());
    vertx.deployVerticle(new ClassVerticle());
    System.out.println("Welcome to Vertx");
  }

}
