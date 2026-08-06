///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 25+
//DEPS org.junit.jupiter:junit-jupiter:5.11.0
//DEPS org.junit.platform:junit-platform-launcher:1.11.0
//DEPS org.hamcrest:hamcrest:3.0
//DEPS org.jacoco:org.jacoco.core:0.8.13
//SOURCES Build.java
//SOURCES src/test/java/**/*
//FILES src/test/resources/

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherSession;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.engine.discovery.DiscoverySelectors;

import org.jacoco.core.runtime.IRuntime;
import org.jacoco.core.runtime.LoggerRuntime;
import org.jacoco.core.runtime.RuntimeData;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.SessionInfoStore;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICounter;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

void main(String... args) throws Exception {
    IRuntime runtime = new LoggerRuntime();
    RuntimeData data = new RuntimeData();
    runtime.startup(data);

    LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
        .request()
        .selectors(DiscoverySelectors.selectPackage("xpto.baz"))
        .build();

    SummaryGeneratingListener listener = new SummaryGeneratingListener();

    try (LauncherSession session = LauncherFactory.openSession()) {
        Launcher launcher = session.getLauncher();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
    } finally {
        runtime.shutdown();
    }

    var summary = listener.getSummary();
    summary.printFailuresTo(new PrintWriter(System.err), 1);
    summary.printTo(new PrintWriter(System.out));

    coverage(data);

    System.exit(summary.getTestsFailedCount() > 0 ? 1 : 0);
}

void coverage(RuntimeData data) throws IOException {
    ExecutionDataStore executionData = new ExecutionDataStore();
    SessionInfoStore sessionInfo = new SessionInfoStore();
    data.collect(executionData, sessionInfo, false);

    CoverageBuilder coverageBuilder = new CoverageBuilder();
    // Agora passamos a estrutura correta (executionData) para o Analyzer
    Analyzer analyzer = new Analyzer(executionData, coverageBuilder);

    String classpath = System.getProperty("java.class.path");
    for (String path : classpath.split(File.pathSeparator)) {
        File file = new File(path);
        if (file.isFile()
            && file.getName().endsWith(".jar")
            && file.getAbsolutePath().contains(".jbang")) {
            analyzer.analyzeAll(file);
        }
    }

    System.out.println("\n=== (JACOCO COVERAGE) ===");
    System.out.printf("%-40s %-10s %-10s %-10s\n",//
        "Class", "Covered", "Total", "% Coverage");
    System.out.println("-".repeat(75));

    int totalLines = 0;
    int coveredLines = 0;

    for (IClassCoverage classCoverage : coverageBuilder.getClasses()) {
        if (classCoverage.getName().endsWith("Test")
            || classCoverage.getName().equals("Test")
            || classCoverage.getName().equals("Build")) {
            continue;
        }

        ICounter lineCounter = classCoverage.getLineCounter();
        int total = lineCounter.getTotalCount();
        int covered = lineCounter.getCoveredCount();
        double pct = total > 0 ? ((double) covered / total) * 100 : 0.0;
        String name = classCoverage.getName().replace('/', '.');

        System.out.printf("%-40s %-10d %-10d %-9.1f%%\n",//
            name, covered, total, pct);

        totalLines += total;
        coveredLines += covered;
    }

    System.out.println("-".repeat(75));
    double pct = totalLines > 0
        ? ((double) coveredLines / totalLines) * 100
        : 0.0;
    System.out.printf("%-40s %-10d %-10d %-9.1f%%\n", //
        "TOTAL", coveredLines, totalLines, pct);
    System.out.println("=".repeat(75));
}
