package utils;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public class ScreenshotUtil {

    private static final Logger log = LogManager.getLogger(ScreenshotUtil.class);

    // Prevent instantiation
    private ScreenshotUtil() {}

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
