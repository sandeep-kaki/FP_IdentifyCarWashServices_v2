package com.indiamart.pages;

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

/**
 * ServiceDetailPage – Page Object for an individual service/company detail page.
 *
 * LOCATOR PRIORITY:
 *   XPath with ID used for phone – //*[@id='pns_no2']//a[contains(@href,'tel')]
 *   This is the exact locator from the updated linear code.
 *
 * COVERS:
 *   TC01 – opened in a new tab for each of the top 3 results.
 *          Extracts the phone number then the tab is closed.
 */
public class ServiceDetailPage {

    private static final Logger log = LogManager.getLogger(ServiceDetailPage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // ── Constructor ──────────────────────────────────────────────────────────

    public ServiceDetailPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.js     = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    // ── Actions ──────────────────────────────────────────────────────────────

    /**
     * Closes the unlock/sign-up popup (idfpclose) if it appears on the detail page.
     * Uses JS click because the element may be partially obscured.
     * WAIT TYPE: Explicit wait (5 sec) – short to avoid slowing down the loop.
     */
    public void closePopupIfPresent() {
        try {
            List<WebElement> popups = driver.findElements(By.id("idfpclose"));
            if (!popups.isEmpty()) {
                js.executeScript("arguments[0].click();", popups.get(0));
                log.debug("Detail page popup dismissed.");
                Thread.sleep(500);   // brief pause so popup animation completes
            }
        } catch (Exception e) {
            log.debug("No popup on detail page – continuing.");
        }
    }

    /**
     * Extracts the phone number from the detail page.
     *
     * LOCATOR: XPath with ID (highest priority – pns_no2 is the phone section)
     * The href attribute starts with "tel:" – we strip that prefix to get the number.
     *
     * Returns "Hidden (Unlock required)" if the phone is behind a paywall.
     * WAIT TYPE: Explicit wait with try-catch – does not fail the test if hidden.
     */
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
