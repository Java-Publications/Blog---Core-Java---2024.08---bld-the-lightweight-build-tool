package com.svenruppert;

import rife.bld.BuildCommand;
import rife.bld.Project;
import rife.bld.dependencies.VersionNumber;
import rife.bld.extension.JacocoReportOperation;
import rife.bld.extension.PitestOperation;
import rife.bld.extension.CheckstyleOperation;

import java.nio.file.Path;
import java.util.List;

import static rife.bld.dependencies.Repository.MAVEN_CENTRAL;
import static rife.bld.dependencies.Repository.RIFE2_RELEASES;
import static rife.bld.dependencies.Scope.compile;
import static rife.bld.dependencies.Scope.test;

public class CoreBuild extends Project {

  @BuildCommand(summary = "Generates Jacoco Reports")
  public void jacoco() throws Exception {
    new JacocoReportOperation()
        .fromProject(this)
        .execute();
  }

  @BuildCommand(summary = "Run PIT mutation tests")
  public void pit() throws Exception {
    new PitestOperation()
        .fromProject(this)
        .reportDir(Path.of("reports", "mutations").toString())
        .targetClasses(pkg + ".*")
        .targetTests("junit." + pkg + ".*")
        .verbose(true)
        .execute();
  }

  @BuildCommand(summary = "Check code style")
  public void checkstyle() throws Exception {
    new CheckstyleOperation()
        .fromProject(this)
        .configurationFile("src/test/resources/sun_checks.xml")
        .execute();
  }

  public CoreBuild() {
    pkg = "com.svenruppert";
    name = "Core";
    mainClass = "com.svenruppert.Application";
    version = version(0, 1, 0,"SNAPSHOT");

    javaRelease = 17;
    downloadSources = true;
    downloadJavadoc = false;

    repositories = List.of(MAVEN_CENTRAL, RIFE2_RELEASES);

    VersionNumber versionJavalin = version(6, 3, 0);
    VersionNumber versionSLF4J = version(2, 0, 16);
    scope(compile)
        .include(dependency("io.javalin", "javalin", versionJavalin))
        .include(dependency("org.slf4j", "slf4j-api", versionSLF4J))
        .include(dependency("org.slf4j", "slf4j-simple", versionSLF4J));


    VersionNumber versionJunit5Jupiter = version(5, 11, 1);
    VersionNumber versionJunit5Plattform = version(1, 11, 1);
    scope(test)
        .include(dependency("org.junit.jupiter", "junit-jupiter", versionJunit5Jupiter))
        .include(dependency("org.junit.jupiter", "junit-jupiter-api", versionJunit5Jupiter))
        .include(dependency("org.junit.jupiter", "junit-jupiter-params", versionJunit5Jupiter))
        .include(dependency("org.junit.jupiter", "junit-jupiter-engine", versionJunit5Jupiter))
        .include(dependency("org.junit.platform", "junit-platform-launcher", versionJunit5Plattform))
        .include(dependency("org.junit.platform", "junit-platform-testkit", versionJunit5Plattform))
        .include(dependency("org.junit.platform", "junit-platform-console-standalone", versionJunit5Plattform))
        .include(dependency("io.javalin", "javalin-testtools", versionJavalin))
        .include(dependency("org.assertj", "assertj-core", version(3, 26, 3)))
        .include(dependency("io.rest-assured", "rest-assured", version(5, 5, 0)))
        .include(dependency("com.puppycrawl.tools", "checkstyle", version(10, 18, 1)))
        .include(dependency("org.pitest", "pitest", version(1, 17, 0)))
        .include(dependency("org.pitest", "pitest-command-line", version(1, 17, 0)))
        .include(dependency("org.pitest", "pitest-junit5-plugin", version(1, 2, 1)))
        .include(dependency("org.pitest", "pitest-testng-plugin", version(1, 0, 0)));
  }

  public static void main(String[] args) {
    new CoreBuild().start(args);
  }
}