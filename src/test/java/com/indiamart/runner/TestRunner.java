package com.indiamart.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * TestRunner – Main Cucumber + TestNG Runner.
 *
 * HOW TestNG + CUCUMBER WORK TOGETHER HERE:
 *   1. Maven Surefire reads testng.xml
 *   2. testng.xml points to this class
 *   3. This class extends AbstractTestNGCucumberTests
 *      → TestNG treats it as a standard test class
 *      → AbstractTestNGCucumberTests provides the @Test method
 *         that iterates over all scenarios in the feature file
 *   4. @CucumberOptions tells Cucumber where feature files and step defs are
 *
 * PLUGINS:
 *   pretty   → clean readable console output during test run
 *   Allure   → generates JSON result files in target/allure-results/
 *              After test run, open report with:  mvn allure:serve
 *   rerun    → writes paths of FAILED scenarios to target/failed_scenarios.txt
 *              Used by RerunRunner to run only failed scenarios next time
 *
 * RUN COMMANDS:
 *   All scenarios  →  mvn test
 *   Single tag     →  mvn test -Dcucumber.filter.tags="@TC01"
 *   Multiple tags  →  mvn test -Dcucumber.filter.tags="@TC01 or @TC03"
 *   Smoke suite    →  mvn test -Dcucumber.filter.tags="@smoke"
 */
@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = {
                "com.indiamart.stepdefs",   // step definition classes
                "com.indiamart.hooks"       // @Before / @After hooks
        },
        plugin    = {
                "pretty",

                // Allure adapter – generates result files for Allure report
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",

                // Rerun plugin – writes failed scenario locations to a file
                // RerunRunner reads this file to retry only failed scenarios
                "rerun:target/failed_scenarios.txt"
        },
        monochrome = true   // removes ANSI color codes from console output (cleaner)
)
public class TestRunner extends AbstractTestNGCucumberTests {
    // No code needed – AbstractTestNGCucumberTests provides all the logic
}
