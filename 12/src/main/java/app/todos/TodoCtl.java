package app.todos;

import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class TodoCtl {

    private final TodoSvc svc;

    public TodoCtl(TodoSvc svc) {
        this.svc = svc;
    }

    public void list(@NotNull Context ctx) {
        ctx.json(svc.list());
    }

    public void create(@NotNull Context ctx) {
        TodoMdl todo = ctx.bodyAsClass(TodoMdl.class);
        ctx.json(svc.save(todo));
    }
}
