# Going native with JBang

This is a huge trend over the entire industry. _Go native_. There are benefits, 
of course, but they don't diminish the good parts of what java ecosystem 
delivers normally.

In fact, most java projects need special care to get properly transformed into 
native images, but i'll not discuss that here.

Anyway, jbang delivers a nice experience for those attempting to produce native 
things, as long as your environment is configured right.

## Enter GraalVM

Use our friend [sdkman][sdk] to get the native guns:

[sdk]: https://sdkman.io

```bash
sdk install java 25.2.4-graalce
```

Test the native powers this way:

```bash
rm Native Native.java
jbang init Native.java
jbang export native Native.java
./Native
```

## A more complete example

Consider this setup for a first-class microservice in java:

```bash 
jbang init --deps \
io.javalin:javalin:7.2.2,\
org.jdbi:jdbi3-core:3.54.0,\
com.fasterxml.jackson.core:jackson-databind:2.22.1,\
org.slf4j:slf4j-simple:2.0.13,\
com.h2database:h2:2.2.224,\
Build.java
mkdir -p src/main/{java,resources}
mkdir -p src/main/java/app/todos
mkdir -p src/main/resources/META-INF/native-image
touch src/main/java/app/App.java
touch src/main/java/app/todos/Todo{Ctl,Svc,Mdl}.java
touch src/main/resources/application.properties
touch src/main/resources/META-INF/native-image/resource-config.json
touch src/main/resources/META-INF/native-image/reflect-config.json
```

The Build.java goes as usual:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS io.javalin:javalin:7.2.2
//DEPS org.jdbi:jdbi3-core:3.54.0
//DEPS com.fasterxml.jackson.core:jackson-databind:2.22.1
//DEPS org.slf4j:slf4j-simple:2.0.13
//DEPS com.h2database:h2:2.2.224
//SOURCES src/main/java
//FILES src/main/resources

void main(String... args) throws Exception {
    app.App.main(args);
}
```

The App looks like this:

```java
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
```

You get the idea. Run as usual:

```bash
jbang Build.java
```

## The bundle config

Although javalin does its best to avoid reflection, which is a weak point on 
java native images, some dependencies need external resources, mostly 
locales, to work properly. 

But the native image generation strips out those resources from the final 
binary and, because of that, we end up with nasty runtime errors.

To avoid this, the special configuration,
`META-INF/native-image/resource-config.json` defines every runtime bundle 
that will be needed:

```json
{
  "bundles": [
    {
      "name": "jakarta.servlet.LocalStrings"
    },
    {
      "name": "jakarta.servlet.http.LocalStrings"
    }
  ],
  "resources": {
    "includes": [
      {
        "pattern": "application\\.properties"
      },
      {
        "pattern": "\\Qapplication.properties\\E"
      }
    ]
  }
}
```

## Why people avoid reflection in Native images

Another issue is reflection. If you really need reflection, it mjust be 
_authorized_ in `META-INF/native-image/reflect-config.json`:

```json
[
  {
    "name": "org.h2.Driver",
    "allPublicMethods": true,
    "allDeclaredConstructors": true
  },
  {
    "name": "app.todos.TodoMdl",
    "allPublicMethods": true,
    "allDeclaredConstructors": true
  }
]
```

Imagine that, reflection became illegal in java!

Anyway, that's it, we're good to go ahead and create a native image from this 
code using jbang:

```bash
rm -rf lib Build Build.jar Build-fatjar.jar
jbang cache clear
jbang export native Build.java
```

## Is it worth the trouble?

So, what do we get for our troubles?

```bash
jbang export portable Build.java
# ...
java -jar Build.jar
# ...
[main] INFO io.javalin.Javalin - Javalin started in 314ms \o/
```

Meanwhile, the native image:

```bash
./Build
# ...
[main] INFO io.javalin.Javalin - Javalin started in 64ms \o/
```

And even better startup times are possible.

One drawback are the compilation times. They make regular java builds look 
like a regular, interactive interpreter.

But boy it runs fast! 
