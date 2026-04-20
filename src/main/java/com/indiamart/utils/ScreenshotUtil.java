package com.indiamart.utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

/**
 * ScreenshotUtil – Utility class for saving screenshots to disk.
 *
 * PURPOSE:
 *   Used for INTENTIONAL screenshots requested by the problem statement:
 *     TC03 – captures the validation error on invalid phone number
 *     TC04 – captures the top product categories section
 *
 *   FAILURE screenshots (any scenario that fails) are handled separately
 *   in Hooks.java @After – those are attached to the Allure report directly.
 *
 * OUTPUT:
 *   ./Screenshots/<fileName>.png
 */
public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);

    // Prevent instantiation
    private ScreenshotUtil() {}

    /**
     * Captures the current browser screen and saves it as a PNG.
     *
     * @param driver   the active WebDriver
     * @param fileName file name WITHOUT extension (e.g. "invalid_number_error")
     */
    public static void take(WebDriver driver, String fileName) {
        try {
            // Create Screenshots folder if it does not exist
            File screenshotsDir = new File("./Screenshots");
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }

            File source      = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destination = new File("./Screenshots/" + fileName + ".png");
            FileUtils.copyFile(source, destination);

            log.info("Screenshot saved → {}", destination.getPath());

        } catch (IOException e) {
            log.error("Failed to save screenshot '{}': {}", fileName, e.getMessage());
        }
    }
}
