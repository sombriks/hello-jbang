package app;

import app.todos.TodoCtl;
import app.todos.TodoSvc;
import io.javalin.Javalin;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class App {
    public static void main(String... args) throws Exception {
        TodoSvc svc = new TodoSvc();
        TodoCtl ctl = new TodoCtl(svc);
        svc.init();
        Javalin.create(c -> {
                    c.routes.apiBuilder(() -> {
                        path("todos", () -> {
                            get(ctl::list);
                            post(ctl::create);
                        });
                    });
                })
                .start(7000);
    }
}
