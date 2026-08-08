//DEPS jakarta.platform:jakarta.jakartaee-web-api:11.0.0
// App.java
package app;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.ApplicationPath;

@ApplicationPath("api")
public class App extends Application {}
