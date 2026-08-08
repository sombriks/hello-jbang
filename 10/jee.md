# Going enterprise

Anyone aware of the JEE history would giggle a little just by thinking running 
those overly complex, full of xml configurations, ear/war java applications 
from a simple command line script.

Then [Spring Boot][boot] emerged and ate the market.

[boot]: https://spring.io/projects/spring-boot

On the other hand, modern [Jakarta EE][jee] applications fought complexity and 
kept API consistency.

I dare to say that JEE applications are simpler than any other option available 
today for enterprise development on java ecosystem.

And this is how some of some JEE players behave when we scale it down while 
trying to consume all the goodies from the Jakarta EE specification.

## The (portable) application

The application is a simple, honest JSON api:

```bash
mkdir -p src/app/{controllers,models,repositories,services}
touch src/app/App.java
touch src/app/controllers/TodoRes.java
touch src/app/models/Todo.java
touch src/app/repositories/TodoRepo.java
touch src/app/services/TodoSvc.java
mkdir src/META-INF
touch src/META-INF/persistence.xml
```

And the classes goes by the book:

```java
//DEPS jakarta.platform:jakarta.jakartaee-web-api:11.0.0
// App.java
package app;

@ApplicationPath("api")    
public class App extends Application {}
```
---
```java
// TodoRes.java
package app.controllers;

```

Other classes goes as expected by [JEE11 specification][jee11].

[jee11]: https://jakarta.ee/specifications/platform/11/

## Jbang and Helidon

## Jbang and Wildfly Glow

## Jbang and Payara Embedded

## State of the thing

