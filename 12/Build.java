///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS io.javalin:javalin:7.2.2
//DEPS org.jdbi:jdbi3-core:3.54.0
//DEPS com.fasterxml.jackson.core:jackson-databind:2.22.1
//DEPS org.slf4j:slf4j-simple:2.0.13
//DEPS com.h2database:h2:2.2.224
//DEPS com.zaxxer:HikariCP:6.0.0
//SOURCES src/main/java
//FILES src/main/resources

void main(String... args) throws Exception {
    app.App.main(args);
}
