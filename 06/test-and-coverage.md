# Testing like a pro

So far we can build simple scripts, add resources, build complex 
infrastructures. 

But does it do what it is supposed to do?

To answer this, the answer is to add tests to the jbang project.

## Basic setup

Initialize the project as usual:

```bash
mkdir -p src/{main,test}/{java,resources}
mkdir -p src/{main,test}/java/xpto/baz
mkdir -p src/main/resources/META-INF
touch src/main/java/xpto/baz/{Main,TodoList,TodoItem}.java
touch src/main/resources/META-INF/persistence.xml
touch src/test/resources/init-test.sql
touch src/test/java/xpto/baz/MainTest.java
jbang init --deps \
org.hibernate.orm:hibernate-core:6.5.2.Final,\
com.h2database:h2:2.2.224 \
Build.java
```

In jbang entry point, configure the folder structure and call the actual main 
class:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS org.eclipse.persistence:eclipselink:4.0.2
//DEPS com.h2database:h2:2.2.224
//SOURCES src/main/java/**/*
//FILES src/main/resources/

void main(String... args) throws Exception {
    xpto.baz.Main.main(args);
}
```

The example is pretty straightforward, a simple JPA use case:

```java
package xpto.baz;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.List;

public class Main {

    private final EntityManagerFactory emf = Persistence //
            .createEntityManagerFactory("default");

    public List<TodoList> list() {
        try (EntityManager em = emf.createEntityManager()) {
            return em.createQuery("""
                    select t from TodoList t
                    """, TodoList.class).getResultList();
        }
    }

    public TodoList add(String list, String task) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            TodoList todoList = new TodoList(list);
            TodoItem todoItem = new TodoItem(todoList, task);
            em.persist(todoList);
            em.persist(todoItem);
            em.flush();
            em.clear();
            em.getTransaction().commit();
            todoList.getItems().add(todoItem);
            return todoList;
        }
    }

    public void close() {
        emf.close();
    }

    public static void main(String... args) throws Exception {
        var app = new Main();
        app.add("today", "walk the dog");
        List<TodoList> result = app.list();
        result.forEach(IO::println);
        app.close();
    }
}
```

## How to test

Nothing new so far, so, how to call a test runner in a jbang Scenario? Worth 
 mentioning, how to keep the runtime classpath clean from test dependencies?

Easiest way: **Add a Test entrypoint**:

```bash
jbang init --deps \
org.junit.jupiter:junit-jupiter:5.11.0,\
org.junit.platform:junit-platform-launcher:1.11.0,\
org.hamcrest:hamcrest:3.0 \
Test.java
```

In the class `Test.java`, add the _Build.java_ main jbang entry point as 
a `//SOURCES` dependency, along with instructions to configure the test 
classpath and the code to execute the tests  and print the test summary:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS org.junit.jupiter:junit-jupiter:5.11.0
//DEPS org.junit.platform:junit-platform-launcher:1.11.0
//DEPS org.hamcrest:hamcrest:3.0
//SOURCES Build.java
//SOURCES src/test/java/**/*
//FILES src/test/resources/

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.engine.discovery.DiscoverySelectors;

void main(String... args) throws Exception {
    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
        .request()
        .selectors(DiscoverySelectors
            .selectPackage("xpto.baz")) // put your tests under this package
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
}
```

And the tests are pretty standard java tests:

```java
package xpto.baz;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class MainTest {

    final Main main = new Main();

    @Test
    void shouldCreateTodo() {
        var result = main.add("today", "doomscrolling");
        assertThat(result, is(not(nullValue())));
        assertThat(result.getId(), notNullValue());
        assertThat(result.getId(), greaterThan(0L));
    }
}
```

## What about coverage

If a test serves to the purpose of confidently assert that the software should 
work, the coverage describes **how much code** should be working.

In any big boy project the minimum coverage lies about 80%, but this isn't a 
general rule. What is for sure is that covered code should not produce 
surprises. If it does, there is a flaky test.

The most popular coverage tool for java is [jacoco][jacoco].

[jacoco]: https://www.eclemma.org/jacoco/

To add it into our setup, add the dependency and create a extra coverage 
function to the test script:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS org.junit.jupiter:junit-jupiter:5.11.0
//DEPS org.junit.platform:junit-platform-launcher:1.11.0
//DEPS org.hamcrest:hamcrest:3.0
//DEPS org.jacoco:org.jacoco.core:0.8.13
//SOURCES Build.java
//SOURCES src/test/java/**/*
//FILES src/test/resources/

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

void main(String... args) throws Exception {
    IRuntime runtime = new LoggerRuntime();
    RuntimeData data = new RuntimeData();
    runtime.startup(data);

    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
        .request()
        .selectors(DiscoverySelectors.selectPackage("xpto.baz"))
        .build();

    SummaryGeneratingListener listener = new SummaryGeneratingListener();

    try (LauncherSession session = LauncherFactory.openSession()) {
        Launcher launcher = session.getLauncher();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
    } finally {
        runtime.shutdown();
    }

    var summary = listener.getSummary();
    summary.printFailuresTo(new PrintWriter(System.err), 1);
    summary.printTo(new PrintWriter(System.out));

    coverage(data);

    System.exit(summary.getTestsFailedCount() > 0 ? 1 : 0);
}

void coverage(RuntimeData data) throws IOException {
    ExecutionDataStore executionData = new ExecutionDataStore();
    SessionInfoStore sessionInfo = new SessionInfoStore();
    data.collect(executionData, sessionInfo, false);

    CoverageBuilder coverageBuilder = new CoverageBuilder();
    // Agora passamos a estrutura correta (executionData) para o Analyzer
    Analyzer analyzer = new Analyzer(executionData, coverageBuilder);

    String classpath = System.getProperty("java.class.path");
    for (String path : classpath.split(File.pathSeparator)) {
        File file = new File(path);
        if (file.isFile()
            && file.getName().endsWith(".jar")
            && file.getAbsolutePath().contains(".jbang")) {
            analyzer.analyzeAll(file);
        }
    }

    System.out.println("\n=== (JACOCO COVERAGE) ===");
    System.out.printf("%-40s %-10s %-10s %-10s\n",//
        "Class", "Covered", "Total", "% Coverage");
    System.out.println("-".repeat(75));

    int totalLines = 0;
    int coveredLines = 0;

    for (IClassCoverage classCoverage : coverageBuilder.getClasses()) {
        if (classCoverage.getName().endsWith("Test")
            || classCoverage.getName().equals("Test")
            || classCoverage.getName().equals("Build")) {
            continue;
        }

        ICounter lineCounter = classCoverage.getLineCounter();
        int total = lineCounter.getTotalCount();
        int covered = lineCounter.getCoveredCount();
        double pct = total > 0 ? ((double) covered / total) * 100 : 0.0;
        String name = classCoverage.getName().replace('/', '.');

        System.out.printf("%-40s %-10d %-10d %-9.1f%%\n",//
            name, covered, total, pct);

        totalLines += total;
        coveredLines += covered;
    }

    System.out.println("-".repeat(75));
    double pct = totalLines > 0
        ? ((double) coveredLines / totalLines) * 100
        : 0.0;
    System.out.printf("%-40s %-10d %-10d %-9.1f%%\n", //
        "TOTAL", coveredLines, totalLines, pct);
    System.out.println("=".repeat(75));
}
```

That way both test and coverage reports will be provided to the project.

## It's getting complex

Well, yes, but it comes in incremental steps.

jbang also offers an export tool, if you feel that the project should be 
managed in a more old-fashioned way, using gradle ofr maven.
