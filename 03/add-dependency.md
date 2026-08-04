# Dependency management

So far, jbang is a neat toy, but any simple java project nowadays must be able 
to consume maven dependencies or some sort of external library registry.

It can do that too!

## Creating a script with dependencies

To init a script with dependencies:

```bash
jbang init --deps=com.esotericsoftware:minlog:1.3.1 Hello.java
```

The script will look like this:

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS com.esotericsoftware:minlog:1.3.1

// i modified the script a little to make use of the dependency
import com.esotericsoftware.minlog.Log;

void main(String... args) {
    // IO.println("Hello World");
    Log.info("This is a tiny log message.");    
}
```

Run it:

```Bash
sombriks@erebus 03 $ jbang Hello.java
[jbang] Resolving dependencies...
[jbang]    com.esotericsoftware:minlog:1.3.1
[jbang] Dependencies resolved
[jbang] Building jar for Hello.java...
00:00  INFO: This is a tiny log message.
sombriks@erebus 03 $
```

Now we're talking.

## Add more dependencies

In order to add more dependencies, either provide a comma-separated list of 
maven coordinates (or [GAV][2] as jbang refers to them), during the init 
command or add them by hand in the script:

[2]: https://share.google/aimode/M2HvPaalwwxjcEBwD

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS com.esotericsoftware:minlog:1.3.1
//DEPS com.google.code.gson:gson:2.11.0

import com.esotericsoftware.minlog.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

record Todo(String description, boolean done){}

void main(String... args) {
    Log.info("This is a tiny log message.");

    var list = new ArrayList();
    list.add(new Todo("Do the dishes",true));
    list.add(new Todo("Walk the dog",false));

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String jsonOutput = gson.toJson(list);
    Log.info(jsonOutput);
}
```
