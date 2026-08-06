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
//DEPS org.hibernate.orm:hibernate-core:6.5.2.Final
//DEPS com.h2database:h2:2.2.224
//SOURCES src/main/java/**/*
//FILES src/main/resources/

void main(String... args) {
    xpto.baz.Main.main(args);
}
```
