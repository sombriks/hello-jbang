package foo.bar;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.jdbcclient.JDBCPool;

import foo.bar.configs.TodoCfg;
import foo.bar.controllers.TodoCtl;
import foo.bar.services.TodoSvc;

public class TodoApp {
    public static void main(String...args) throws Exception {

        Vertx vertx = Vertx.vertx();

        var cfg = new TodoCfg(vertx);
        var pool = cfg.configurePool();
        var todoSvc = new TodoSvc(pool);
        var todoCtl = new TodoCtl(todoSvc);

        Router router = Router.router(vertx);
        router.get("/todos").handler(todoCtl::list);
        router.post("/todos").handler(todoCtl::insert);

        todoSvc.init().onSuccess(_ -> {
            vertx.createHttpServer()
                .requestHandler(router)
                .listen(cfg.getServerPort());
        });
    }
}
