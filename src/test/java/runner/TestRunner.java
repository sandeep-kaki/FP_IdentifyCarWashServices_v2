package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.AllureReportCleaner;

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

                // Cucumber report
                "html:target/cucumber-report.html",

                // Rerun plugin – writes failed scenario locations to a file
                "rerun:target/failed_scenarios.txt"
        },
        monochrome = true   // removes ANSI color codes from console output
)
public class TestRunner extends AbstractTestNGCucumberTests {
        @BeforeSuite
        public void beforeSuite() {
                System.out.println("--- Starting Test Suite: Allure Results Cleaned ---");
                AllureReportCleaner.cleanAllureResults();
        }

}
