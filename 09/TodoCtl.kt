import io.javalin.http.Context
import io.javalin.http.bodyAsClass
import io.javalin.http.sse.SseClient

class TodoCtl(val svc: TodoSvc) {
    private var client: SseClient? = null

    fun list(ctx: Context) {
        ctx.json(svc.list())
    }

    fun create(ctx: Context) {
        val todo = ctx.bodyAsClass<Todo>()
        val result = svc.create(todo)
        ctx.header("Location", "/todos/${result.id}")
            .status(201)
    }

    fun find(ctx: Context) {
        TODO()
    }

    fun update(ctx: Context) {
        TODO()
    }

    fun del(ctx: Context) {
        TODO()
    }

    fun events(client: SseClient) {
        this.client = client
    }
}
