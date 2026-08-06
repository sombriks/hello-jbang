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

Add the class `Test.java` along the Build.java main jbang entry point:

```java

```
