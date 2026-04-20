package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ServiceDetailPage {

    private static final Logger log = LogManager.getLogger(ServiceDetailPage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // -- Constructor ----------------------------------------------------

    public ServiceDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js     = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    //  Actions ----------------------------------------------------

    public void closePopupIfPresent() {
        try {
            List<WebElement> popups = driver.findElements(By.id("idfpclose"));
            if (!popups.isEmpty()) {
                js.executeScript("arguments[0].click();", popups.get(0));
                log.debug("Detail page popup dismissed.");
                Thread.sleep(500);   // letting popup animation to complete
            }
        } catch (Exception e) {
            log.debug("No popup on detail page – continuing.");
        }
    }

    public String getPhoneNumber() {
        try {
            WebElement phoneEl = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[@id='pns_no2']//a[contains(@href,'tel')]")));

            String phone = phoneEl.getAttribute("href").replace("tel:", "").trim();
            log.info("Phone extracted: {}", phone);
            return phone;

        } catch (Exception e) {
            log.warn("Phone not visible – possibly locked behind paywall.");
            return "Hidden (Unlock required)";
        }
    }
}
