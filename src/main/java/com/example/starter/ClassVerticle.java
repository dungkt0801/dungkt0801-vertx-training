package com.example.starter;

import com.example.starter.handler.ClassHandler;
import com.example.starter.repository.ClassRepository;
import com.example.starter.router.ClassRouter;
import com.example.starter.service.ClassService;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;

public class ClassVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    final ConfigStoreOptions store = new ConfigStoreOptions().setType("env");
    final ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(store);
    final ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

    retriever.getConfig()
      .flatMap(configurations -> {
        final MongoClient mongoClient = createMongoClient(vertx, configurations);

        final ClassRepository classRepository = new ClassRepository(mongoClient);
        final ClassService classService = new ClassService(classRepository);
        final ClassHandler classHandler = new ClassHandler(classService);
        final ClassRouter classRouter = new ClassRouter(vertx, classHandler);

        return createHttpServer(classRouter.getRouter(), configurations);
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
      .listen(configurations.getInteger("HTTP_PORT", 8081));
  }
}
