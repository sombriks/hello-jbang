# Resources

Resources are useful for configurations, templates and all sort of blobs you 
application migh need to consume in order to do the jobs.

In jbang, resources are pretty straightforward. For example:

```bash
touch users.xml
touch User.java
rouch log4j2.xml
jbang init --deps \
tools.jackson.dataformat:jackson-dataformat-xml:3.0.3,\
org.apache.logging.log4j:log4j-api:2.26.1,\
org.apache.logging.log4j:log4j-core:2.26.1 \
Build.java
```

Users xml would be like this:

```xml
<Users>
    <user id="1" name="Alice"/>
    <user id="2" name="Bobb"/>
</Users>
```

In Buyild.jva, we'll use the `//SOURCES` comment for the extra java sources and 
the `//FILES` for the resource files: 

```java
///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//COMPILE_OPTIONS -parameters
//DEPS tools.jackson.dataformat:jackson-dataformat-xml:3.0.3
//DEPS org.apache.logging.log4j:log4j-api:2.26.1
//DEPS org.apache.logging.log4j:log4j-core:2.26.1
//SOURCES User.java
//FILES log4j2.xml
//FILES users.xml

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import tools.jackson.dataformat.xml.XmlMapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;

 record Users(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "user")
    ArrayList<User> user){}

public class Build {

    private static final Logger LOG = LogManager.getLogger(Build.class);

    public static void main(String... args) throws Exception {
        LOG.info("Hello World");
        var mapper = XmlMapper.builder().build();
        var users = mapper.readValue(Build.class
        .getResourceAsStream("users.xml"), Users.class);
        LOG.info(users);
    }
}
```

The `//COMPILE_OPTIONS` is just to make jackson behave. More on that later. 

Both in `//FILES` and `//SOURCES` it's possible to indicate a list of
comma-separated file or folders with wildcards. (i.e. src/main/java/**/*.java).
