package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.AllureReportOpener;

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
        monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
        @BeforeSuite
        public void beforeSuite() {
                System.out.println("--- Starting Test Suite: Allure Results Cleaned ---");
                AllureReportOpener.cleanAllureResults();
        }

        @AfterSuite
        public void afterSuite() {
                System.out.println("--- All Tests Finished. Launching Allure Report ---");
                AllureReportOpener.openAllureReport();
        }
}
