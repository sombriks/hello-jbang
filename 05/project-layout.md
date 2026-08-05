# Project layout

Unlike maven, jbang is not opinionated regarding how you should organize your 
project.

But that doesn't mean that everything should be inside one single java source.

This is not the case.

For example, for those who comes from a maven or gradle culture, the following 
setup is more that familiar:

```bash
mkdir -p src/main/{java,resources}
mkdir -p src/main/java/foo/bar/{configs,controllers,services,models}
touch src/main/java/foo/bar/TodoApp.java
touch src/main/java/foo/bar/configs/TodoCfg.java
touch src/main/java/foo/bar/controllers/TodoCtl.java
touch src/main/java/foo/bar/services/TodoSvc.java
touch src/main/java/foo/bar/models/Todo.java
touch src/main/resources/application.properties
jbang init --deps \
io.vertx:vertx-core:4.5.7,\
io.vertx:vertx-web:4.5.7,\
io.vertx:vertx-jdbc-client:4.5.7,\
io.agroal:agroal-pool:2.5,\
com.h2database:h2:2.2.224 \
Build.java
```

Next, add the `//SOURCES` and `//FILES` comment configurations in `Build.java`:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS io.vertx:vertx-core:4.5.7
//DEPS io.vertx:vertx-web:4.5.7
//DEPS io.vertx:vertx-jdbc-client:4.5.7
//DEPS io.agroal:agroal-pool:2.5
//DEPS com.h2database:h2:2.2.224

//SOURCES src/main/java/**/*
//FILES src/main/resources/

void main(String... args) {
    // simply pass the args to the application entry point
    foo.bar.TodoApp.main(args);
}
```

The `TodoApp` class has no knowledge about how the project is built:

```java
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
```

## A lower barrier

This sample aims to demonstrate how far jbang can take the project. It is 
possible to start with a simple, humble configuration and grow from that. This 
is a long lost skill in java ecosystem that i am glad to see it coming back in 
such elegant way.

In fact, The build system, with dependency resolution capabilities and other 
neat features should be native in the jdk itself.
