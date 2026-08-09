///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS org.liquibase:liquibase-core:5.0.3
//DEPS org.springframework.boot:spring-boot-starter-web:4.1.0
//DEPS org.springframework.boot:spring-boot-starter-data-jpa:4.1.0
//DEPS org.springframework.boot:spring-boot-starter-liquibase:4.1.0
//DEPS org.springframework.boot:spring-boot-starter-test:4.1.0
//DEPS org.junit.platform:junit-platform-launcher:6.1.2
//DEPS org.hamcrest:hamcrest:3.0
//DEPS com.h2database:h2:2.3.232
//SOURCES src/**/*.java
//FILES resources

import app.App;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import java.io.PrintWriter;

void main(String... args) {
    if (args.length > 0 && "test".equals(args[0])) {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
            .request()
            .selectors(DiscoverySelectors.selectPackage("app"))
            .build();

        SummaryGeneratingListener listener = new SummaryGeneratingListener();

        try (LauncherSession session = LauncherFactory.openSession()) {
            Launcher launcher = session.getLauncher();
            launcher.registerTestExecutionListeners(listener);
            launcher.execute(request);
        }

        var summary = listener.getSummary();
        summary.printFailuresTo(new PrintWriter(System.err), 1);
        summary.printTo(new PrintWriter(System.out));

        System.exit(summary.getTestsFailedCount() > 0 ? 1 : 0);
    } else {
        App.main(args);
    }
}
