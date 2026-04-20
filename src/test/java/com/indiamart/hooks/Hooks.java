package com.indiamart.hooks;

import com.indiamart.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Hooks – Cucumber lifecycle hooks for setup and teardown.
 *
 * @Before  → runs before each scenario: initializes the browser
 * @After   → runs after each scenario: attaches screenshot to Allure report on failure,
 *            then quits the browser
 *
 * WHY IN A SEPARATE CLASS?
 *   Keeps step definition classes focused on business logic only.
 *   Hooks are infrastructure concerns – they belong separately.
 */
public class Hooks {

    private static final Logger log = LogManager.getLogger(Hooks.class);

    /**
     * Runs BEFORE every scenario.
     * Initializes WebDriver based on browser set in config.properties.
     */
    @Before
    public void setUp(Scenario scenario) {
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        log.info("STARTING: {}", scenario.getName());
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        DriverManager.initDriver();
    }

    /**
     * Runs AFTER every scenario.
     *
     * On FAILURE:
     *   Captures a screenshot and attaches it directly to the Allure report.
     *   (Allure shows this inline when you open the failed scenario in the report.)
     *
     * Always:
     *   Quits the browser via DriverManager.quitDriver().
     *
     * NOTE: This failure screenshot is SEPARATE from the intentional screenshots
     * in TC03 and TC04 that are saved to ./Screenshots/ on disk by ScreenshotUtil.
     */
    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();

        if (scenario.isFailed() && driver != null) {
            // Attach screenshot as bytes – Allure embeds it in the report HTML
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "FAILURE SCREENSHOT: " + scenario.getName());
            log.error("Scenario FAILED: {}. Screenshot attached to Allure report.", scenario.getName());
        }

        DriverManager.quitDriver();

        log.info("FINISHED: {} [{}]", scenario.getName(), scenario.getStatus());
        log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
    }
}
