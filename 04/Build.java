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
