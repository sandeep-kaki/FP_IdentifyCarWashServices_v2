package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class AllureReportOpener {

    private static final Logger logger =
            LogManager.getLogger(AllureReportOpener.class);

    public static void cleanAllureResults() {
        File resultsDir = new File("allure-results");
        if (resultsDir.exists()) {

            logger.info("Cleaning existing Allure results directory");

            File[] files = resultsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    boolean    deleted = file.delete();
                    if(!deleted)
                    {
                        logger.warn("Unable to delete file: {}", file.getName());
                    }
                }
            }
            logger.info("Allure results directory cleaned");
        } else {
            logger.info("No existing Allure results directory found");
        }
    }

    public static void openAllureReport() {
        try {
            String resultsDir = "allure-results";
            logger.info("Opening Allure report from directory: {}", resultsDir);

            ProcessBuilder pb = new ProcessBuilder(
                    "cmd.exe", "/c", "allure serve " + resultsDir
            );
            Process pro = pb.start();

            pb.start();

            logger.info("Allure report server started successfully");
            pro.destroy();



        } catch (IOException e) {

            logger.error("Failed to open Allure report. Ensure Allure CLI is installed and added to PATH.", e);
            e.printStackTrace();
        }
    }
}