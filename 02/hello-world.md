# How to _hello world_ 

## Direct eval

Make jbang eval java code directly from the command line:

```bash
jbang run -c "System.out.println(\"Hello World\");"
```

## Create a simple script

Use jbang to initialize a simple script

```bash
jbang init Hello.java
```

The generated file goes like this:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+

void main(String... args) {
    IO.println("Hello World");
}
```

If you need a specific java version, pass the version as parameter in the init 
command:

```bash
jbang init --java=8 Hello.java
```

Then the Generated source file goes like this:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 8

import static java.lang.System.*;

public class Hello {

    public static void main(String... args) {
        out.println("Hello World");
    }
}
```

## Running

Call the entry script:

```bash
jbang Hello.java
```

One cool thing is that jbang downloads a suitable jdk based on what you 
provides in the `//JAVA <version>` comment in the script.

Those comments are relevant, as we'll see over the samples. 

## Other exotic entry points

JBang also supports jshell scripts and markdown files as entry points, but 
let's not cover those modes here. Just check the [official docs][1] if you want 
to do some exotic things.

[1]: https://www.jbang.dev/documentation/jbang/latest/multiple-languages.html
