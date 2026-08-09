
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
