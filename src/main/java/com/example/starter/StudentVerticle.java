package com.example.starter;

import com.example.starter.handler.StudentHandler;
import com.example.starter.repository.StudentRepository;
import com.example.starter.router.StudentRouter;
import com.example.starter.service.StudentService;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;

public class StudentVerticle extends AbstractVerticle {


  @Override
  public void start() throws Exception {
    final ConfigStoreOptions store = new ConfigStoreOptions().setType("env");
    final ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(store);
    final ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

    retriever.getConfig(configurations -> {
      final MongoClient mongoClient = createMongoClient(vertx, configurations.result());

      final StudentRepository studentRepository = new StudentRepository(mongoClient);
      final StudentService studentService = new StudentService(studentRepository);
      final StudentHandler studentHandler = new StudentHandler(studentService);
      final StudentRouter studentRouter = new StudentRouter(vertx, studentHandler);

      HttpServer server = createHttpServer(studentRouter.getRouter(), configurations.result());

      if(server != null) {
        System.out.println("HTTP Server listening on port " + server.actualPort());
      } else {
        System.out.println("Error occurred before creating a new HTTP server");
      }
    });
  }

  private MongoClient createMongoClient(Vertx vertx, JsonObject configurations) {
    final JsonObject config = new JsonObject()
      .put("host", "localhost")
      .put("db_name", "demo");

    return MongoClient.createShared(vertx, config);
  }

  private HttpServer createHttpServer(Router rc, JsonObject configurations) {
    return vertx
      .createHttpServer()
      .requestHandler(rc)
      .listen(configurations.getInteger("HTTP_PORT", 8080));
  }
}
