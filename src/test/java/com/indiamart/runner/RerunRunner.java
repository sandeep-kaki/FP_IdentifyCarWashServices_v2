package com.indiamart.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * RerunRunner – Reruns ONLY the scenarios that failed in the previous run.
 *
 * HOW RERUN WORKS:
 *   1. TestRunner has the plugin:  "rerun:target/failed_scenarios.txt"
 *   2. After a test run, if any scenario fails, Cucumber writes its
 *      file path + line number into target/failed_scenarios.txt
 *      Example content:  src/test/resources/features/IndiaMart.feature:23
 *   3. RerunRunner reads that file via  features = "@target/failed_scenarios.txt"
 *   4. Only the failed scenarios are re-executed
 *
 * HOW TO USE:
 *   Step 1:  mvn test                    (runs all scenarios, captures failures)
 *   Step 2:  mvn test -Dtest=RerunRunner (reruns only the failed ones)
 *
 *   OR to do both in one command (CI/CD style):
 *   mvn test; mvn test -Dtest=RerunRunner
 *
 * NOTE:
 *   If target/failed_scenarios.txt is empty (no failures), RerunRunner
 *   will run 0 scenarios – which is the correct behavior.
 */
@CucumberOptions(
        features = "@target/failed_scenarios.txt",   // reads the failed scenario locations
        glue     = {
                "com.indiamart.stepdefs",
                "com.indiamart.hooks"
        },
        plugin   = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "rerun:target/failed_scenarios_2.txt"  // in case rerun itself fails
        },
        monochrome = true
)
public class RerunRunner extends AbstractTestNGCucumberTests {
    // No code needed – same bridge pattern as TestRunner
}
