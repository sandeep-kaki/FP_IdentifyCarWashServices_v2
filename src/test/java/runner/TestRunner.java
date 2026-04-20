package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features  = "src/test/resources/features",
        glue      = {
                "stepdefs",   // step definition classes
                "hooks"       // @Before & @After hooks
        },
        plugin    = {
                "pretty",

                // Allure adapter – generates result files for Allure report
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",

                // Rerun plugin – writes failed scenario locations to a file
                "rerun:target/failed_scenarios.txt"
        },
        monochrome = true   // removes ANSI color codes from console output
)
public class TestRunner extends AbstractTestNGCucumberTests {
    // AbstractTestNGCucumberTests provides all the logic
}
