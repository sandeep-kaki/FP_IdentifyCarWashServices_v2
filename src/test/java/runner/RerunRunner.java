package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

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
