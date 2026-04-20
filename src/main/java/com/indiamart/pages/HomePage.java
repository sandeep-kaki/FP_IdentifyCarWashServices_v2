package com.indiamart.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * HomePage – Page Object for IndiaMart Home Page.
 *
 * LOCATOR PRIORITY USED THROUGHOUT THIS CLASS:
 *   1. ID        → fastest, unique, most reliable
 *   2. Name      → used for search box (no ID available)
 *   3. CSS       → not used here (XPath available and clear enough)
 *   4. XPath     → used when ID/Name/CSS are not available or not unique
 *
 * COVERS:
 *   TC01 – city selection + keyword search
 *   TC02 – Food, Agriculture & Farming section
 *   TC03 – click Sign In button
 *   TC04 – click Exporters tab
 */
public class HomePage {

    private static final Logger log = LogManager.getLogger(HomePage.class);

    private final WebDriver     driver;
    private final WebDriverWait wait;
    private final JavascriptExecutor js;

    // ── @FindBy Locators (PageFactory) ─────────────────────────────────────
    // PRIORITY: ID used wherever possible

    /** City label element – shows currently selected city */
    @FindBy(id = "hd_usercity")
    private WebElement cityDisplay;

    /**
     * City text input – appears after clicking cityDisplay.
     * LOCATOR: ID (updated from new linear code – was sending keys to cityDisplay before)
     */
    @FindBy(id = "hd_city_sugg")
    private WebElement cityInput;

    /**
     * City autocomplete suggestion item.
     * LOCATOR: XPath (no ID/Name on the anchor – anchor id is "anchor")
     */
    @FindBy(xpath = "//*[@id='anchor']")
    private WebElement cityAutoSuggest;

    /** Search keyword input box. LOCATOR: Name (no ID on this element) */
    @FindBy(name = "ss")
    private WebElement searchBox;

    /** Food, Agriculture & Farming section link. LOCATOR: linkText */
    @FindBy(linkText = "Food, Agriculture & Farming")
    private WebElement foodAgriLink;

    /** Sign In button in the top nav. LOCATOR: ID */
    @FindBy(id = "user_sign_in")
    private WebElement signInButton;

    /** Exporters tab in the top nav. LOCATOR: ID */
    @FindBy(id = "exporters")
    private WebElement exportersTab;

    // ── Constructor ──────────────────────────────────────────────────────────

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.js     = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);   // wires @FindBy elements
    }

    // ── TC01 Actions ─────────────────────────────────────────────────────────

    /**
     * Sets the search location to the target city.
     * Skips the action if the city is already set to the desired value.
     */
    public void setCity(String targetCity) {
        String current = cityDisplay.getText().trim();
        log.info("Current city: '{}', Target city: '{}'", current, targetCity);

        if (!current.equalsIgnoreCase(targetCity)) {
            cityDisplay.click();
            cityInput.sendKeys(targetCity);                                   // type in city input box
            wait.until(ExpectedConditions.elementToBeClickable(cityAutoSuggest)).click(); // pick suggestion
            log.info("City set to: {}", targetCity);
        } else {
            log.info("City already set to {}. Skipping city selection.", targetCity);
        }
    }

    /**
     * Types the keyword in the search box and presses ENTER.
     */
    public void searchFor(String keyword) {
        log.info("Searching for: {}", keyword);
        searchBox.sendKeys(keyword, Keys.ENTER);
    }

    // ── TC02 Actions ─────────────────────────────────────────────────────────

    /**
     * Scrolls the Food, Agriculture & Farming section header into view.
     * Uses JavascriptExecutor because the element may be below the fold.
     */
    public void scrollToFoodAgriSection() {
        wait.until(ExpectedConditions.visibilityOf(foodAgriLink));
        js.executeScript("arguments[0].scrollIntoView(true)", foodAgriLink);
        log.info("Scrolled to Food, Agriculture & Farming section.");
    }

    /**
     * Dismisses the floating popup (idfpclose) if it appears.
     * Uses a short 5-second wait so the test doesn't block if popup is absent.
     * WAIT TYPE: Explicit wait with try-catch – does not fail if popup is missing.
     */
    public void closePopupIfPresent() {
        try {
            WebElement popup = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(By.id("idfpclose")));
            js.executeScript("arguments[0].click()", popup);
            log.info("Popup dismissed.");
        } catch (Exception e) {
            log.debug("No popup found – continuing.");
        }
    }

    /**
     * Returns all product/sub-category names from the Food, Agriculture & Farming section.
     *
     * XPath Logic:
     *   Start at the <h2> whose text matches the section name,
     *   then walk into its following-sibling divs to find product-item h3 elements.
     *
     * NOTE: If this returns empty, inspect the page in DevTools and
     *       check if the class 'product-item' has changed.
     */
    public List<String> getFoodAgriProductNames() {
        List<WebElement> items = driver.findElements(By.xpath(
                "//h2[normalize-space()='Food, Agriculture & Farming']" +
                "/following-sibling::div //div[contains(@class,'product-item')]//h3"));

        List<String> names = new ArrayList<>();
        for (WebElement el : items) {
            String text = el.getText().trim();
            if (!text.isEmpty()) names.add(text);
        }
        log.info("Found {} products in Food, Agriculture & Farming.", names.size());
        return names;
    }

    // ── TC03 Action ──────────────────────────────────────────────────────────

    /** Opens the Sign In modal. */
    public void clickSignIn() {
        log.info("Clicking Sign In button.");
        signInButton.click();
    }

    // ── TC04 Action ──────────────────────────────────────────────────────────

    /** Navigates to the Exporters section. */
    public void clickExporters() {
        log.info("Clicking Exporters tab.");
        exportersTab.click();
    }
}
