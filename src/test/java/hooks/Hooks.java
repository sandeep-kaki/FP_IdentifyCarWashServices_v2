package hooks;

import utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    // Initializes WebDriver based on browser set in config.properties.
    @Before
    public void setUp(Scenario scenario) {
        log.info("----------------------------------------------");
        log.info("STARTING: {}", scenario.getName());
        log.info("----------------------------------------------");
        DriverManager.initDriver();
    }

    // Runs AFTER every scenario.
    // Quits the browser via DriverManager.quitDriver().
    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();

        if (scenario.isFailed() && driver != null) {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "FAILURE SCREENSHOT: " + scenario.getName());
            log.error("Scenario FAILED: {}. Screenshot attached to Allure report.", scenario.getName());
        }

        DriverManager.quitDriver();

        log.info("FINISHED: {} [{}]", scenario.getName(), scenario.getStatus());
        log.info("----------------------------------------------");
    }
}
