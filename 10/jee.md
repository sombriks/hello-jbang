# Going enterprise

Anyone aware of the JEE history would giggle a little just by thinking running 
those overly complex, full of xml configurations, ear/war java applications 
from a simple command line script.

Then [Spring Boot][boot] emerged and ate the market.

[boot]: https://spring.io/projects/spring-boot

But after the shock, over the years, [Jakarta EE][jee] applications fought 
complexity while kept a consistent API. 

The spec is beautiful.

## The (portable) application

The application is a simple, honest, JSON api:

```bash
mkdir -p src/app/{controllers,models,repositories,services}
touch src/app/App.java
touch src/app/controllers/TodoRes.java
touch src/app/models/Todo.java
touch src/app/repositories/TodoRepo.java
touch src/app/services/TodoSvc.java
mkdir src/META-INF
touch src/META-INF/{beans,persistence}.xml
```

And the implementation  goes by the book:

```java
// App.java
package app;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("api")
public class App extends Application {}

```
---
```java
// TodoRes.java
package app.controllers;

import app.models.Todo;
import app.services.TodoSvc;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("todos")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TodoRes {

    @Inject
    private TodoSvc service;

    @GET
    public List<Todo> list() {
        return service.listarTodas();
    }

    @POST
    public Todo create(Todo todo) {
        return service.salvar(todo);
    }
}

```

All other classes goes with those well-known JEE annotations.

The `persistence.xml` is also tweaked to rely only on portable properties:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<persistence version="3.0"
             xmlns="https://jakarta.ee"
             xmlns:xsi="http://w3.org"
             xsi:schemaLocation="https://jakarta.ee
             https://jakarta.ee/persistence_3_0.xsd">
    <persistence-unit name="default" transaction-type="JTA">
        <class>app.models.Todo</class>
        <properties>
            <property name="jakarta.persistence.jdbc.driver"
                      value="com.h2database.Driver"/>
            <property name="jakarta.persistence.jdbc.url"
                      value="jdbc:h2:mem:tododb;DB_CLOSE_DELAY=-1;MODE=LEGACY"/>
            <property name="jakarta.persistence.jdbc.user" value="sa"/>
            <property name="jakarta.persistence.jdbc.password" value=""/>
            <property
                name="jakarta.persistence.schema-generation.database.action"
                value="create"/>
        </properties>
    </persistence-unit>
</persistence>
```

The `beans.xml` is an empty file.

## Building the portable JEE artifact

Unlike everything we saw so far, we don't run the application straightforward.

Instead, we build a [war][war] file to be deployed into a jee server.

[war]: https://tinyurl.com/47a6za3x

First, init the `Portable.java` source to compile the classes for us:

```bash
jbang init --java=21 --deps \
jakarta.platform:jakarta.jakartaee-web-api:10.0.0 \
Portable.java
```

The `--java=21` flag matters due to some incompatibilities that some jee 
servers has with java 25, used on all examples until now.

The `Portable.java` entry point looks like this:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS jakarta.platform:jakarta.jakartaee-web-api:10.0.0
//JAVA 21
//FILES META-INF/persistence.xml=src/META-INF/persistence.xml
//FILES META-INF/beans.xml=src/META-INF/beans.xml
//SOURCES src/**/*.java

public class Portable {
    public static void main(String... args) {
        System.out.println("Hello World");
    }
}

```

Just _one_ single `//DEPS` on the web profile jee specification. 

Now we're good to go ahead and generate the jar:

```bash
jbang export portable --force Portable.java
```

### The war format

Now it's time to perform a small convoluted step and smash the jar and create 
a war file:

```bash
mkdir -p ROOT/WEB-INF/classes
unzip ./Portable.jar -d ROOT/WEB-INF/classes/
cd ROOT
rm -f WEB-INF/classes/Portable.class
jar -cvfM ../ROOT.war *
cd ..
rm -rf ROOT
```

Now our artifact is ready to get deployed on any _modern JEE AppServer_.

## Getting a JEE compatible app server

Now we need to find a server compatible with this application. This step should 
be easy.

## Payara

We ca use jbang with no major issues to run our artifact:

```bash
jbang --java=21 \
fish.payara.extras:payara-micro:7.2026.2 \
--port 8080 \
--deploy ROOT.war
```

## Wildfly Glow

For wildfly, just one extra step to generate a true portable jar file:

```bash
jbang org.wildfly.glow:wildfly-glow:1.4.0.Final \
scan ROOT.war \
--provision=BOOTABLE_JAR \
--add-ons=h2-database:default
java -jar ROOT-41.0.0.Final-bootable.jar
```

## The state of JEE in the age of microservices

JEE barely passes this test, to be honest. The feeling that we're seeing a huge 
elephant riding a child-sized bicycle remains.

This is not an overall issue, just in this use case, where i am scaling from 
nothing to any arbitrary project size and structure using just jbang. And the 
fact that two of may JEE server i tried to run actually worked is a kind of 
good sign.

The spec goes in its own pace, it's open and keeps evolving.
