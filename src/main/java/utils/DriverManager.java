package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);

    // Each thread (scenario) gets its own WebDriver stored here
    private static final ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    // Prevent instantiation – this is a pure utility class
    private DriverManager() {}

    public static void initDriver() {
        String browser = ConfigReader.getInstance().get("browser").toLowerCase();
        log.info("Initializing browser: {}", browser);

        WebDriver driver;

        switch (browser.toLowerCase()) {

            case "edge":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--disable-notifications");
                driver = new EdgeDriver(edgeOptions);
                driver.manage().window().maximize();
                log.info("Edge started.");
                break;

            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(chromeOptions);
                log.info("ChromeDriver started.");
                break;
        }

        driverThread.set(driver);
    }

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
            log.info("Browser closed and driver removed.");
        }
    }
}
