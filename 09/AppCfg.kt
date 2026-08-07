import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.config.JavalinConfig
import io.javalin.config.RoutesConfig
import org.jetbrains.exposed.v1.jdbc.Database

object AppCfg {

    fun database() {
        Database.connect(
            url = "jdbc:h2:./todo.h2;DB_CLOSE_DELAY=-1;",
            driver = "org.h2.Driver"
        )
    }

    fun cors(config: JavalinConfig) {
        config.bundledPlugins.enableCors { cors ->
            cors.addRule {
                it.anyHost()
            }
        }
    }

    fun api(ctl: TodoCtl, routes: RoutesConfig) {
        routes.apiBuilder {
            path("todos") {
                get(ctl::list)
                post(ctl::create)
                path("{id}") {
                    get(ctl::find)
                    put(ctl::update)
                    delete(ctl::del)
                }
                sse("events", ctl::events)
            }
        }
    }
}
