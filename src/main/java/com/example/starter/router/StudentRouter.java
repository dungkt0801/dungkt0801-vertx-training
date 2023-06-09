package com.example.starter.router;

import com.example.starter.handler.StudentHandler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StudentRouter {

  private final Vertx vertx;

  private final StudentHandler studentHandler;

  public Router getRouter() {
    final Router studentRouter = Router.router(vertx);

    studentRouter.route("/api/v1/students*").handler(BodyHandler.create());
    studentRouter.get("/api/v1/students").handler(studentHandler::findAll);
    studentRouter.get("/api/v1/students/:id").handler(studentHandler::findById);
    studentRouter.post("/api/v1/students").handler(studentHandler::insertOne);
    studentRouter.put("/api/v1/students/:id").handler(studentHandler::updateOne);
    studentRouter.delete("/api/v1/students/:id").handler(studentHandler::deleteOne);

    return studentRouter;
  }

}
