package foo.bar.controllers;

import foo.bar.services.TodoSvc;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

public class TodoCtl {

    private final TodoSvc todoSvc;

    public TodoCtl(TodoSvc todoSvc) {
        this.todoSvc = todoSvc;
    }

    public void list(RoutingContext ctx) {
        todoSvc.list()
                .onSuccess(todos -> ctx.response()
                        .putHeader("content-type", "application/json")
                        .end(Json.encode(todos)))
                .onFailure(err -> ctx.response()
                        .setStatusCode(500)
                        .putHeader("content-type", "application/json")
                        .end());
    }

    public void insert(RoutingContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
