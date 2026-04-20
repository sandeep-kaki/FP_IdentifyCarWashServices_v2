package com.indiamart.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * DriverManager – Utility class (ThreadLocal pattern).
 *
 * PURPOSE:
 *   Creates and manages the WebDriver instance for each scenario.
 *   ThreadLocal ensures each test thread has its own driver
 *   (safe even if scenarios run in parallel later).
 *
 * NO WebDriverManager needed:
 *   Selenium 4 ships with "Selenium Manager" built-in.
 *   It automatically finds and downloads the correct ChromeDriver
 *   version matching your installed Chrome. Works out of the box.
 *
 * MULTI-BROWSER:
 *   Set  browser=chrome  OR  browser=firefox  in config.properties.
 *   For Firefox, geckodriver must be available in your system PATH.
 *
 * LIFECYCLE (called from Hooks.java):
 *   initDriver()  → @Before each scenario
 *   getDriver()   → used by all Page Objects and Step Definitions
 *   quitDriver()  → @After each scenario
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);

    // Each thread (scenario) gets its own WebDriver stored here
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    // Prevent instantiation – this is a pure utility class
    private DriverManager() {}

    /**
     * Creates a new WebDriver based on the 'browser' value in config.properties.
     * Called once at the start of each scenario via the @Before hook.
     */
    public static void initDriver() {
        String browser = ConfigReader.getInstance().get("browser").toLowerCase();
        log.info("Initializing browser: {}", browser);

        WebDriver driver;

        switch (browser) {

            case "edge":
                // Firefox – geckodriver must be in system PATH
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                driver.manage().window().maximize();
                log.info("Edge started.");
                break;

            case "chrome":
            default:
                // Chrome – Selenium 4 Selenium Manager handles driver download
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(chromeOptions);
                log.info("ChromeDriver started.");
                break;
        }

        driverThread.set(driver);
    }

    /** Returns the WebDriver for the current thread/scenario. */
    public static WebDriver getDriver() {
        return driverThread.get();
    }

    /**
     * Quits the browser and removes the driver from ThreadLocal.
     * Called once at the end of each scenario via the @After hook.
     * Removing from ThreadLocal prevents memory leaks.
     */
    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
            log.info("Browser closed and driver removed.");
        }
    }
}
