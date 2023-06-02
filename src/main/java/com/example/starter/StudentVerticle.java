package com.example.starter;

import com.example.starter.handler.StudentHandler;
import com.example.starter.repository.StudentRepository;
import com.example.starter.router.StudentRouter;
import com.example.starter.service.StudentService;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
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

    retriever.getConfig()
      .flatMap(configurations -> {
        final MongoClient client = createMongoClient(vertx, configurations);

        final StudentRepository bookRepository = new StudentRepository(client);
        final StudentService bookService = new StudentService(bookRepository);
        final StudentHandler bookHandler = new StudentHandler(bookService);
        final StudentRouter bookRouter = new StudentRouter(vertx, bookHandler);

        return createHttpServer(bookRouter.getRouter(), configurations);
      })
      .onComplete(ar -> {
          if(ar.succeeded()) {
            System.out.println("HTTP Server listening on port " + ar.result().actualPort());
          } else {
            System.out.println("Error occurred before creating a new HTTP server: "  + ar.cause().getMessage());
          }
        }
      );
  }

  private MongoClient createMongoClient(Vertx vertx, JsonObject configurations) {
    final JsonObject config = new JsonObject()
      .put("host", "localhost")
      .put("db_name", "demo");

    return MongoClient.createShared(vertx, config);
  }

  private Future<HttpServer> createHttpServer(Router rc, JsonObject configurations) {
    return vertx
      .createHttpServer()
      .requestHandler(rc)
      .listen(configurations.getInteger("HTTP_PORT", 8080));
  }
}
