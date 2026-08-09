# Going enterprise, this time with more feeling

There are some fun facts about spring. For example:

The creator of spring framework started it as a book to point out the marvels 
of java enterprise edition. So, spring started as documentation first. Also, he 
started do disbelieve jee the more he studied it. Too convoluted, unnecessarily 
complex. It's a [fun story][documentary].

[documentary]: https://www.youtube.com/watch?v=4qTwA6Y1-OI

## Basic setup

The simplest setup goes like this:

```bash
jbang init --deps \
org.liquibase:liquibase-core:5.0.3,\
org.springframework.boot:spring-boot-starter-web:4.1.0,\
org.springframework.boot:spring-boot-starter-data-jpa:4.1.0,\
org.springframework.boot:spring-boot-starter-liquibase:4.1.0,\
org.springframework.boot:spring-boot-starter-test:4.1.0,\
org.junit.platform:junit-platform-launcher:6.1.2,\
org.hamcrest:hamcrest:3.0,\
com.h2database:h2:2.3.232 \
HelloSpring.java
mkdir -p src/app/{controllers,models,repositories,services}
mkdir -p resources/changelogs/2026/08/09
touch src/app/{App,AppTest}.java
touch src/app/controllers/TodoCtl.java
touch src/app/models/Todo.java
touch src/app/repositories/TodoRepo.java
touch src/app/services/TodoSvc.java
touch resources/{application,application-test}.yml
touch resources/changelogs/root-changelog.yml
touch resources/changelogs/2026/08/09/{1-create-database,2-test-data}.sql
```

This is a non-trivial _hello world_ with spring and database migrations with 
[liquibase][liquibase]. Scaffolded in a single (long) command line!

[liquibase]: https://docs.liquibase.com/

## Our entry point

As usual, the main jbang script acts more as a dependency management than 
proper code, except for the code needed to run the tests:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS org.liquibase:liquibase-core:5.0.3
//DEPS org.springframework.boot:spring-boot-starter-web:4.1.0
//DEPS org.springframework.boot:spring-boot-starter-data-jpa:4.1.0
//DEPS org.springframework.boot:spring-boot-starter-liquibase:4.1.0
//DEPS org.springframework.boot:spring-boot-starter-test:4.1.0
//DEPS org.junit.platform:junit-platform-launcher:6.1.2
//DEPS org.hamcrest:hamcrest:3.0
//DEPS com.h2database:h2:2.3.232
//SOURCES src/**/*.java
//FILES resources

import app.App;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import java.io.PrintWriter;

void main(String... args) {
    if (args.length > 0 && "test".equals(args[0])) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
            .request()
            .selectors(DiscoverySelectors.selectPackage("app"))
            .build();

        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        try (LauncherSession session = LauncherFactory.openSession()) {
            Launcher launcher = session.getLauncher();
            launcher.registerTestExecutionListeners(listener);
            launcher.execute(request);
        }

        var summary = listener.getSummary();
        summary.printFailuresTo(new PrintWriter(System.err), 1);
        summary.printTo(new PrintWriter(System.out));

        System.exit(summary.getTestsFailedCount() > 0 ? 1 : 0);
    } else {
        App.main(args);
    }
}
```

The test itself need no knowledge of jbang or anything but spring:

```java
package app;

import app.models.Todo;
import app.services.TodoSvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@ActiveProfiles("test")
public class AppTest {

    @Autowired
    private TodoSvc todoSvc;

    @Test
    public void deveBuscarDadosIniciaisDoLiquibaseViaService() {
        List<Todo> todos = todoSvc.listarTodos();
        assertThat(todos, is(notNullValue()));
        assertThat(todos.size(), is(greaterThanOrEqualTo(2)));

        String primeiroTitulo = todos.get(0).getTitulo();
        assertThat(primeiroTitulo, containsString("Estudar JBang"));
    }
}
```

## Exporting

Export the project as a portable and things will work standalone:

```bash
jbang export portable HelloSpring.java
java -jar HelloSpring.jar
```

This approach, however, expects the lib folder next to the jar. Keep that in 
mind.

For the **fatjar** approach, follow these steps:

```bash
# under construction
```

## The state of thing regarding jbang for serious spring projects

This is a marriage mad in heaven.

Spring scales up and down as good as jbang does and don't suffer from the rigid 
standarization that classic jee suffers.

Ir simply works.

