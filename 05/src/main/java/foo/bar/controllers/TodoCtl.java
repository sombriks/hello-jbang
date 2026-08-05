package foo.bar.controllers;

import io.vertx.ext.web.RoutingContext;

import foo.bar.services.TodoSvc;

public class TodoCtl {

    private final TodoSvc todoSvc;

    public TodoCtl (TodoSvc todoSvc) {
        this.todoSvc = todoSvc;
    }

    public void list(RoutingContext ctx) {

        ctx.response().end("Lista de usuários!!");
    }

    public void insert(RoutingContext ctx) {
        ctx.response().end("Lista de usuários!!");
    }
}
