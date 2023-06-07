package com.example.starter;

import com.example.starter.handler.ClassHandler;
import com.example.starter.handler.impl.ClassHandlerImpl;
import com.example.starter.repository.ClassRepository;
import com.example.starter.repository.impl.ClassRepositoryImpl;
import com.example.starter.router.ClassRouter;
import com.example.starter.service.ClassService;
import com.example.starter.service.impl.ClassServiceImpl;
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

public class ClassVerticle extends AbstractVerticle {

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    final ConfigStoreOptions store = new ConfigStoreOptions().setType("env");
    final ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(store);
    final ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

    retriever.getConfig(ar -> {
      if(ar.failed()) {
        startFuture.fail(ar.cause());
      } else {
        createServicesAndStartServer(ar.result(), startFuture);
      }
    });
  }

  private void createServicesAndStartServer(JsonObject configurations, Future<Void> startFuture) {
    final MongoClient mongoClient = createMongoClient(vertx, configurations);

    final ClassRepository classRepository = new ClassRepositoryImpl(mongoClient);
    final ClassService classService = new ClassServiceImpl(classRepository);
    final ClassHandler classHandler = new ClassHandlerImpl(classService);
    final ClassRouter classRouter = new ClassRouter(vertx, classHandler);

    HttpServer server = createHttpServer(classRouter.getRouter(), configurations);
    if(server != null) {
      startServer(server, configurations, startFuture);
    } else {
      System.out.println("Error occurred before creating a new HTTP server");
    }
  }

  private void startServer(HttpServer server, JsonObject configurations, Future<Void> startFuture) {
    server.listen(configurations.getInteger("HTTP_PORT", 8081) , result -> {
      System.out.println("HTTP Server listening on port " + server.actualPort());
      startFuture.complete();
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
      .requestHandler(rc);
  }
}
