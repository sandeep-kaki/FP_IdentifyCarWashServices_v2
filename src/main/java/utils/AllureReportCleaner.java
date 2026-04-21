package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class AllureReportCleaner {

    private static final Logger logger =
            LogManager.getLogger(AllureReportCleaner.class);

    public static void cleanAllureResults() {
        File resultsDir = new File("allure-results");
        if (resultsDir.exists()) {

            logger.info("Cleaning existing Allure results directory");

            // Only delete the files inside, not the folder itself,
            // to avoid permission issues during the next run
            File[] files = resultsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    boolean    deleted = file.delete();
                    if(!deleted)
                    {

                        logger.warn("Unable to delete file: {}", file.getName());

                    }
//                    file.delete();
                }
            }
            logger.info("Allure results directory cleaned");
        }else{

            logger.info("No existing Allure results directory found");

        }



    }
}